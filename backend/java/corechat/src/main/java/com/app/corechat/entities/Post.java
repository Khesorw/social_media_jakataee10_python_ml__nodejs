package com.app.corechat.entities;


import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id",nullable = false)
    private User author;


    @Column(name = "content_post")
    private String content;

    @Column(name = "post_type",nullable=false,length=20)
    private String postType;


    @Column(name="created_at")
    private OffsetDateTime createdAt;

    
    @Column(name="updated_at")
    private OffsetDateTime updateedAt;


    @OneToMany(mappedBy = "post", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<PostMedia> postMedia;


    @OneToMany(mappedBy= "post", cascade=CascadeType.ALL,orphanRemoval=true)
    private List<Comment> comments;

    public Post(User author, List<Comment> comments, String content, OffsetDateTime createdAt, Long id,
            List<PostMedia> postMedia, String postType, OffsetDateTime updateedAt) {
        this.author = author;
        this.comments = comments;
        this.content = content;
        this.createdAt = createdAt;
        this.id = id;
        this.postMedia = postMedia;
        this.postType = postType;
        this.updateedAt = updateedAt;
    }
    
    public Post() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdateedAt() {
        return updateedAt;
    }

    public void setUpdateedAt(OffsetDateTime updateedAt) {
        this.updateedAt = updateedAt;
    }

    public List<PostMedia> getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(List<PostMedia> postMedia) {
        this.postMedia = postMedia;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    
}
