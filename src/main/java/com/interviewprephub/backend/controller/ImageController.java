package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.entity.UserImage;
import com.interviewprephub.backend.repository.UserImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageController {

    @Autowired
    private UserImageRepository userImageRepository;

    @PostMapping("/upload-image/")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("id") String userId) {
        
        try {
            // Check if user already has an image
            UserImage userImage = userImageRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        UserImage newImage = new UserImage();
                        newImage.setUserId(userId);
                        return newImage;
                    });

            userImage.setName(name);
            userImage.setImageData(image.getBytes());
            userImage.setContentType(image.getContentType());
            
            UserImage savedImage = userImageRepository.save(userImage);

            Map<String, Object> response = new HashMap<>();
            response.put("id", savedImage.getId());
            response.put("message", "Image uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to upload image");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/get-image/{userId}/")
    public ResponseEntity<?> getImage(@PathVariable String userId) {
        return userImageRepository.findByUserId(userId)
                .map(image -> {
                    if (image.getImageData() == null) {
                        return ResponseEntity.notFound().build();
                    }
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(
                            image.getContentType() != null ? image.getContentType() : "image/jpeg"));
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", image.getId());
                    response.put("name", image.getName());
                    response.put("image_base64", java.util.Base64.getEncoder().encodeToString(image.getImageData()));
                    response.put("contentType", image.getContentType());
                    
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
