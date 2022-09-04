package com.mycustomblog.blog.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadImgDTO {
    private MultipartFile img;
}
