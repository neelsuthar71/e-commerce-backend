package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {
    //create
    ProductDto createProduct(ProductDto productDto);
    //update
    ProductDto updateProduct (ProductDto productDto,String productId);
    //delete
    void deleteProduct(String productId);
    //getSingle
    ProductDto getSingleProduct(String productId);
    //getAll
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);
    //getAll live
    PageableResponse<ProductDto> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir);
    //searchByTitle
    PageableResponse<ProductDto> searchByTitle( String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);

}
