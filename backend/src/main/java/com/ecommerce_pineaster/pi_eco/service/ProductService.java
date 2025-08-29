package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.config.AppConstant;
import com.ecommerce_pineaster.pi_eco.model.Product;
import com.ecommerce_pineaster.pi_eco.payload.ProductDTO;
import com.ecommerce_pineaster.pi_eco.payload.ProductResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);


    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder );


    ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
