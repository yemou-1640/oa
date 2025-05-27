package com.yemou.oa.service;

import com.yemou.oa.pojo.Leave;
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
public interface LeaveService extends IService<Leave> {
    //获取待审批的请假列表
    PageResult<Leave> getPendingList(String realName,Integer current,Integer pageSize);

    //根据uid和当前页，页面容量进行分页查询和模糊查询
    public PageResult<Leave> getListByPage(Integer uid, Integer currentPage, Integer pageSize);
    //获取已办审批请假列表
    PageResult<Leave> getCompletedList(String realName, Integer current, Integer pageSize);

}
