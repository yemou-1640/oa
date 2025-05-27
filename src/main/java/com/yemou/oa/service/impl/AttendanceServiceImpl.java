package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yemou.oa.dao.UserMapper;
import com.yemou.oa.dao.AttendanceMapper;
import com.yemou.oa.pojo.Attendance;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.AttendanceService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {
    
    @Autowired
    private AttendanceMapper attendanceMapper;
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Attendance> getTodayRecords(Integer uid) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(today);
        
        queryWrapper.eq("uid", uid)
                .apply("DATE_FORMAT(date, '%Y-%m-%d') = {0}", todayStr)
                .orderByAsc("time");
        return list(queryWrapper);
    }

    
    @Override
    public boolean hasClockToday(Integer uid, String type) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(today);
        
        queryWrapper.eq("uid", uid)
                .eq("type", type)
                .apply("DATE_FORMAT(date, '%Y-%m-%d') = {0}", todayStr);
        
        return count(queryWrapper) > 0;
    }


    @Override
    public PageResult<Attendance> getListByPage(String realName, Integer current, Integer pageSize) {
        // 创建分页对象
        Page<Attendance> page = new Page<>(current, pageSize);
        // 构建查询条件
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        if (realName != null && !realName.isEmpty()) {
            queryWrapper.exists("SELECT 1 FROM o_user u WHERE u.uid = o_attendance.uid AND u.realName LIKE '%" + realName + "%'");
        }
        
        // 按时间倒序排序
        queryWrapper.orderByDesc("time");
        List<Attendance> list = attendanceMapper.selectList(page, queryWrapper);
        for(Attendance attendance:list){
            Integer uid = attendance.getUid();
            User user = userMapper.selectById(uid);
            attendance.setUser(user);
        }
        Long count = attendanceMapper.selectCount(queryWrapper);
        int total=count.intValue();
        // 执行分页查询
        PageResult<Attendance> pageResult=new PageResult<>(total,list);
        pageResult.setCode(0);
        pageResult.setMsg("分页查询成功");
        
        // 返回分页结果
        return pageResult;
    }
}
