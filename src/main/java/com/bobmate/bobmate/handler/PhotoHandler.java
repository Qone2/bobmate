package com.bobmate.bobmate.handler;

import com.bobmate.bobmate.domain.Photo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class PhotoHandler {

    public List<Photo> parseFileInfo(List<MultipartFile> multipartFiles) {

        List<Photo> photoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = date.format(dateTimeFormatter);

            String absolutePath = new File("").getAbsolutePath() + File.separator;

            String path = "images" + File.separator + current_date;
            File file = new File(path);

            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                if(!wasSuccessful) {
                    System.out.println("file: was not successful");
                }
            }

            for (MultipartFile multipartFile : multipartFiles) {
                String originalFileName = multipartFile.getOriginalFilename();
                if (! validateFileName(Objects.requireNonNull(originalFileName))) {
                    continue;
                }

                String fileExtension;
                String contentType = multipartFile.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    continue;
                } else if (contentType.contains("image/jpeg")) {
                    fileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    fileExtension = ".png";
                } else if (contentType.contains("image/heic")) {
                    fileExtension = ".heic";
                } else if (contentType.contains("image/heif")) {
                    fileExtension = ".heif";
                } else {
                    continue;
                }

                UUID uuid = UUID.randomUUID();

                String newFileName = uuid.toString() + '_' + System.nanoTime() + fileExtension;

                Photo photo = Photo.photoHandle(originalFileName,
                        path + File.separator + newFileName, multipartFile.getSize());

                photoList.add(photo);

                file = new File(absolutePath + path + File.separator + newFileName);
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return photoList;
    }


    private boolean validateFileName(String originalFileName) {
        if (originalFileName.contains("%")) {
            return false;
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);

        if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") ||
                fileExtension.equals("heic") || fileExtension.equals("heif")) {
            return true;
        } else {
            return false;
        }
    }
}
