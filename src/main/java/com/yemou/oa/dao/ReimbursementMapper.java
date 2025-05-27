package com.yemou.oa.dao;

import com.yemou.oa.pojo.Reimbursement;
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
public interface ReimbursementMapper extends BaseMapper<Reimbursement> {
    //根据状态统计报销申请数量
    //@param rstatus 状态（0-待审批，1-已通过，2-已驳回）
    int countByStatus(@Param("rstatus") String rstatus);
}
