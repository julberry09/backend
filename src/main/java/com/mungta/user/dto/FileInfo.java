package com.mungta.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
  private String userFileName;
  private String userFileOriName;
  private long   userFileSize;
  private String userFileUrl;
  private String fileExtension;
}
