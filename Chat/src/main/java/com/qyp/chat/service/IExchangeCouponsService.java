package com.qyp.chat.service;

import com.qyp.chat.domain.entity.ExchangeCoupons;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 兑换优惠卷 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface IExchangeCouponsService extends IService<ExchangeCoupons> {

    void exchange(String userId, Integer couponsId);

    void record(String userId, Integer couponsId);
}
