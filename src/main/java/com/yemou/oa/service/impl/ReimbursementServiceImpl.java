package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yemou.oa.dao.BusinessMapper;
import com.yemou.oa.dao.UserMapper;
import com.yemou.oa.pojo.Business;
import com.yemou.oa.pojo.Leave;
import com.yemou.oa.pojo.Reimbursement;
import com.yemou.oa.dao.ReimbursementMapper;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.ReimbursementService;
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
public class ReimbursementServiceImpl extends ServiceImpl<ReimbursementMapper, Reimbursement> implements ReimbursementService {
    @Autowired
    private ReimbursementMapper reimbursementMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public PageResult<Reimbursement> getListByPage(Integer uid, Integer currentPage, Integer pageSize){
        //创建page<Leave>对象，封装分页条件
        Page<Reimbursement> page = new Page<>(currentPage, pageSize);
        //创建querywrapper
        QueryWrapper<Reimbursement> queryWrapper = new QueryWrapper<>();
        if (uid!=null){//执行模糊查询
            queryWrapper.like("uid",uid);
        }
        //执行分页查询
        //如果querywrapper=null->普通分页查询，！=null->模糊查询
        List<Reimbursement> list = reimbursementMapper.selectList(page,queryWrapper);
        //查询总记录数
        Long count = reimbursementMapper.selectCount(queryWrapper);
        int total=count.intValue();
        //创建pageresult<Business>对象，
        PageResult<Reimbursement> pageResult = new PageResult<>(total,list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");

        return pageResult;
    }

    //获取待审批的请假列表
    @Override
    public PageResult<Reimbursement> getPendingList(String realName, Integer current, Integer pageSize){
        Page<Reimbursement> page = new Page<>(current, pageSize);
        QueryWrapper<Reimbursement> queryWrapper = new QueryWrapper<>();
        // 只查询状态为"审核中"的记录
        queryWrapper.eq("rstatus", "审核中");
        //模糊查询
        if (realName!=null){
            queryWrapper.exists("SELECT 1 FROM o_user u WHERE u.uid = o_reimbursement.uid AND u.realName LIKE '%" + realName + "%'");
        }
        List<Reimbursement> list = reimbursementMapper.selectList(page,queryWrapper);
        for(Reimbursement reimbursement:list){
            Integer uid = reimbursement.getUid();
            User user = userMapper.selectById(uid);
            reimbursement.setUser(user);
        }
        Long count = reimbursementMapper.selectCount(queryWrapper);
        int total=count.intValue();
        PageResult<Reimbursement> pageResult = new PageResult<>(total,list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        return pageResult;
    }

    @Override
    public PageResult<Reimbursement> getCompletedList(String realName, Integer current, Integer pageSize) {
        Page<Reimbursement> page = new Page<>(current, pageSize);
        QueryWrapper<Reimbursement> queryWrapper = new QueryWrapper<>();
        
        // 添加已完成状态的条件（已通过或已拒绝）
        queryWrapper.and(wrapper -> wrapper
            .eq("rstatus", "通过")
            .or()
            .eq("rstatus", "驳回")
        );
        
        // 如果提供了姓名，添加姓名查询条件
        if (realName != null && !realName.isEmpty()) {
            queryWrapper.exists("SELECT 1 FROM o_user u WHERE u.uid = o_reimbursement.uid AND u.realName LIKE '%" + realName + "%'");
        }
        
        // 按审批时间降序排序
        queryWrapper.orderByDesc("approvalTime");
        
        List<Reimbursement> list = reimbursementMapper.selectList(page, queryWrapper);
        for(Reimbursement reimbursement:list){
            Integer uid = reimbursement.getUid();
            User user = userMapper.selectById(uid);
            reimbursement.setUser(user);
        }
        Long count = reimbursementMapper.selectCount(queryWrapper);
        
        PageResult<Reimbursement> pageResult = new PageResult<>(count.intValue(), list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        
        return pageResult;
    }
}
