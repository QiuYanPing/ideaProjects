package com.qyp.chat.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponsVO {
    private Integer couponsId;
    private String name;
    private Integer cost;
    private Integer count;
    private LocalDateTime outdateTime;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
