package com.qyp.chat.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.domain.enums.GroupStatusEnum;
import com.qyp.chat.domain.vo.GroupVO;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.mapper.GroupMapper;
import com.qyp.chat.service.IGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {
    @Autowired
    UserUtils userUtils;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    AppConfig appConfig;
    @Override
    @Transactional
    public void saveGroup(Group group, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        UserInfoDTO userInfoDTO = userUtils.get();
        if(StrUtil.isEmpty(group.getGroupId())){
            //新增
            SysSettingDTO sysSetting = userUtils.getSysSetting();
            Integer maxGroupCount = sysSetting.getMaxGroupCount();
            //查询当前用户已创建的群聊数量
            Integer count = lambdaQuery().eq(Group::getGroupOwnerId, userInfoDTO.getUserId()).count();
            if(count == maxGroupCount)
                throw new BusinessException("创建的群聊数已达最大！");

            String groupId = createGroupId();
            group.setGroupId(groupId);
            group.setCreateTime(dateTime);
            save(group);

            //将群聊添加为联系人
            Contact contact = new Contact();
            contact.setUserId(userInfoDTO.getUserId());
            contact.setContactType(ContactTypeEnum.GROUP.getType());
            contact.setContactId(groupId);
            contact.setCreateTime(dateTime);
            contact.setLastUpdateTime(dateTime);
            contact.setStatus(ContactStatusEnum.FRIEND.getStatus());
            contactMapper.insert(contact);

            //todo 创建回话
            //todo 发送创建群聊成功的信息

        }else{
            //修改
            //判断当前用户是否为群主，只有为群主才可以对群信息进行修改
            Group  group1 = getById(group.getGroupId());
            if(!userInfoDTO.getUserId().equals(group1.getGroupOwnerId()))
                throw new BusinessException("非法修改！！");
            updateById(group);
        }
        if(avatarFile == null){
           return;
        }
        //上传群聊头像
        String projectFolder = appConfig.getProjectFolder();
        String path = projectFolder + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR;
        File fileFolder = new File(path);
        if(!fileFolder.exists()){
            //文件夹不存在
            fileFolder.mkdirs();
        }
        String filePath = fileFolder.getPath()+"/"+group.getGroupId()+SysConstant.IMAGE_SUFFIX;
        String coverPath = fileFolder.getPath()+"/"+group.getGroupId()+SysConstant.COVER_IMAGE_SUFFIX;
        avatarFile.transferTo(new File(filePath));
        avatarCover.transferTo(new File(coverPath));

    }

    @Override
    public List<Group> loadMyGroup(String userId) {
        List<Group> list = lambdaQuery().eq(Group::getGroupOwnerId, userId)
                .orderByDesc(Group::getCreateTime).list();
        return list;
    }

    @Override
    public Group getGroupInfo(String userId, String groupId) {
        Group group = getGroup(userId, groupId);
        //查询群聊人数
        LambdaQueryChainWrapper<Contact> wrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Integer count = wrapper.eq(Contact::getContactId, groupId).
                eq(Contact::getStatus,ContactStatusEnum.FRIEND.getStatus()).count();
        group.setMemberCount(count);
        return group;
    }

    @Override
    public GroupVO getGroupInfoDetail(String userId, String groupId) {
        Group group = getGroup(userId, groupId);
        //查询群聊成员信息
        List<Contact> contactList = contactMapper.selectGroupMember(groupId);
        GroupVO groupVO = new GroupVO();
        groupVO.setGroup(group);
        groupVO.setContactList(contactList);
        return groupVO;
    }

    private Group getGroup(String userId, String groupId) {
        //当前用户需再群聊中
        LambdaQueryWrapper<Contact> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Contact::getContactId, groupId)
                .eq(Contact::getUserId, userId);
        Contact contact = contactMapper.selectOne(lambdaQueryWrapper);
        if(contact == null || !ContactStatusEnum.FRIEND.getStatus().equals(contact.getStatus()))
            throw new BusinessException("你不在群聊中或群聊不存在！");

        //查询的群聊应存在
        Group group = lambdaQuery().eq(Group::getGroupId, groupId).one();
        if(group == null || !GroupStatusEnum.NORMAL.getStatus().equals(group.getStatus()))
            throw new BusinessException("群聊不存在或群聊已解散！");
        return group;
    }


    private String createGroupId(){
        return ContactTypeEnum.GROUP.getPrefix()+ RandomUtil.randomString(11);
    }
}
