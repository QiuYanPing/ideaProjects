package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.service.IExchangeCouponsService;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 兑换优惠卷 前端控制器
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@RestController
@RequestMapping("/exchange-coupons")
public class ExchangeCouponsController {

    @Autowired
    IExchangeCouponsService exchangeCouponsService;

    @Autowired
    UserUtils userUtils;

    @PostMapping("/exchange")
    public R exchange(Integer couponsId){
        UserInfoDTO userInfoDTO = userUtils.get();
        exchangeCouponsService.exchange(userInfoDTO.getUserId(),couponsId);
        return R.success(null);

    }
}
