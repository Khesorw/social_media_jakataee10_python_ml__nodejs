package com.app.corechat.entities;

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
@Table(name="comment_replies")
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    


    @Column(name="content_text",nullable=false)
    private String content;

    @ManyToOne(optional = true)
    @JoinColumn(name="parent_comment_id",nullable=true)
    private Comment parent;

    @OneToMany(mappedBy="parent",cascade=CascadeType.ALL)
    private List<Comment> replies;

    
}
