package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Photo;
import com.bobmate.bobmate.service.PhotoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 사진관련
 */
@RestController
@RequiredArgsConstructor
public class PhotoApiController {

    private final PhotoService photoService;

    /**
     * 사진을 제공
     */
    @GetMapping("/api/v1/photo/{id}")
    @ApiOperation(value = "사진을 제공")
    public ResponseEntity<Resource> photoDetailV1(@PathVariable("id") Long id) throws IOException {
        Photo photo = photoService.findOne(id);
        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = photo.getFilePath();

        Resource resource = new FileSystemResource(absolutePath + path);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(photo.getFileName())
                .build();

        HttpHeaders header = new HttpHeaders();
        Path filePath = Paths.get(path);
        header.add("Content-Type", Files.probeContentType(filePath));
        header.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }
}
