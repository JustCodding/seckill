package com.jbc.seckill.service;

import com.jbc.seckill.domain.User;
import com.jbc.seckill.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserByid(int id){
        return  userMapper.selUserById(id);
    }
}
