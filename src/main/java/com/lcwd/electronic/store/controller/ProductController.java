package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductController {
    //create

    @Value("${product.profile.image.path}")
    private String imagepath;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @PostMapping
    public ResponseEntity<ProductDto>createProduct(@RequestBody ProductDto productDto){

        ProductDto productDto1=productService.createProduct(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }


    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto>updateProduct(@PathVariable("productId") String productId,@RequestBody ProductDto productDto){

        ProductDto updatedProduct=productService.updateProduct(productDto,productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }



    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage>deleteProduct(@PathVariable("productId") String productId){

       productService.deleteProduct(productId);
       ApiResponseMessage responseMessage= ApiResponseMessage.builder()
               .message("product is deleted successfully")
               .success(true)
               .status(HttpStatus.OK)
               .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

//    get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto>getSingleProduct(@PathVariable("productId") String productId){

        ProductDto singleProduct=productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.CREATED);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>>getAllProduct(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "20",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "productTitle",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse=productService.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>>getAllProductLive(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "productTitle",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse=productService.getAllProductLive(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //search all
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>>getAllProductSearch(
            @PathVariable("query") String query,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "productTitle",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> pageableResponse=productService.searchByTitle(query,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //image upload

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable("productId") String productId,
            @RequestParam("productImage")MultipartFile image

            ) throws IOException {

        String fileName=fileService.uploadImage(image,imagepath);
        ProductDto productDto =productService.getSingleProduct(productId);
        productDto.setProductImage(fileName);
        ProductDto updatedDto=productService.updateProduct(productDto,productId);
        ImageResponse imageResponse =ImageResponse.builder()
                .imageName(updatedDto.getProductImage())
                .message("product is successfully updated")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }

    //serve image

    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

       ProductDto product=productService.getSingleProduct(productId);
        InputStream resources=fileService.getResources(imagepath,product.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resources,response.getOutputStream());
    }


}
