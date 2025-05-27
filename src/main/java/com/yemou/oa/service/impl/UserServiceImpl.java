package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yemou.oa.dao.PermissionMapper;
import com.yemou.oa.pojo.Leave;
import com.yemou.oa.pojo.Permission;
import com.yemou.oa.pojo.User;
import com.yemou.oa.dao.UserMapper;
import com.yemou.oa.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yemou.oa.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User login(User user) {
        //创建querymapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //封装条件
        queryWrapper.eq("name",user.getName());
        queryWrapper.eq("password",user.getPassword());
        queryWrapper.eq("pid",user.getPid());
        //执行查询
        User user2=userMapper.selectOne(queryWrapper);
        return user2;
    }

    @Override
    public PageResult<User> getListByPage(String name, Integer currentPage, Integer pageSize){
        //创建page<User>对象，封装分页条件
        Page<User> page = new Page<>(currentPage, pageSize);
        //创建querywrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (name!=null){//执行模糊查询
            queryWrapper.like("name",name);
        }
        //执行分页查询
        //如果querywrapper=null->普通分页查询，！=null->模糊查询
        List<User> list = userMapper.selectList(page,queryWrapper);
        //查询总记录数
        Long count = userMapper.selectCount(queryWrapper);
        int total=count.intValue();
        //创建pageresult<User>对象，
        PageResult<User> pageResult = new PageResult<>(total,list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");

        return pageResult;
    }

    @Override
    public List<Integer> getUserIdsByRealName(String realName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getRealName, realName);
        List<User> users = this.list(queryWrapper);
        return users.stream()
                .map(User::getUid)
                .collect(Collectors.toList());
    }
}
