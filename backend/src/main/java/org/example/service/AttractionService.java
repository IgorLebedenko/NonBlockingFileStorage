package org.example.service;

import org.example.model.AttractionModel;
import org.example.model.FileModel;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/attractions")
public interface AttractionService {

    @GetMapping
    List<AttractionModel> findAll();

    @GetMapping("/{id}")
    AttractionModel findOne(@PathVariable(name = "id") Integer id);

    @PostMapping
    AttractionModel create(@RequestBody AttractionModel model);

    @PutMapping("/{id}")
    AttractionModel update(@PathVariable(name = "id") Integer id, @RequestBody AttractionModel model);

    @DeleteMapping("/{id}")
    void delete(@PathVariable(name = "id") Integer id);



    @CrossOrigin(origins = "*")
    @PostMapping("/{id}/image/upload")
    FileModel uploadImage(@PathVariable(name = "id") Integer id, @RequestParam("file") MultipartFile image);

    @CrossOrigin(origins = "*")
    @DeleteMapping("/image/delete/{imageId}")
    void deleteImage(@PathVariable(name = "imageId") Integer imageId);

    @CrossOrigin(origins = "*")
    @GetMapping("/image/{imageId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "imageId") Integer imageId);
}
