package com.jcoroutine.test;

import com.jcoroutine.core.JCoroutine;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-15
 */
public class User {
    private String name;
    private int age;
    private Gender gender;

    public User(String name, int age, Gender gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @JCoroutine
    public String getName() {
        return name;
    }
    @JCoroutine
    public void setName(String name) {
        this.name = name;
    }
    @JCoroutine
    public int getAge() {
        return age;
    }
    @JCoroutine
    public void setAge(int age) {
        this.age = age;
    }
}
