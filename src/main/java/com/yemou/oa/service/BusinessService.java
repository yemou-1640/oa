package com.yemou.oa.service;

import com.yemou.oa.pojo.Business;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yemou.oa.pojo.Leave;
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
public interface BusinessService extends IService<Business> {
    public PageResult<Business> getListByPage(Integer uid,Integer currentPage, Integer pageSize);

    //获取待审批的请假列表
    PageResult<Business> getPendingList(String realName, Integer current, Integer pageSize);

    PageResult<Business> getCompletedList(String realName, Integer current, Integer pageSize);
}
