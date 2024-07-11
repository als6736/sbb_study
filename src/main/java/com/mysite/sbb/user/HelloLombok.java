package com.mysite.sbb.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HelloLombok {
    private final String hello;
    private final int lombok;

    public static void main(String[] arg) {
        HelloLombok helloLombok = new HelloLombok("헬로",5);

        System.out.println(helloLombok.getHello());
        System.out.println(helloLombok.getLombok());
    }
}
