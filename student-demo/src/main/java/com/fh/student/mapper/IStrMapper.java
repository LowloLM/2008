package com.fh.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.student.param.StudentParam;
import com.fh.student.po.Student;

import java.util.List;

public interface IStrMapper extends BaseMapper<Student> {

    List<Student> findPageList(StudentParam studentParam);


    Long findListCount(StudentParam studentParam);
}
