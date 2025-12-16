package com.app.corechat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="post_media")
public class PostMedia {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="post_id",nullable=false)
    private Post post;


    @Column(name="media_url",nullable=false)
    private String mediaUrl;

    @Column(name="media_type",nullable=false)
    private String mediaType;

    private Integer width;
    private Integer height;
    private Integer duration;


    public PostMedia() {

    }
    
    
    public PostMedia(Integer height, Integer duration, Long id, String mediaType, String mediaUrl, Post post, Integer width) {
        this.height = height;
        this.duration = duration;
        this.id = id;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.post = post;
        this.width = width;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getheight() {
        return height;
    }

    public void setheight(Integer height) {
        this.height = height;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    


    
}
