package com.ecommerce_pineaster.pi_eco.controller;
import com.ecommerce_pineaster.pi_eco.config.AppConstant;
import com.ecommerce_pineaster.pi_eco.payload.CategoryDTO;
import com.ecommerce_pineaster.pi_eco.payload.CategoryResponse;
import com.ecommerce_pineaster.pi_eco.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public  ResponseEntity<CategoryResponse>  getAllCategories(@RequestParam(value = "pageNumber",
            defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
                                                               @RequestParam(value = "pageSize",
            defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                               @RequestParam(value = "sortBy",
             defaultValue = AppConstant.SORT_CATEGORIES_BY, required = false) String sortBy,
                                                               @RequestParam(value = "sortOrder",
             defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
                                                               ){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return  new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }


    @PostMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO   savedCategory = categoryService.createCategory(categoryDTO);
        return  new ResponseEntity<>(savedCategory, HttpStatus.CREATED);

    }

    @DeleteMapping("/admin/category/{categoryId}")
    //@RequestMapping(value = "/admin/category/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDTO> deleteCategory( @PathVariable Long categoryId){
           CategoryDTO category =  categoryService.deleteCategory(categoryId);
           return new ResponseEntity<>(category, HttpStatus.OK);
//           return  ResponseEntity.ok(status);
//           return  ResponseEntity.status(HttpStatus.OK).body(status);

    }

    @PutMapping("/admin/categories/{id}")
    //@RequestMapping(value = "/admin/categories/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable Long id){


              CategoryDTO savedCategory = categoryService.updateCategory(categoryDTO,id);

              return  new ResponseEntity<>(savedCategory, HttpStatus.ACCEPTED);



    }

}
