package com.hmk.javaweb.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class SubOrderDto implements Serializable,Cloneable {
    private static final long serialVersionUID = -5694235898149242467L;

    private Integer id;
    private String order_code;
    private Integer order_status;  //订单状态:1,待支付、2,已支付、3,退订中、4,已退订、5,已关闭
    private Integer order_type;
    private String product_name;
    private String server_start;
    private String server_end;
    private Integer pay_type;
    private String app_sno;
    private String apply_date;
    private String sname;
    private Integer order_count;
    private Integer pay_status; //接口返回的，订单的支付状态
    private String zjzh;
    private Long phone_num;
    private Integer product_id;
    private Integer expiryDate;  //有效期(月)
    private Integer first_buy;   //是否首次购买(0,首次购买;1,非首次购买)

    private String unsub_date;  //退订申请时间
    private Integer unsub_audit_status; //退订审核状态
    private String inputtime;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
