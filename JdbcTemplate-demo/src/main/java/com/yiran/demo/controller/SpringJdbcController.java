package com.yiran.demo.controller;

import com.yiran.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class SpringJdbcController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpringJdbcController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    @ResponseBody
    public List<User> queryUsers() {
        // 查询所有用户
        String sql = "select * from t_user";
        // new Object[]{} --> Object类型的的数组定义构建初始化一体的写法，{} 为数组内容，这里为空，下面的数组内容为 id
        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(User.class));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        // 根据主键id查询
        String sql = "select * from t_user where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
    }

    @DeleteMapping("/{id}")
    public int delUser(@PathVariable Integer id) {
        // 根据主键id删除用户信息
        String sql = "delete from t_user where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @PostMapping
    public int addUser(@RequestBody User user) {
        // 添加用户
        String sql = "insert into t_user(username, password) values(?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    @PutMapping("/{id}")
    public int editUser(@PathVariable Integer id, @RequestBody User user) {
        // 根据主键id修改用户信息
        String sql = "update t_user set username = ? ,password = ? where id = ?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), id);
    }
}
