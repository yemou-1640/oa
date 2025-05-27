package com.yemou.oa.service.impl;

import com.yemou.oa.dao.BusinessMapper;
import com.yemou.oa.dao.LeaveMapper;
import com.yemou.oa.dao.ReimbursementMapper;
import com.yemou.oa.service.PendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingServiceImpl implements PendingService {
    
    @Autowired
    private LeaveMapper leaveMapper;
    
    @Autowired
    private BusinessMapper businessMapper;
    
    @Autowired
    private ReimbursementMapper reimbursementMapper;
    
    @Override
    public int getPendingLeaveCount() {
        return leaveMapper.countByStatus("审核中");
    }
    
    @Override
    public int getPendingBusinessCount() {
        return businessMapper.countByStatus("审核中");
    }
    
    @Override
    public int getPendingReimbursementCount() {
        return reimbursementMapper.countByStatus("审核中");
    }
} 