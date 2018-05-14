package com.objectcomputing.webflux;

public class JsonResponse {
    private String message;
    private boolean happy;
    private int age;

    public JsonResponse() { }

    public JsonResponse(String message, boolean happy, int age) {
        this.message = message;
        this.happy = happy;
        this.age = age;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHappy() {
        return happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
