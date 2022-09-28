package com.mungta.user.service;
import org.springframework.web.multipart.MultipartFile;

import com.mungta.user.dto.FileInfo;

public interface StorageService {

    FileInfo store(String userId,MultipartFile file);

}
