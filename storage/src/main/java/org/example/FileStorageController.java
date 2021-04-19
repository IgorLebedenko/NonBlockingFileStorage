package org.example;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Controller
public class FileStorageController implements FileStorage {
    private String root = "/file-storage";

    @Override
    public InputStream getContent(String path) {
        Path target = Paths.get(root, path);
        try {
            return Files.newInputStream(target);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String saveContent(InputStream content, String path) {
        Path target = Paths.get(root, path);
        try {
            Files.createDirectories(target.getParent());
            Files.copy(content, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    @Override
    public void deleteContent(String path) {
        Path target = Paths.get(root, path);
        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
