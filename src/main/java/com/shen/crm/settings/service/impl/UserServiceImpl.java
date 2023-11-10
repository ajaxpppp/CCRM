package com.shen.crm.settings.service.impl;

import com.shen.crm.settings.domain.User;
import com.shen.crm.settings.mapper.UserMapper;
import com.shen.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public int saveNewUser(User user) {
        return userMapper.insertNewUser(user);
    }

    @Override
    public int changeUserPassword(String id, String loginPwd) {
        return userMapper.updateEditByPrimaryKey(id, loginPwd);
    }


}
