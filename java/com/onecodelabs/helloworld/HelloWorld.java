package com.onecodelabs.helloworld;

import models.UserOuterClass.User;

public class HelloWorld {

    public static String greet() {
        return "Hello world!";
    }

    public static void main(String[] args) {
        User user = User.newBuilder()
                .setName("nacho")
                .setAge(15)
                .setMale(true)
                .build();
        System.out.println(user);
        System.out.println(greet());
    }
}
