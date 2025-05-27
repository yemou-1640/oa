package com.yemou.oa.service;

import com.yemou.oa.pojo.Business;
import com.yemou.oa.pojo.Leave;
import com.yemou.oa.pojo.Reimbursement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yemou.oa.utils.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
public interface ReimbursementService extends IService<Reimbursement> {
    public PageResult<Reimbursement> getListByPage(Integer uid, Integer currentPage, Integer pageSize);

    //获取待审批的请假列表
    PageResult<Reimbursement> getPendingList(String realName, Integer current, Integer pageSize);

    PageResult<Reimbursement> getCompletedList(String realName, Integer current, Integer pageSize);
}
