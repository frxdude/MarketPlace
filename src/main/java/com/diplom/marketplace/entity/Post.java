package com.diplom.marketplace.entity;

import com.diplom.marketplace.entity.enums.PostTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Post
 *
 * @author Sainjargal Ishdorj
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"createdDate", "modifiedDate", "password", "isActive"})
@Entity
@Table(name = "POSTS")
public class Post extends Audit {

    @Id
    @Column(nullable = false, name = "ID", length = 45, unique = true)
    private String id;

    @Column(nullable = false, name = "TITLE")
    private String title;

    @Column(nullable = false, name = "DESCRIPTION")
    private String description;

    @Column(name = "ROOMS")
    private int rooms;

    @Column(nullable = false, name = "AREA")
    private float area;

    @Column(nullable = false, name = "PRICE")
    private double price;

    @Column(nullable = false, name = "ADDRESS")
    private String address;

    @OneToMany(targetEntity = PostImage.class,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            mappedBy = "post",
            fetch = FetchType.LAZY)
    private List<PostImage> postImages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<PostTypes> postTypes;

    @PrePersist
    private void prePersist() {
        if (getId() == null)
            setId(UUID.randomUUID().toString());
    }
}
