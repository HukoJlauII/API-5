package com.example.api5.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Schema(example = "89853661411")
    private String number;

    @Schema(example = "Name")
    private String name;
    @Schema(example = "Surname")
    private String surname;

    public Contact(String number, String name, String surname) {
        this.number = number;
        this.name = name;
        this.surname = surname;
    }
}
