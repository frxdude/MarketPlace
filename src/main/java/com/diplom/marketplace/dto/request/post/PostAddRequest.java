package com.diplom.marketplace.dto.request.post;

import com.diplom.marketplace.entity.PostImage;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * PostAddRequest
 *
 * @author Sainjargal Ishdorj
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostAddRequest {

    @NotBlank(message = "{val.not.null}")
    private String title;

    @NotBlank(message = "{val.not.null}")
    private String description;

    @NotNull(message = "{val.not.null}")
    private int rooms;

    @NotNull(message = "{val.not.null}")
    private float area;

    @NotNull(message = "{val.not.null}")
    private double price;

    @NotBlank(message = "{val.not.null}")
    private String address;

    @NotEmpty(message = "{val.not.null}")
    private List<PostImage> postImages = new ArrayList<>();

    @NotNull(message = "{val.not.null}")
    private int postType;

}
