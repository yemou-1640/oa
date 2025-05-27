package com.yemou.oa.service;

import com.yemou.oa.pojo.Leave;
import com.yemou.oa.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yemou.oa.utils.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
public interface UserService extends IService<User> {
    //根据用户名，密码，角色查询指定用户
    public User login(User user);
    //根据name和当前页，页面容量进行分页查询和模糊查询
    public PageResult<User> getListByPage(String name, Integer currentPage, Integer pageSize);

    /**
     * 根据真实姓名查询用户ID列表
     * @param realName 真实姓名
     * @return 用户ID列表
     */
    List<Integer> getUserIdsByRealName(String realName);
}
