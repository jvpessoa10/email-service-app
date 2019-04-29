package com.joao.emailservice.models;

public class Email {

    private String title;
    private String body;


    public Email(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" +
                    "title:" + title + "," +
                    "body:" + body  +
                "}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
