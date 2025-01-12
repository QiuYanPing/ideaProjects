package com.qyp.chat.service;

import com.qyp.chat.domain.entity.Apply;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 * 申请 服务类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface IApplyService extends IService<Apply> {

    Integer applyAdd(String contactId,String applyInfo);

    List<Apply> loadApply(Integer pageNo);


    void dealWithApply(Integer applyId, Integer status);
}
