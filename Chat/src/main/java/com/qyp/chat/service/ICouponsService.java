package com.qyp.chat.service;

import com.qyp.chat.domain.entity.Coupons;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qyp.chat.domain.vo.CouponsVO;

import java.util.List;

/**
 * <p>
 * 优惠卷 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface ICouponsService extends IService<Coupons> {

    void saveCoupons(Coupons coupons);

    List<CouponsVO> loadCoupons(Integer pageNo, Integer pageSize);
}
