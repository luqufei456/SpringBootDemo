package com.yiran.demo.demoo;

public class Yiran extends Cat {

    public int age = 22;

    @Override
    public void eat() {
        System.out.println(age);
    }

    public static void main(String[] args) {
        Cat cat = new Yiran();
        cat.eat();
        System.out.println(cat.age);
    }
}
