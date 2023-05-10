package com.lyq.springioc.service.impl;

import com.lyq.springioc.anno.Be;
import com.lyq.springioc.anno.Di;
import com.lyq.springioc.dao.UserDao;
import com.lyq.springioc.service.UserService;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
@Be
public class UserServiceImpl implements UserService {
    @Di
    private UserDao userDao;
    @Override
    public void add() {
        System.out.println("service...");
        userDao.add();
    }
}
