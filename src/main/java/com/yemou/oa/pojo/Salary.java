package com.yemou.oa.pojo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@TableName("o_salary")
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sid", type = IdType.AUTO)
    private Integer sid;

    @TableField(value = "baseSalary")
    private BigDecimal baseSalary;

    private BigDecimal bonus;

    private BigDecimal deduction;

    @TableField(value = "paymentDate")
    private Date paymentDate;

    private String remarks;

    @TableField(value = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date created;

    @TableField(value = "updated")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updated;

    private Integer uid;

    @TableField(exist = false)
    private User user;
}
