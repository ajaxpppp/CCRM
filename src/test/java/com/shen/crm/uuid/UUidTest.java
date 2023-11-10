package com.shen.crm.uuid;

import java.util.UUID;

public class UUidTest {
    public static void main(String[] args) {
        String string = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(string);
    }
}
