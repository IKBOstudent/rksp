package com.example.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping("/save")
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file) {
        try {
            imageService.saveImage(file);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("error");
        }
        return ResponseEntity.ok("file saved");
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", imageService.getUploadFilesFromFolder());
        return "index";
    }
}
