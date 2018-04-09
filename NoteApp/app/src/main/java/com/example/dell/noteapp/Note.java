package com.example.dell.noteapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dell on 3/30/2018.
 */

public class Note implements Serializable {
    private int id;
    private String image;
    private String content;
    private Date time;

    public Note() {
    }

    public Note(String image, String content, Date time) {
        this.image = image;
        this.content = content;
        this.time = time;
    }


    public Note(String content, Date time) {
        this.content = content;
        this.time = time;
    }

    public Note(int id, String image, String content, Date time) {
        this.id = id;
        this.image = image;
        this.content = content;
        this.time = time;
    }

    public Note(String image, String content) {
        this.image = image;
        this.content = content;
    }

    public Note(int id, String image, String content) {
        this.id = id;
        this.image = image;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
