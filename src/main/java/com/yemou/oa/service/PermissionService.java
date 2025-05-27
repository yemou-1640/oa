package com.yemou.oa.service;

import com.yemou.oa.pojo.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yemou.oa.pojo.User;
import com.yemou.oa.utils.PageResult;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
public interface PermissionService extends IService<Permission> {
    public PageResult<User> getListByPage(String pname, Integer currentPage, Integer pageSize);

}
