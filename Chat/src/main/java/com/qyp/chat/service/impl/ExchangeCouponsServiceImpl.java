package com.qyp.chat.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.qyp.chat.domain.entity.Coupons;
import com.qyp.chat.domain.entity.ExchangeCoupons;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.mapper.CouponsMapper;
import com.qyp.chat.mapper.ExchangeCouponsMapper;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.service.IExchangeCouponsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 兑换优惠卷 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@Service
public class ExchangeCouponsServiceImpl extends ServiceImpl<ExchangeCouponsMapper, ExchangeCoupons> implements IExchangeCouponsService {
    @Autowired
    CouponsMapper couponsMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    IExchangeCouponsService exchangeCouponsService;

    @Override
    @Transactional
    public void exchange(String userId, Integer couponsId) {
        long time = System.currentTimeMillis();
        Coupons coupons = couponsMapper.selectById(couponsId);
        if(coupons == null)
            throw new BusinessException("不存在优惠卷");

        //判断秒杀的开始时间和结束时间
        Long beginTime = coupons.getBeginTime();
        Long endTime = coupons.getEndTime();
        if(time<beginTime)
            throw new BusinessException("活动未开始");
        if(time>endTime)
            throw new BusinessException("活动已结束");


        //扣减库存（乐观锁防止超卖）
        LambdaUpdateWrapper<Coupons> couponsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        couponsLambdaUpdateWrapper.setSql("count = count -1")
                .eq(Coupons::getCouponsId,couponsId)
                .gt(Coupons::getCount,0);
        int count = couponsMapper.update(null, couponsLambdaUpdateWrapper);
        if(count == 0)
            throw new BusinessException("库存不足");


        //记录兑换记录（悲观锁实现一人一单）
        ExchangeCoupons one = lambdaQuery().eq(ExchangeCoupons::getUserId, userId)
                .eq(ExchangeCoupons::getCouponsId, couponsId).one();
        if(one != null)
            throw new BusinessException("已抢购过，不可再抢购");
        synchronized (userId.intern()){
            exchangeCouponsService.record(userId,couponsId);
        }


        //支付金币
        Integer cost = coupons.getCost();
        //判断金币是否足够
        User user = userMapper.selectById(userId);
        if(cost>user.getCoins())
            throw new BusinessException("金币不足");
        LambdaUpdateChainWrapper<User> userLambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(userMapper);
        userLambdaUpdateChainWrapper.setSql("coins = coins - "+cost)
                .eq(User::getUserId,userId);


    }

    @Transactional
    public void record(String userId, Integer couponsId) {
        ExchangeCoupons one;
        one  = lambdaQuery().eq(ExchangeCoupons::getUserId, userId)
                .eq(ExchangeCoupons::getCouponsId, couponsId).one();
        if(one != null)
            throw new BusinessException("已抢购过，不可再抢购");
        ExchangeCoupons exchangeCoupons = new ExchangeCoupons();
        exchangeCoupons.setUserId(userId);
        exchangeCoupons.setCouponsId(couponsId);
        save(exchangeCoupons);
    }
}
