package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private Logger logger= LoggerFactory.getLogger(UserController.class);

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryService categoryService;

//    /create /
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto  categoryDto){
        CategoryDto CategoryDto1=categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(CategoryDto1, HttpStatus.CREATED);

    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable("categoryId") String categoryId){

       CategoryDto categoryDto1= categoryService.updateCategory(categoryDto,categoryId);
    return new ResponseEntity<>(categoryDto1,HttpStatus.OK);
    }

    //delete

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage responseMessage=
        ApiResponseMessage.builder()
                .message("category is deleted successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    //getall

    @GetMapping

    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse=categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);

    }
    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable("categoryId")String categoryId){
        CategoryDto categoryDto=categoryService.getSingleCategory(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }


    //file upload and serve
    @PostMapping("/categoryImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable("categoryId") String categoryId, @RequestParam("categoryImage") MultipartFile image) throws IOException {
//         private String imageUploadPath;
        String imageName=fileService.uploadImage(image,imageUploadPath);
        CategoryDto categoryDto=categoryService.getSingleCategory(categoryId);
        categoryDto.setCoverImage(imageName);

        CategoryDto categoryDto1=categoryService.updateCategory(categoryDto,categoryId);

        ImageResponse imageResponse=ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    //serve image
    @GetMapping("/categoryImage/{categoryId}")
    public void serveUserImage(@PathVariable("categoryId") String categoryId, HttpServletResponse response) throws IOException {

        CategoryDto categoryDto=categoryService.getSingleCategory(categoryId);

        InputStream resources=fileService.getResources(imageUploadPath,categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resources,response.getOutputStream());
    }

}
