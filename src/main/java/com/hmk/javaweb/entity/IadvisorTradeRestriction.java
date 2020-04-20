package com.hmk.javaweb.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 交易限制表(IadvisorTradeRestriction)实体类
 *
 * @author makejava
 * @since 2019-07-10 11:04:58
 */

@Data
public class IadvisorTradeRestriction implements Serializable {
    private static final long serialVersionUID = 689681186598462205L;
    //pk
    private Integer id;
    //股票代码
    private String stockcode;
    //股票名称
    private String stockname;
    //创建时间
    private String inputtime;
    //唯一索引
    private String jsonstr;

}