package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Photo;
import com.bobmate.bobmate.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/api/v1/photo/{photo_id}")
    @Operation(summary = "사진을 제공", description = "저장되어있는 사진을 리턴<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public ResponseEntity<Resource> photoDetailV1(@PathVariable("photo_id") Long photo_id) throws IOException {
        Photo photo = photoService.findOne(photo_id);
        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = photo.getFilePath();

        Resource resource = new FileSystemResource(absolutePath + path);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                .filename(photo.getOriginalFileName())
                .build();

        HttpHeaders header = new HttpHeaders();
        Path filePath = Paths.get(path);
        header.add("Content-Type", Files.probeContentType(filePath));
        header.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }

    /**
     * 사진 전체조회
     */
    @GetMapping("/api/v1/photo")
    public Result photosV1() {
        List<Photo> photoList = photoService.findAll();
        List<PhotoDto> photoDtoList = photoList.stream().map(p ->new PhotoDto(p)).collect(Collectors.toList());
        return new Result(photoDtoList.size(), photoDtoList);
    }


    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Getter
    static class PhotoDto {
        private Long photo_id;
        private Long review_id;
        private String originalFileName;
        private String filePath;
        private Long fileSize;
        private LocalDateTime savedDate;

        public PhotoDto(Photo photo) {
            this.photo_id = photo.getId();
            this.review_id = photo.getReview().getId();
            this.originalFileName = photo.getOriginalFileName();
            this.filePath = photo.getFilePath();
            this.fileSize = photo.getFileSize();
            this.savedDate = photo.getSavedDate();
        }
    }


    /**
     * 사진 삭제
     */
    @DeleteMapping("/api/v1/photo/{photo_id}")
    public DeletePhotoResponse deletePhotoV1(@PathVariable Long photo_id) {
        photoService.deletePhoto(photo_id);
        return new DeletePhotoResponse("성공적으로 삭제되었습니다.");
    }

    @Getter
    @AllArgsConstructor
    static class DeletePhotoResponse {
        private String message;
    }
}
