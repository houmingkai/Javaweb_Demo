package com.hmk.javaweb.entity;

import lombok.Data;

import java.util.List;

@Data
public class StrategyDetailDto {

    private long id;
    //标题
    private String title;
    //标记
    private String label;
    //投资逻辑
    private String logic;
    //问句
    private String query;
    //回测起始时间
    private String startDate;
    //回测结束时间
    private String endDate;
    //时机
    private String opportunity;
    //持股数
    private String stockHoldCount;
    //持股周期
    private String daysForSaleStrategy;
    //仓位控制
    private String positionControl;
    //冲高的比例
    private String upperIncome;
    //回落的比例
    private String fallIncome;
    //止损的比例
    private String lowerIncome;
    //上一个交易日时间
    private String previousTransactionDate;
    //最大成功率/最大上涨概率
    private Double maxWinRate;
    //最大成功率持股周期/最大上涨概率周期
    private Integer maxWinRateDays;
    //最大年化收益
    private Double maxAnnualYield;
    //最大年化收益持股周期
    private Integer maxAnnualYieldDays;
    //最优平均涨跌幅
    private Double maxAverageIncome;
    //最优平均涨跌幅周期
    private Integer maxAverageIncomeDays;
    //历史交易次数
    private Integer totalTradeTimes;
    //最大连续无结果天数
    private Integer maxNoStockDays;
    //平均每天查找数
    private Integer averageStockNum;
    //条例数据
    private List<String> ruleList;
}
