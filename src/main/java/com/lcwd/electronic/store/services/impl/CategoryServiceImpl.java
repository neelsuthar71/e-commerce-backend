package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exception.ResourcesNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String Id = UUID.randomUUID().toString();
        categoryDto.setCategoryId(Id);

       Category category= mapper.map(categoryDto,Category.class);
       Category savedCategory=categoryRepository.save(category);
       categoryDto=mapper.map(savedCategory,CategoryDto.class);
       return categoryDto;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

//        Category category=mapper.map(categoryDto,Category.class);
        Category fetchedCategory=
        categoryRepository.findById(categoryId).orElseThrow(()->  new ResourcesNotFoundException("category with this id does not exist"));

        fetchedCategory.setTitle(categoryDto.getTitle());
        fetchedCategory.setDescription(categoryDto.getDescription());
        fetchedCategory.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory=categoryRepository.save(fetchedCategory);

        categoryDto=mapper.map(updatedCategory,CategoryDto.class);

        return categoryDto;
    }

    @Override
    public void deleteCategory(String categoryId) {

        Category category=
                categoryRepository.findById(categoryId).orElseThrow(()->  new ResourcesNotFoundException("category with this id does not exist"));

        categoryRepository.delete(category);


    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize);
        Page<Category> page=categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response=Helper.getPageableResponse(page,CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto getSingleCategory(String categoryId) {
        Category category=
                categoryRepository.findById(categoryId).orElseThrow(()->  new ResourcesNotFoundException("category with this id does not exist"));

        return mapper.map(category,CategoryDto.class);
    }
}
