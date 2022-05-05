package com.diplom.marketplace.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;

/**
 * PostImage
 *
 * @author Sainjargal Ishdorj
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"createdDate", "modifiedDate", "post"})
@Entity
@Table(name = "POST_IMAGES")
public class PostImage {

    @Id
    @SequenceGenerator(name = "postImageSeq", sequenceName = "POST_IMAGE_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "postImageSeq")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IMAGE", nullable = false)
    private String image;

    @Column(name = "IS_POSTER", nullable = false)
    private boolean isPoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(name = "POST_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Post post;

}
