package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface CategoryService {
    //create
    CategoryDto createCategory(CategoryDto categoryDto);
    //update
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    //delete

    void deleteCategory(String categoryId);
    //getall

    PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get single category detail

    CategoryDto getSingleCategory(String categoryId);


    //search
}
