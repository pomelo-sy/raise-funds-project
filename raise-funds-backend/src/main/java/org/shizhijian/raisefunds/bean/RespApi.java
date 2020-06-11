package org.shizhijian.raisefunds.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RespApi<T> {

    private boolean flag;

    private String message;

    private T data;

    RespApi(Boolean flag){
        this.flag = flag;
        this.data = (T) Boolean.TRUE;
    }

    public static final RespApi OK = new RespApi<Boolean>(true);
}
