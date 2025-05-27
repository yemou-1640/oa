package com.yemou.oa.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("o_business")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "tid", type = IdType.AUTO)
    private Integer tid;

    @TableField(value = "startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startDate;

    @TableField(value = "endDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endDate;

    private String destination;

    private String reason;

    private String bstatus;

    @TableField(value = "approvedByUserId")
    private Integer approvedByUserId;

    @TableField(value = "approvalTime")
    private Date approvalTime;

    @TableField(value = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date created;

    @TableField(value = "updated")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updated;

    private Integer uid;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private User approver;
}
