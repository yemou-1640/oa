package com.yemou.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yemou.oa.dao.DepartmentMapper;
import com.yemou.oa.pojo.Department;
import com.yemou.oa.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper,Department> implements DepartmentService {
}
