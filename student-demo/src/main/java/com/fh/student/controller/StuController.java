package com.fh.student.controller;

import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import com.fh.student.biz.IStrService;
import com.fh.student.param.StudentParam;
import com.fh.student.po.Student;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
public class StuController {

    @Resource(name = "strService")
    private IStrService strService;

    @PostMapping("/students")
    public ServerResponse addStu(@RequestBody Student student){
        return  strService.addStu(student);
    }

    @GetMapping("/students")
    public DataTableResult queryStu(StudentParam studentParam){
        return strService. queryStu( studentParam);
    }
    @DeleteMapping("/students")
    public ServerResponse delStu(Long id){
        return strService.delStu(id);
    }
    @PostMapping("/student")
    public ServerResponse getById(Long id){
        return strService.getById(id);
    }
    @PutMapping("/students")
    public ServerResponse update(@RequestBody Student student){
        return strService.update( student);
    }
}
