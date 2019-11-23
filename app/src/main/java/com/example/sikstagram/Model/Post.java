package com.example.sikstagram.Model;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String pltnumber;
    private String pltname;

    public Post(String postid, String postimage, String description, String publisher, String pltname, String pltnumber) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.pltname = pltname;
        this.pltnumber=pltnumber;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPltname() {
        return pltname;
    }

    public void setPltname(String pltname) {
        this.pltname = pltname;
    }

    public String getPltnumber() {
        return pltnumber;
    }

    public void setPltnumber(String pltnumber) {
        this.pltnumber = pltnumber;
    }
}
