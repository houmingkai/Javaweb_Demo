package com.hmk.javaweb.constant;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnMessage implements Serializable {
    private static final long serialVersionUID = -3683245148543929406L;

    private boolean           flag;                                   // true：成功 false:失败
    private String            msg              = "";                  // 错误消息
    private Object            data;

    public ReturnMessage() {

    }

    public ReturnMessage(boolean flag) {
        this.flag = flag;
    }

    public ReturnMessage(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public ReturnMessage(boolean flag, String msg,Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }
}
