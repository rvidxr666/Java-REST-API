package com.maksi.learnspringboot;

public class Course {
    private final String Name;
    private final String Author;

    public Course(String Name, String Author) {
        this.Name=Name;
        this.Author = Author;
    }
    public String getName() {
        return this.Name;
    }

    public String getAuthor() {
        return this.Author;
    }

}
