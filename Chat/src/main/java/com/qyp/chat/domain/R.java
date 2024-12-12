package com.qyp.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    private Integer code;
    private String msg;
    private Object data;


    public static R success(Object data){
        return new R(200,"sucess",data);
    }

    public static R error(String msg){
        return new R(500,msg,"");
    }
}
