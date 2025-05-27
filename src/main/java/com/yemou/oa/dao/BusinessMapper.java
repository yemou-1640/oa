package com.yemou.oa.dao;

import com.yemou.oa.pojo.Business;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Mapper
public interface BusinessMapper extends BaseMapper<Business> {
    //根据状态统计出差申请数量
    // @param bstatus 状态（0-待审批，1-已通过，2-已驳回）
    int countByStatus(@Param("bstatus") String bstatus);
}
