package com.diplom.marketplace.dto.request.post;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * PostUpdateRequest
 *
 * @author Sainjargal Ishdorj
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostUpdateRequest {

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
}
