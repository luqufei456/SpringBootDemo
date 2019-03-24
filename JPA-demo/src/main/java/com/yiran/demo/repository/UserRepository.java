package com.yiran.demo.repository;

import com.yiran.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 查询结果
     */
    List<User> findAllByUsername(String username);
}
