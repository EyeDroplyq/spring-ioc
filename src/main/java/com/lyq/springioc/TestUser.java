package com.lyq.springioc;

import com.lyq.springioc.bean.AnnotationApplicationContext;
import com.lyq.springioc.controller.UserController;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
public class TestUser {
    @Test
    public void test(){
        AnnotationApplicationContext applicationContext = new AnnotationApplicationContext("com.lyq.springioc");
        UserController userController = (UserController) applicationContext.getBean(UserController.class);
        userController.add();
    }
}
