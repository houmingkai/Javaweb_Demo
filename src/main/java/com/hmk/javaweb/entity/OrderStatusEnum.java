package com.hmk.javaweb.entity;

/**
 *  订单状态枚举
 *
 *  订单状态:1,待支付、2,已支付、3,退订中、4,已退订、5,已关闭
 */
public enum OrderStatusEnum {

    UNPAY(1,"待支付"),
    PAY(2,"已支付"),
    UNSUB(3,"退订中"),
    AUNSub(4,"已退订"),
    CLOSE(5,"已关闭");

    private Integer code;
    private String status;

    private OrderStatusEnum(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getStatusByCode(Integer code){
        String status = "";
        for(OrderStatusEnum b:values()){
            if(b.getCode()==code){
                status = b.status;
                break;
            }
        }
        return status;
    }

    public static void main(String[] args){
        System.out.println(OrderStatusEnum.getStatusByCode(5));
    }
}
