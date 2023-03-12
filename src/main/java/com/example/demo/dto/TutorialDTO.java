package com.example.demo.dto;

import lombok.Data;

@Data
public class TutorialDTO {

    private Integer id;

    private String title;

    private String description;

    private int level;

    private boolean published;
}
