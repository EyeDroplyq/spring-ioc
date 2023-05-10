package com.lyq.springioc.dao.impl;

import com.lyq.springioc.anno.Be;
import com.lyq.springioc.dao.UserDao;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
@Be
public class UserDaoImpl implements UserDao {
    @Override
    public void add() {
        System.out.println("dao.....");
    }
}
