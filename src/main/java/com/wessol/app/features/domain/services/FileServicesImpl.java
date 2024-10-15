package com.wessol.app.features.domain.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@Configuration
public class FileServicesImpl implements FileServices {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {

        //file path
        String filePath = path + File.separator + file.getOriginalFilename();

        //create File object
        File f = new File(path);
        if (!f.exists()) f.mkdirs();

        //save file
        Files.copy(file.getInputStream() , Paths.get(filePath) , StandardCopyOption.REPLACE_EXISTING);

        return file.getOriginalFilename();
    }

    @Override
    public FileInputStream getResourceFile(String path, String name) throws FileNotFoundException {
        //file path
        String filePath = path + File.separator + name;
        return new FileInputStream(filePath);
    }
}
