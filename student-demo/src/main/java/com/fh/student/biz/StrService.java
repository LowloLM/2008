package com.fh.student.biz;

import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import com.fh.student.mapper.IStrMapper;
import com.fh.student.param.StudentParam;
import com.fh.student.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("strService")
@Transactional(rollbackFor = Exception.class)
public class StrService  implements IStrService{

    @Autowired
    private IStrMapper strMapper;

    @Override
    public ServerResponse addStu(Student student) {
        strMapper.insert(student);
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public DataTableResult queryStu(StudentParam studentParam) {
       Long count =strMapper.findListCount(studentParam);
        List<Student> studentList=strMapper.findPageList(studentParam);

        return new DataTableResult(studentParam.getDraw(),count,count,studentList);
    }

    @Override
    public ServerResponse delStu(Long id) {
        strMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse getById(Long id) {
        Student studentId = strMapper.selectById(id);
        return ServerResponse.success(studentId);
    }

    @Override
    public ServerResponse update(Student student) {
        strMapper.updateById(student);
        return ServerResponse.success();
    }
}
