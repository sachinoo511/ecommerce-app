package com.ecommerce_pineaster.pi_eco.controller;

import com.ecommerce_pineaster.pi_eco.config.AppConstant;
import com.ecommerce_pineaster.pi_eco.model.Product;
import com.ecommerce_pineaster.pi_eco.payload.ProductDTO;
import com.ecommerce_pineaster.pi_eco.payload.ProductResponse;
import com.ecommerce_pineaster.pi_eco.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {


    @Autowired
    private ProductServiceImpl productServiceIml;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId){

            ProductDTO productDTOS = productServiceIml.addProduct(categoryId,productDTO);

            return   new ResponseEntity<>(productDTOS, HttpStatus.OK);

    }

    @GetMapping("/public/products")
     public  ResponseEntity<ProductResponse> getAllProduct(
             @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
             @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
             @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
             @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder){

         ProductResponse productResponse = productServiceIml.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);

         return  new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,@RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder){

        ProductResponse productResponse = productServiceIml.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);

        return   new ResponseEntity<>(productResponse, HttpStatus.OK);

    }
    @GetMapping("/public/product/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,@RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
                                                               @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
                                                               @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder){
        ProductResponse productResponse = productServiceIml.getProductByKeyword(keyword ,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }

    @PutMapping("/admin/product/{productId}")
    public  ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
                                                          @PathVariable Long productId){
        ProductDTO productResponse = productServiceIml.updateProduct(productDTO,productId);
        return  new ResponseEntity<>(productResponse,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/admin/product/{productId}")
    public  ResponseEntity<ProductDTO> deleteProduct( @PathVariable Long productId){
        ProductDTO deleteProduct = productServiceIml.deleteProduct(productId);
        return  new ResponseEntity<>(deleteProduct,HttpStatus.OK);
    }

    @PutMapping("/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("name")MultipartFile image) throws IOException {

        ProductDTO updateProduct =  productServiceIml.updateProductImage(productId,image);

        return new ResponseEntity<>(updateProduct, HttpStatus.OK);

    }


}
