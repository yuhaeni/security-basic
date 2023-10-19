package com.example.basic.users.domain;

import com.example.basic.users.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    @Digits(integer = 3, fraction = 0)
    private int age;

    @Column(columnDefinition = "char(1) default('M')")
    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;


}
