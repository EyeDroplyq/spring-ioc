package com.lyq.springioc.controller;

import com.lyq.springioc.anno.Be;
import com.lyq.springioc.anno.Di;
import com.lyq.springioc.service.UserService;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
@Be

public class UserController {
    @Di
    private UserService userService;
    public void add(){
        System.out.println("controller....");
        userService.add();
    }

}
