package com.yemou.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yemou.oa.pojo.Salary;
import com.yemou.oa.utils.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
public interface SalaryService extends IService<Salary> {
    PageResult<Salary> getListByPage(String realName, Integer current,Integer pageSize);
}
