package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yemou.oa.dao.LeaveMapper;
import com.yemou.oa.dao.UserMapper;
import com.yemou.oa.pojo.Leave;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.LeaveService;
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
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {
    @Autowired
    LeaveMapper leaveMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<Leave> getListByPage(Integer uid, Integer currentPage, Integer pageSize) {
        //创建page<Leave>对象，封装分页条件
        Page<Leave> page = new Page<>(currentPage, pageSize);
        //创建querywrapper
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        if (uid!=null){//执行模糊查询
            queryWrapper.like("uid",uid);
        }
        //执行分页查询
        //如果querywrapper=null->普通分页查询，！=null->模糊查询
        List<Leave> list = leaveMapper.selectList(page,queryWrapper);
        //查询总记录数
        Long count = leaveMapper.selectCount(queryWrapper);
        int total=count.intValue();
        //创建pageresult<Leave>对象，
        PageResult<Leave> pageResultObj = new PageResult<>(total,list);
        pageResultObj.setCode(0);
        pageResultObj.setMsg("分页查询成功");

        return pageResultObj;
    }

    @Override
    //获取待审批的请假列表
    public PageResult<Leave> getPendingList(String realName,Integer current,Integer pageSize){
        Page<Leave> page = new Page<>(current,pageSize);
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        // 只查询状态为"审核中"的记录
        queryWrapper.eq("lstatus", "审核中");
        if (realName!=null){
            queryWrapper.exists("SELECT 1 FROM o_user u WHERE u.uid = o_leave.uid AND u.realName LIKE '%" + realName + "%'");
        }
        List<Leave> list = leaveMapper.selectList(page,queryWrapper);
        for(Leave leave:list){
            Integer uid=leave.getUid();
            User user=userMapper.selectById(uid);
            leave.setUser(user);
        }
        Long count = leaveMapper.selectCount(queryWrapper);
        int total=count.intValue();
        PageResult<Leave> pageResult = new PageResult<>(total,list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        return pageResult;
    }

    @Override
    public PageResult<Leave> getCompletedList(String realName, Integer current, Integer pageSize) {
        Page<Leave> page = new Page<>(current, pageSize);
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        
        // 添加已完成状态的条件（通过或驳回）
        queryWrapper.and(wrapper -> wrapper
            .eq("lstatus", "通过")
            .or()
            .eq("lstatus", "驳回")
        );
        
        // 如果提供了姓名，添加姓名查询条件
        if (realName != null && !realName.isEmpty()) {
            queryWrapper.exists("SELECT 1 FROM o_user u WHERE u.uid = o_leave.uid AND u.realName LIKE '%" + realName + "%'");
        }
        
        // 按审批时间降序排序
        queryWrapper.orderByDesc("approvalTime");
        
        List<Leave> list = leaveMapper.selectList(page, queryWrapper);
        for(Leave leave:list){
            Integer uid=leave.getUid();
            User user=userMapper.selectById(uid);
            leave.setUser(user);
        }
        Long count = leaveMapper.selectCount(queryWrapper);
        
        PageResult<Leave> pageResult = new PageResult<>(count.intValue(), list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        
        return pageResult;
    }

}
