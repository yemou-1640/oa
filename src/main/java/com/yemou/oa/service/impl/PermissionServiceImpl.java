package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yemou.oa.dao.UserMapper;
import com.yemou.oa.pojo.Permission;
import com.yemou.oa.dao.PermissionMapper;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public PageResult<User> getListByPage(String pname, Integer currentPage, Integer pageSize){
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        // 如果提供了权限名称，则进行模糊查询
        if (pname != null && !pname.trim().isEmpty()) {
            queryWrapper.exists("SELECT 1 FROM o_permission p WHERE p.pid = o_user.pid AND p.pname LIKE {0}", "%" + pname + "%");
        }
        
        // 查询用户列表
        List<User> list = userMapper.selectList(queryWrapper);
        
        // 为每个用户加载对应的权限信息
        for (User user : list) {
            Integer pid = user.getPid();
            if (pid != null) {
                Permission permission = permissionMapper.selectById(pid);
                user.setPermission(permission);
            }
        }
        
        // 获取总记录数
        Long count = userMapper.selectCount(queryWrapper);
        int total = count.intValue();
        
        // 创建分页结果对象
        PageResult<User> pageResult = new PageResult<>(total, list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        return pageResult;
    }
}
