package com.ecommerce_pineaster.pi_eco.service;
import com.ecommerce_pineaster.pi_eco.exception.ApiException;
import com.ecommerce_pineaster.pi_eco.exception.ResourceNotFoundException;
import com.ecommerce_pineaster.pi_eco.model.Cart;
import com.ecommerce_pineaster.pi_eco.model.Category;
import com.ecommerce_pineaster.pi_eco.model.Product;
import com.ecommerce_pineaster.pi_eco.payload.CartDTO;
import com.ecommerce_pineaster.pi_eco.payload.ProductDTO;
import com.ecommerce_pineaster.pi_eco.payload.ProductResponse;
import com.ecommerce_pineaster.pi_eco.repository.CartRepository;
import com.ecommerce_pineaster.pi_eco.repository.CategoryRepository;
import com.ecommerce_pineaster.pi_eco.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService  {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        // Check Product is already present?


        Product product = modelMapper.map(productDTO, Product.class);

        Product findProduct = productRepository.findByProductName(product.getProductName());
        if (findProduct != null) {
            throw new ApiException("Product is already present");
        } else {


            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
            product.setProductImage("default.png");
            product.setCategory(category);

            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

            product.setSpecialPrice(specialPrice);

            Product saveProduct = productRepository.save(product);


            return modelMapper.map(saveProduct, ProductDTO.class);
        }
    }


    @Override
    public ProductResponse getAllProducts( Integer pageNumber, Integer pageSize,
                                           String sortBy, String sortOrder ){

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        
        Page<Product>  pageProducts = productRepository.findAll(pageDetails);
        
        

        List<Product> products = pageProducts.getContent();
        
        if(!products.isEmpty()) {

            List<ProductDTO> productDTOS = products.stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOS);
            productResponse.setPageNumber(pageProducts.getNumber());
            productResponse.setPageSize(pageProducts.getSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());
            productResponse.setLastPage(pageProducts.isLast());
            return productResponse;
        }else {
            throw new ApiException("No Product is available");
        }
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","category", categoryId));


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product>  pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);



        List<Product> products = pageProducts.getContent();


        if(!products.isEmpty()){
            List<ProductDTO> productDTOList = products.stream()
                    .map( product -> modelMapper.map(product,ProductDTO.class)).toList();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDTOList);
            productResponse.setPageNumber(pageProducts.getNumber());
            productResponse.setPageSize(pageProducts.getSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());
            productResponse.setLastPage(pageProducts.isLast());

            return productResponse;
        }else{
            throw new ApiException("No product exit!!");
        }


    }

    @Override
    public ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder ) {



        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product>  pageProducts = productRepository.findByProductNameContainingIgnoreCase(keyword,pageDetails);



        List<Product> products = pageProducts.getContent();


         if(!products.isEmpty()){

             List<ProductDTO> productDTOList = products.stream()
                     .map( product -> modelMapper.map(product,ProductDTO.class)).toList();

             ProductResponse productResponse = new ProductResponse();
             productResponse.setContent(productDTOList);
             productResponse.setPageNumber(pageProducts.getNumber());
             productResponse.setPageSize(pageProducts.getSize());
             productResponse.setTotalElements(pageProducts.getTotalElements());
             productResponse.setTotalPages(pageProducts.getTotalPages());
             productResponse.setLastPage(pageProducts.isLast());

             return productResponse;
         }else {
             throw new ApiException("No product exist!!");
         }
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {


        Product product = modelMapper.map(productDTO,Product.class);

        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

       if(findProduct!=null){
           findProduct.setProductName(product.getProductName());
           findProduct.setQuantity(product.getQuantity());
           findProduct.setDescription(product.getDescription());
           findProduct.setPrice(product.getPrice());
           findProduct.setDiscount(product.getDiscount());
           findProduct.setSpecialPrice(product.getSpecialPrice());

           Product saveProduct = productRepository.save(findProduct);

           List<Cart>  carts =  cartRepository.findCartsByProductId(productId);

           List<CartDTO>  cartDTOS = carts.stream()
                   .map(cart ->{
                       CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                       List<ProductDTO> products = cart.getCartItems().stream()
                               .map(cartItem -> modelMapper.map(cartItem.getProduct(),ProductDTO.class)).toList();

                       cartDTO.setProducts(products);
                       return cartDTO;
                   }).toList();

           cartDTOS.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(),productId));

           return modelMapper.map(saveProduct, ProductDTO.class);
       }else {
           throw new ApiException("No product exist!!") ;
       }
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));


        List<Cart>  carts =  cartRepository.findCartsByProductId(productId);
        carts.forEach(cart ->cartService.deleteProductFromCart(cart.getCartId(),productId));

         if(findProduct !=null){

             productRepository.delete(findProduct);


             return modelMapper.map(findProduct,ProductDTO.class);
         }else {
             throw new ApiException("No product exist");
         }
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //Get Product From DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
        // Upload Product On Server
        //Get the File name of uploaded image


        String fileName = fileService.uploadImage(path,image);
        //Uploading the new file name to the product

        product.setProductImage(fileName);

        Product updatedProduct = productRepository.save(product);

        // Return DTO

        return modelMapper.map(updatedProduct,ProductDTO.class);
    }




}
