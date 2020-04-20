package com.hmk.javaweb.controller;

import com.hmk.javaweb.constant.ReturnMessage;
import com.hmk.javaweb.entity.IadvisorTradeRestriction;
import com.hmk.javaweb.service.IadvisorTradeRestrictionService;
import com.hmk.javaweb.utils.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * 交易限制表(IadvisorTradeRestriction)表控制层
 *
 * @author makejava
 * @since 2019-07-10 11:05:01
 */
@RestController
@RequestMapping("/iadvisor")
public class IadvisorTradeRestrictionController {

    private static final Logger logger = LoggerFactory.getLogger(IadvisorTradeRestrictionController.class);
    /**
     * 服务对象
     */
    @Resource
    private IadvisorTradeRestrictionService iadvisorTradeRestrictionService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @RequestMapping("/selectOne")
    public ReturnMessage selectOneMethod(Integer id) {
        ReturnMessage returnMessage = new ReturnMessage();
        try {

            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            System.out.println(method);


            IadvisorTradeRestriction dto = iadvisorTradeRestrictionService.queryById(id);
//            System.out.println(dto.getStockcode());
            returnMessage.setFlag(true);
            returnMessage.setMsg("查询成功");
            returnMessage.setData(dto);
        } catch (Exception e) {
            returnMessage.setFlag(true);
            returnMessage.setMsg("查询异常");
            e.printStackTrace();
        }

        return returnMessage;
    }

    @RequestMapping("/caculateSharp_rate")
    public Object caculateSharp_rate(String salaryno){
        BigDecimal yearNoRisk = new BigDecimal(0.03);
        BigDecimal dayForYear = new BigDecimal(365);
        BigDecimal dayNoRisk = yearNoRisk.divide(dayForYear, 20, RoundingMode.HALF_UP);
        BigDecimal zero = new BigDecimal(0);
        List<String> syl0ListAll = iadvisorTradeRestrictionService.getSyl0List(salaryno);
            BigDecimal avgSyl0 = NumberUtil.getAvgFromSList(syl0ListAll);
        System.out.println("avgSyl0:"+avgSyl0);
            if (avgSyl0.compareTo(zero) < 0) {
                logger.info("日平均收益率为负数，返回零");
                return new BigDecimal(0);
            }
            // 利用工具类求方差之后开根号求标准差 计算列表所有数据的标准差
            BigDecimal variance = NumberUtil.getVarianceFromStrList(syl0ListAll); // 方差
            System.out.println("variance:"+variance);
            BigDecimal standardDeviation = NumberUtil.rooting(variance); // 分母 标准差
            //当今60个交易日的sly0 全部为0时,standardDeviation 可能为null,做分母时会报错
            System.out.println("standardDeviation:"+standardDeviation);
            // 计算夏普比率
            BigDecimal fenzi = avgSyl0.subtract(dayNoRisk); // 分子
            BigDecimal sharp = new BigDecimal(0);
            if(standardDeviation != null){
                 sharp = fenzi.divide(standardDeviation, 10, RoundingMode.HALF_UP);
            }
            return sharp;
    }

    @RequestMapping("/caculateInfo_rate")
    public BigDecimal caculateInfo_rate(String salaryno) {
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        BigDecimal zero = new BigDecimal(0);
        // 资金账号的收益率列表
        List<String> syl0List = iadvisorTradeRestrictionService.getSyl0List(salaryno);
        if (syl0List == null || syl0List.size() == 0) {
            logger.info("组合收益率列表长度不足,返回信息比率为0");
            return new BigDecimal(0);
        }
        BigDecimal avgForSyl0 = NumberUtil.getAvgFromSList(syl0List);
        if (avgForSyl0.compareTo(zero) < 0) {
            logger.info("组合收益率平均为负,返回信息比率为0");
            return new BigDecimal(0);
        }
        // 同期hs300的收益率列表
        List<Double> hsSyl0List = iadvisorTradeRestrictionService.getHsSyl0List(salaryno);
        if (hsSyl0List == null || hsSyl0List.size() == 0) {
            logger.info("同期hs收益率列表长度不足,返回信息比率为0");
            return new BigDecimal(0);
        }
        BigDecimal avgForHsSyl0 = NumberUtil.getAvgFromDList(hsSyl0List);
        BigDecimal subtract = avgForSyl0.subtract(avgForHsSyl0, mc);
        // 同期的hs300超额成本
        List<Double> cebcAll = iadvisorTradeRestrictionService.getcebcList(salaryno);
        if (cebcAll != null && cebcAll.size() > 2) {
            // 超额报酬的标准差
            BigDecimal cebcVariance = NumberUtil.getVarianceFromDoubleList(cebcAll);
            System.out.println("cebcVariance:  "+cebcVariance);
            BigDecimal standardDeviation = NumberUtil.rooting(cebcVariance);
            System.out.println("standardDeviation:"+standardDeviation);
            BigDecimal infoRate = new BigDecimal(0);
            if(standardDeviation != null){
                infoRate = subtract.divide(standardDeviation, 10, RoundingMode.HALF_UP);
            }
            return infoRate;
        } else {
            logger.info("组合超额报酬列表长度不足,返回信息比率为0");
            return new BigDecimal(0);
        }
    }

}