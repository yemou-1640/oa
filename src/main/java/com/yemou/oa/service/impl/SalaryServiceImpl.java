package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yemou.oa.dao.SalaryMapper;
import com.yemou.oa.dao.UserMapper;
import com.yemou.oa.pojo.Salary;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.SalaryService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {
    @Autowired
    private SalaryMapper salaryMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<Salary> getListByPage(String realName,Integer current,Integer pageSize) {
        // 创建分页对象
        Page<Salary> page = new Page<>(current, pageSize);
        // 构建查询条件
        QueryWrapper<Salary> queryWrapper = new QueryWrapper<>();
        if (realName != null && !realName.isEmpty()) {
            queryWrapper.exists("SELECT 1 FROM o_user u WHERE u.uid = o_salary.uid AND u.realName LIKE '%" + realName + "%'");
        }
        
        // 执行分页查询
        List<Salary> list=salaryMapper.selectList(page,queryWrapper);
        for (Salary salary:list){
            Integer uid=salary.getUid();
            User user=userMapper.selectById(uid);
            salary.setUser(user);
        }
        Long count=salaryMapper.selectCount(queryWrapper);
        int total=count.intValue();
        PageResult<Salary> pageResult=new PageResult<>(total,list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        // 返回分页结果
        return pageResult;
    }
}
