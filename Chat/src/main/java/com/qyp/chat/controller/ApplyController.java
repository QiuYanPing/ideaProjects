package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.entity.Apply;
import com.qyp.chat.service.IApplyService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 申请 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    IApplyService applyService;

    @PostMapping("/applyAdd")
    public R applyAdd(String contactId,String applyInfo){
        Integer joinType = applyService.applyAdd(contactId,applyInfo);
        return R.success(joinType);
    }

    @PostMapping("/loadApply")
    public R loadApply( @RequestParam(defaultValue = "1") Integer pageNo){
        List<Apply> applyList = applyService.loadApply(pageNo);
        return R.success(applyList);
    }

    @PostMapping("/dealWithApply")
    public R dealWithApply(Integer applyId,Integer status){
        applyService.dealWithApply(applyId,status);
        return R.success(null);
    }

}
