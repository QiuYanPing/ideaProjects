package com.qyp.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyp.chat.domain.entity.Coupons;
import com.qyp.chat.domain.vo.CouponsVO;
import com.qyp.chat.mapper.CouponsMapper;
import com.qyp.chat.service.ICouponsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠卷 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@Service
public class CouponsServiceImpl extends ServiceImpl<CouponsMapper, Coupons> implements ICouponsService {

    @Override
    public void saveCoupons(Coupons coupons) {
        if(coupons.getCouponsId() != null){
            //修改
            updateById(coupons);
        }else{
            //新增
            save(coupons);
        }
    }

    @Override
    public List<CouponsVO> loadCoupons(Integer pageNo, Integer pageSize) {
        Page<Coupons> page = page(new Page<>(pageNo, pageSize));
        List<Coupons> records = page.getRecords();
        List<CouponsVO> couponsVOList = records.stream().map(item -> {
            CouponsVO couponsVO = BeanUtil.toBean(item, CouponsVO.class);
            couponsVO.setOutdateTime(Instant.ofEpochMilli(item.getOutdateTime())
                    .atZone(ZoneOffset.of("+8")).toLocalDateTime());
            couponsVO.setBeginTime(Instant.ofEpochMilli(item.getBeginTime())
                    .atZone(ZoneOffset.of("+8")).toLocalDateTime());
            couponsVO.setEndTime(Instant.ofEpochMilli(item.getEndTime())
                    .atZone(ZoneOffset.of("+8")).toLocalDateTime());
            return couponsVO;
        }).collect(Collectors.toList());
        return couponsVOList;
    }
}
