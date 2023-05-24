package com.lcwd.electronic.store.services.impl;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exception.ResourcesNotFoundException;

import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service


public class ProductServiceImpl implements ProductService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product =mapper.map(productDto,Product.class);
        String productId =UUID.randomUUID().toString();
        product.setProductId(productId);

        product.setAddedDate(new Date());

        Product saveProduct=productRepository.save(product);
        return mapper.map(saveProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
         Product product=productRepository.findById(productId).orElseThrow(()-> new ResourcesNotFoundException("product not found of given id"));
         product.setProductTitle(productDto.getProductTitle());
         product.setGetProductDescription(productDto.getGetProductDescription());
         product.setPrice(productDto.getPrice());
         product.setDiscountedPrice(productDto.getDiscountedPrice());
         product.setQuantity(productDto.getQuantity());
         product.setLive(productDto.isLive());
         product.setStock(product.isStock());

         Product saveProduct = productRepository.save(product);

        return mapper.map(saveProduct,ProductDto.class);

    }

    @Override
    public void deleteProduct(String productId) {
        Product product=productRepository.findById(productId).orElseThrow(()-> new ResourcesNotFoundException("product not found of given id"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        Product product=
                productRepository.findById(productId).orElseThrow(()->  new ResourcesNotFoundException("product with this id does not exist"));

        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findAll(pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }


    @Override
    public PageableResponse<ProductDto> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page,ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByproductTitleContaining(subTitle,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

}
