package com.example.server;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ImageService {

    @Value("${app.path.upload.path}")
    private String uploadPath;

    @PostConstruct
    void init() {
        File folder = new File(uploadPath);
        folder.mkdir();
    }

    public void saveImage(MultipartFile file) throws IOException {
        File folder = new File(uploadPath);
        File target = new File(folder.getAbsoluteFile() + "/" + file.getOriginalFilename());

        Files.copy(file.getInputStream(),
                target.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

    }

    public List<String> getUploadFilesFromFolder() {
        return Arrays.stream(Objects.requireNonNull(new File(uploadPath).listFiles()))
                .map(File::getName)
                .toList();
    }

}
