package com.example.task_1;

public class User {
    private String name;
    private int age;
    private boolean premium;

    public User() {
        // Empty constructor required for Firebase
    }

    public User(String name, int age, boolean premium) {
        this.name = name;
        this.age = age;
        this.premium = premium;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isPremium() {
        return premium;
    }
}

