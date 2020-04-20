package com.hmk.javaweb.service;

import com.hmk.javaweb.entity.IadvisorTradeRestriction;
import java.util.List;

/**
 * 交易限制表(IadvisorTradeRestriction)表服务接口
 *
 * @author makejava
 * @since 2019-07-10 11:05:00
 */
public interface IadvisorTradeRestrictionService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    IadvisorTradeRestriction queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<IadvisorTradeRestriction> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 实例对象
     */
    IadvisorTradeRestriction insert(IadvisorTradeRestriction iadvisorTradeRestriction);

    /**
     * 修改数据
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 实例对象
     */
    IadvisorTradeRestriction update(IadvisorTradeRestriction iadvisorTradeRestriction);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<String> getSyl0List(String salaryno);

    List<Double> getHsSyl0List(String salaryno);

    List<Double> getcebcList(String salaryno);
}