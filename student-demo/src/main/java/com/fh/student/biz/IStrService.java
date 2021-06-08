package com.fh.student.biz;

import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import com.fh.student.param.StudentParam;
import com.fh.student.po.Student;

public interface IStrService {
    ServerResponse addStu(Student student);

    DataTableResult queryStu(StudentParam studentParam);

    ServerResponse delStu(Long id);

    ServerResponse getById(Long id);

    ServerResponse update(Student student);
}
