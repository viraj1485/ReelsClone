package com.example.reels.Model;

public class PostModel {
    String Video;
    String description;
    String postuid;

    public PostModel(String Video, String description, String postuid) {
        this.Video = Video;
        this.description = description;
        this.postuid = postuid;
    }

    public PostModel()
    {

    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostuid() {
        return postuid;
    }

    public void setPostuid(String postuid) {
        this.postuid = postuid;
    }
}
