package com.yemou.oa.service;

public interface PendingService {
    /**
     * 获取待审批的请假申请数量
     */
    int getPendingLeaveCount();
    
    /**
     * 获取待审批的出差申请数量
     */
    int getPendingBusinessCount();
    
    /**
     * 获取待审批的报销申请数量
     */
    int getPendingReimbursementCount();
} 