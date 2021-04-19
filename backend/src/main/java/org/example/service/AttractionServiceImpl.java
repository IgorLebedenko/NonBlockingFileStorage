package org.example.service;

import org.example.FileStorage;
import org.example.entity.AttractionEntity;
import org.example.entity.AttractionImageEntity;
import org.example.model.AttractionModel;
import org.example.model.FileModel;
import org.example.repository.AttractionImageRepository;
import org.example.repository.AttractionRepository;
import org.example.util.AttractionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AttractionServiceImpl implements AttractionService {

    @Autowired
    private AttractionRepository repository;
    @Autowired
    private AttractionImageRepository imageRepository;
    @Autowired
    private FileStorage fileStorage;
    @Value("${backend.url}")
    private String backendUrl;

    @Override
    public List<AttractionModel> findAll() {
        return repository.findAll().stream()
                .map(AttractionConverter::model)
                .map(this::fillImagesUrl)
                .collect(Collectors.toList());
    }

    @Override
    public AttractionModel findOne(Integer id) {
        return fillImagesUrl(AttractionConverter.model(getAttractionEntity(id)));
    }

    @Override
    public AttractionModel create(AttractionModel model) {
        Integer id = repository.save(AttractionConverter.entity(model)).getId();
        model.setId(id);
        return model;
    }

    @Override
    public AttractionModel update(Integer id, AttractionModel model) {
        AttractionEntity entity = getAttractionEntity(id);
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setPlace(model.getPlace());
        repository.save(entity);
        return model;
    }

    @Override
    public void delete(Integer id) {
        repository.delete(getAttractionEntity(id));
    }

    @Override
    public FileModel uploadImage(Integer id, MultipartFile image) {
        AttractionEntity attractionEntity = getAttractionEntity(id);
        AttractionImageEntity imageEntity = new AttractionImageEntity();
        String filename = image.getOriginalFilename();
        imageEntity.setName(filename);
        try {
            // TODO - вместо прямого взаимодействия со StorageController стоит подумать над
            // идеей построения слоя для взаимодействия через каналы по сокету
            // слой вынести в библиотеку и в rest сервисах вызывать только нужный метод
            String path = fileStorage.saveContent(image.getInputStream(), id + "/" + filename);
            imageEntity.setPath(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        imageEntity.setAttraction(attractionEntity);
        Integer imageId = imageRepository.save(imageEntity).getId();

        return new FileModel(imageId, getImageUrl(imageId), imageEntity.getName());
    }

    @Override
    public void deleteImage(Integer imageId) {
        AttractionImageEntity imageEntity = getImageEntity(imageId);
        fileStorage.deleteContent(imageEntity.getPath());
        imageRepository.delete(imageEntity);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Integer imageId) {
        AttractionImageEntity imageEntity = getImageEntity(imageId);

        Resource resource = new InputStreamResource(fileStorage.getContent(imageEntity.getPath()));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageEntity.getName() + "\"")
                .body(resource);
    }

    private AttractionEntity getAttractionEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attraction not found"));
    }

    private AttractionImageEntity getImageEntity(Integer id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attraction image not found"));
    }

    private AttractionModel fillImagesUrl(AttractionModel model) {
        if (model.getImages() != null)
            model.getImages().stream().forEach(i -> i.setUrl(getImageUrl(i.getId())));
        return model;
    }

    private String getImageUrl(Integer id) {
        return backendUrl + "/image/" + id;
    }
}
