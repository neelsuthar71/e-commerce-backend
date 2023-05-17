package com.lcwd.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    //general method to store all the images
    public String uploadImage(MultipartFile file,String path) throws IOException;

    InputStream getResources(String path,String name) throws FileNotFoundException;

}
