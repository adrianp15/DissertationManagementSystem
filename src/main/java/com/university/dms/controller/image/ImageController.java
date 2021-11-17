package com.university.dms.controller.image;

import com.university.dms.Utils.ImageUtility;
import com.university.dms.model.Image;
import com.university.dms.model.user.User;
import com.university.dms.repository.ImageRepository;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@CrossOrigin() // open for all ports
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/upload/image")
    public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile file)
            throws IOException, URISyntaxException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        if(!file.isEmpty()){

            // check if the user already has a profile photo and delete the old one
            final Optional<Image> dbImage = imageRepository.findByName(user.getUserName());
            if(dbImage.isPresent()) {
                imageRepository.deleteByName(user.getUserName());
            }

            imageRepository.save(Image.builder()
                    .name(user.getUserName())
                    .type(file.getContentType())
                    .image(ImageUtility.compressImage(file.getBytes())).build());
        }

        URI uri = new URI("../profile");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

    }

    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        Optional<Image> dbImage = imageRepository.findByName(name);

        if(dbImage.isEmpty()) {
            dbImage = imageRepository.findByName("default");
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getImage()));
    }
}