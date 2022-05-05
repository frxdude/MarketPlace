package com.diplom.marketplace.entity;

import com.diplom.marketplace.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * User
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
@Table(name = "USERS")
public class User extends Audit {

    @Id
    @Column(nullable = false, name = "ID", length = 45, unique = true)
    private String id;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Pattern(regexp = "^(.+)@(.+)$", message = "{val.email}")
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Pattern(regexp = "\\d+", message = "{val.number}")
    @Size(min = 8, max = 8, message = "{val.length}")
    @Column(name = "PHONE", length = 16)
    private String phone;

    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
    @Column(name = "FIRSTNAME", length = 64)
    private String firstname;

    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
    @Column(name = "LASTNAME", length = 64)
    private String lastname;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @PrePersist
    private void prePersist() {
        if (getId() == null)
            setId(UUID.randomUUID().toString());
    }
}
