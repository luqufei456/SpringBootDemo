package com.yiran.demo.mapper;

import com.yiran.demo.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

// t_student 操作，继承 BaseMapper<T> 就可以了，是不是有点类似 JpaRepository
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 根据用户名统计（TODO 假设它是一个很复杂的SQL）
     *
     * @param stuName 用户名
     * @return 统计结果
     */
    int countByStuName(String stuName);
}
