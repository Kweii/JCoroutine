package com.jcoroutine.test;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-10
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        User user = new User("guiliehua", 28, Gender.MAN);
        System.out.println(user.getName());
    }

}
