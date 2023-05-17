package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.exception.BadApiRequest;
import com.lcwd.electronic.store.services.FileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        String originalFileName=file.getOriginalFilename();
        logger.info("file name{}",originalFileName);
        String filename= UUID.randomUUID().toString();

        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));

        String filenameWithExtension=filename+extension;

        String fullPathWithFileName=path+filenameWithExtension;

        logger.info("full image name:{}",filenameWithExtension);
        logger.info("full image name with oath:{}",fullPathWithFileName);

        if(extension.equalsIgnoreCase(".png")
                ||extension.equalsIgnoreCase(".jpg")
                ||extension.equalsIgnoreCase(".jpeg"))
        {
            File folder=new File(path);
            if(!folder.exists()){
                //create new folder
                folder.mkdirs();
            }

            //upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            //return filename
            return filenameWithExtension;
        }
        else {
            throw new BadApiRequest("file this extension is not allowed");
        }
    }

    @Override
    public InputStream getResources(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputstream=new FileInputStream(fullPath);
        return inputstream;
    }
}
