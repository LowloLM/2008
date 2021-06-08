package student;

import com.fh.shop.admin.mapper.student.IStudentMapper;
import com.fh.shop.admin.po.student.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-common.xml"})
public class TestStudent {

    @Autowired
    private IStudentMapper studentMapper;

    /***
     * 新增
     */
    @Test
    public void testInsert1(){
        Student student = new Student();
        student.setStuName("李明");
        student.setAge(21);
        studentMapper.insert(student);
    }

    /***
     * 修改
     */
    @Test
    public void testUpdate1(){
        Student student = new Student();
        student.setId(1L);
        student.setStuName("李清水");
        student.setAge(22);
        studentMapper.updateById(student);
    }

    /***
     * 查询
     */
    @Test
    public void testSelect1(){
        Student student = studentMapper.selectById(1);
        System.out.println(student);
    }

    /***
     * 批量删除
     */
    @Test
    public void testDelete1(){
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList.add(2L);
        studentMapper.deleteBatchIds(idList);
    }

}
