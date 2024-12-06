package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.Messsage;
import com.qyp.chat.mapper.MesssageMapper;
import com.qyp.chat.service.IMesssageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class MesssageServiceImpl extends ServiceImpl<MesssageMapper, Messsage> implements IMesssageService {

}
