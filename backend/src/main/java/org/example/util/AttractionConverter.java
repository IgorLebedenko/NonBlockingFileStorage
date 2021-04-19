package org.example.util;

import org.example.entity.AttractionEntity;
import org.example.entity.AttractionImageEntity;
import org.example.model.AttractionModel;
import org.example.model.FileModel;

import java.util.stream.Collectors;

public class AttractionConverter {

    public static AttractionModel model(AttractionEntity entity) {
        AttractionModel model = new AttractionModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setPlace(entity.getPlace());
        if (entity.getImages() != null)
            model.setImages(entity.getImages().stream().map(AttractionConverter::fileModel).collect(Collectors.toList()));
        return model;
    }

    private static FileModel fileModel(AttractionImageEntity entity) {
        FileModel model = new FileModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }

    public static AttractionEntity entity(AttractionModel model) {
        AttractionEntity entity = new AttractionEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setPlace(model.getPlace());
        return entity;
    }
}
