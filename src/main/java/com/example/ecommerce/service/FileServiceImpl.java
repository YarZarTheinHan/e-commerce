package com.example.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service 
public class FileServiceImpl implements FileService{
    public String uploadImage(String path, MultipartFile file) throws IOException {
       //Take original file name
        String originalFileName = file.getOriginalFilename();
       //Create the unique random id
        String uniqueId = UUID.randomUUID().toString();
       //random id + file extention
        String imageExtention = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newImageName = uniqueId.concat(imageExtention);
       //Define the file path with the file name
        String filePath = path+File.separator+newImageName;
       //Create new folder (if not already exist)
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
        // File copy to that path
        Files.copy(file.getInputStream(), Paths.get(filePath));
       // return the String value of image name
       return newImageName;
    }
}
