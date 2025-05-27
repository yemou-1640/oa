package com.yemou.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yemou.oa.pojo.Attendance;
import com.yemou.oa.utils.PageResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */

public interface AttendanceService extends IService<Attendance> {
    /**
     * 获取用户今天的所有考勤记录
     * @param uid 用户ID
     * @return 考勤记录列表
     */
    List<Attendance> getTodayRecords(Integer uid);
    
    /**
     * 判断用户今天是否已经进行了某种类型的打卡
     * @param uid 用户ID
     * @param type 打卡类型（如"签到"、"签退"）
     * @return 是否已打卡
     */
    boolean hasClockToday(Integer uid, String type);


    PageResult<Attendance> getListByPage(String realName, Integer current, Integer pageSize);
}
