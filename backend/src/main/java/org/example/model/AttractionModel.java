package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttractionModel {
    private Integer id;
    private String name;
    private String description;
    private String place;
    private List<FileModel> images;
}
