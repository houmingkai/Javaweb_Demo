package com.hmk.javaweb.dao;

import com.hmk.javaweb.entity.IadvisorTradeRestriction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 交易限制表(IadvisorTradeRestriction)表数据库访问层
 *
 * @author makejava
 * @since 2019-07-10 11:04:59
 */
@Mapper
public interface IadvisorTradeRestrictionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    IadvisorTradeRestriction queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<IadvisorTradeRestriction> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 对象列表
     */
    List<IadvisorTradeRestriction> queryAll(IadvisorTradeRestriction iadvisorTradeRestriction);

    /**
     * 新增数据
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 影响行数
     */
    int insert(IadvisorTradeRestriction iadvisorTradeRestriction);

    /**
     * 修改数据
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 影响行数
     */
    int update(IadvisorTradeRestriction iadvisorTradeRestriction);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    @Select("SELECT syl0 FROM t_simulation_total_asserts where simulation_acc = #{salaryno} ORDER BY id DESC limit 0,60")
    List<String> getSyl0List(@Param("salaryno") String salaryno);

    @Select("select hs.syl0  from t_hs300 hs INNER JOIN t_simulation_total_asserts ta on TO_DAYS(DATE_SUB(hs.inputtime,INTERVAL 1 day))=TO_DAYS(ta.inputtime) and ta.simulation_acc= #{salaryno} limit 0,60")
    List<Double> getHsSyl0List(@Param("salaryno")String salaryno);

    @Select("SELECT cebc FROM t_simulation_total_asserts where simulation_acc =  #{salaryno} ORDER BY id DESC limit 0,60")
    List<Double> getcebcList(@Param("salaryno")String salaryno);
}