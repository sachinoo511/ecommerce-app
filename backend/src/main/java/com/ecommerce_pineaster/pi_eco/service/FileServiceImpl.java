package com.ecommerce_pineaster.pi_eco.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // Original File Name
        String originalFileName =  file.getOriginalFilename();

        //Generate Unique File Name;
        String randomId = UUID.randomUUID().toString();

        // Ex -> a3c.jpg --> random  = 123   --> 123.jpg
        String newFileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String newFilePath = path+ File.separator +newFileName;

        //Check if path exist and create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        //upload to server
        Files.copy(file.getInputStream(), Paths.get(newFilePath));

        //returning file
        return newFileName;



    }
}
