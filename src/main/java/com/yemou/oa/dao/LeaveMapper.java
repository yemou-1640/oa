package com.yemou.oa.dao;

import com.yemou.oa.pojo.Leave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {
    //根据状态统计请假申请数量
    int countByStatus(@Param("lstatus") String lstatus);
    
    List<Leave> findByUserNameAndStatus(@Param("realName") String realName, @Param("lstatus") String lstatus);
}
