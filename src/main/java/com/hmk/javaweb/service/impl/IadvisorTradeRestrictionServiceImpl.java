package com.hmk.javaweb.service.impl;

import com.hmk.javaweb.entity.IadvisorTradeRestriction;
import com.hmk.javaweb.dao.IadvisorTradeRestrictionDao;
import com.hmk.javaweb.service.IadvisorTradeRestrictionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交易限制表(IadvisorTradeRestriction)表服务实现类
 *
 * @author makejava
 * @since 2019-07-10 11:05:00
 */
@Service("iadvisorTradeRestrictionService")
public class IadvisorTradeRestrictionServiceImpl implements IadvisorTradeRestrictionService {
    @Resource
    private IadvisorTradeRestrictionDao iadvisorTradeRestrictionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public IadvisorTradeRestriction queryById(Integer id) {
        return this.iadvisorTradeRestrictionDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<IadvisorTradeRestriction> queryAllByLimit(int offset, int limit) {
        return this.iadvisorTradeRestrictionDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 实例对象
     */
    @Override
    public IadvisorTradeRestriction insert(IadvisorTradeRestriction iadvisorTradeRestriction) {
        this.iadvisorTradeRestrictionDao.insert(iadvisorTradeRestriction);
        return iadvisorTradeRestriction;
    }

    /**
     * 修改数据
     *
     * @param iadvisorTradeRestriction 实例对象
     * @return 实例对象
     */
    @Override
    public IadvisorTradeRestriction update(IadvisorTradeRestriction iadvisorTradeRestriction) {
        this.iadvisorTradeRestrictionDao.update(iadvisorTradeRestriction);
        return this.queryById(iadvisorTradeRestriction.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.iadvisorTradeRestrictionDao.deleteById(id) > 0;
    }

    @Override
    public List<String> getSyl0List(String salaryno) {
        return iadvisorTradeRestrictionDao.getSyl0List(salaryno);
    }

    @Override
    public List<Double> getHsSyl0List(String salaryno) {
        return iadvisorTradeRestrictionDao.getHsSyl0List(salaryno);
    }

    @Override
    public List<Double> getcebcList(String salaryno) {
        return iadvisorTradeRestrictionDao.getcebcList(salaryno);
    }
}