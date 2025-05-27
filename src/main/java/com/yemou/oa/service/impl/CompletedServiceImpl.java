package com.yemou.oa.service.impl;

import com.yemou.oa.dao.BusinessMapper;
import com.yemou.oa.dao.LeaveMapper;
import com.yemou.oa.dao.ReimbursementMapper;
import com.yemou.oa.service.CompletedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompletedServiceImpl implements CompletedService {
    
    @Autowired
    private LeaveMapper leaveMapper;
    
    @Autowired
    private BusinessMapper businessMapper;
    
    @Autowired
    private ReimbursementMapper reimbursementMapper;
    
    @Override
    public int getCompletedLeaveCount() {
        return leaveMapper.countByStatus("通过") + leaveMapper.countByStatus("驳回");
    }
    
    @Override
    public int getCompletedBusinessCount() {
        return businessMapper.countByStatus("通过") + businessMapper.countByStatus("驳回");
    }
    
    @Override
    public int getCompletedReimbursementCount() {
        return reimbursementMapper.countByStatus("通过") + reimbursementMapper.countByStatus("驳回");
    }
} 