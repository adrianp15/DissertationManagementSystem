package com.university.dms.models;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    @NotNull(message = "*Please provide an email")
    private String email;

    @Length(min = 8, message = "*Your password must have at least 8 characters")
    private String password;

    @NotEmpty(message = "*Please provide your first name")
    @NotNull(message = "*Please provide your first name")
    private String firstName;

    @NotEmpty(message = "*Please provide your last name")
    @NotNull(message = "*Please provide your last name")
    private String lastName;

    private int active;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


}
