package com.mungta.user.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mungta.user.dto.FileInfo;

@Service
public class FileSystemStorageService implements StorageService {

    private String uploadPath;


    @Override
    public FileInfo store(final String userId, final MultipartFile file) {

        String oldFileName ="";
        String fileName = "";
        String fileExtension ="";

        try {
            if (file.isEmpty()) {
                throw new Exception("ERROR : File is empty.");
            }
            // 파일명 생성

            oldFileName = file.getOriginalFilename();

            try {
                fileExtension = oldFileName.substring( oldFileName.lastIndexOf(".")+1);
            } catch (Exception e) {
                throw new RuntimeException("Could not find extension. Error: " + e.getMessage());
            }
            fileName = UUID.randomUUID().toString() +"_"+
                       userId+"."+
                       fileExtension;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return FileInfo.builder().userFileName(fileName).userFileOriName(oldFileName).userFileSize(0).userFileUrl(uploadPath).fileExtension(fileExtension).build();
    }

}
