package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.exception.ApiException;
import com.ecommerce_pineaster.pi_eco.exception.ResourceNotFoundException;
import com.ecommerce_pineaster.pi_eco.model.Category;
import com.ecommerce_pineaster.pi_eco.payload.CategoryDTO;
import com.ecommerce_pineaster.pi_eco.payload.CategoryResponse;
import com.ecommerce_pineaster.pi_eco.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceIml implements CategoryService {

//    private List<Category> categories = new ArrayList<>();
//    private Long nextId = 1L; // start with 1
  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ModelMapper modelMapper;

    public CategoryServiceIml() {
    }

    @Override
    public CategoryResponse getAllCategories( Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending()
               :Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);
        Page<Category> categoryPage =  categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();

        // List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()){
            throw new ApiException("Category is empty");
        }else{

            List<CategoryDTO>  categoryDTOS = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .toList();

            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDTOS);
            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalElements(categoryPage.getTotalElements());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setLastPage(categoryPage.isLast());
            return  categoryResponse;
        }

    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category =  modelMapper.map(categoryDTO,Category.class);

        Category saveCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(saveCategory!=null){
            throw  new ApiException("Category :"+ category.getCategoryName()+" already present");
        }


         Category  savedCategory = categoryRepository.save(category);
        CategoryDTO convertDtoFrom = modelMapper.map(savedCategory,CategoryDTO.class);

        return convertDtoFrom;


    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

//        Optional<Category> categories =  categoryRepository.findById(categoryId);
//
//        Category category = categories.
//                            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND," Id not found"));
//        categoryRepository.delete(category);


        // Above code  also we can write more optimized way

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Categroy","CategoryId",categoryId));
        categoryRepository.delete(category);

        CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);

        return  categoryDTO ;

//        Category category = categories.stream()
//                .filter(c->c.getCategoryId().equals(categoryId))
//                .findFirst().
//                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"\"Resource not found\""));

//   Without stream we can do that
//        Category category = null;
//        for (Category c : categories) {
//            if (c.getCategoryId().equals(categoryId)) {
//                category = c;
//                break;
//            }
//        }
//
//        if (category == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
//        }


    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {


//        Optional<Category> saveCategoryOptional =  categoryRepository. findById(id);

        Category saveCategory = categoryRepository.findById(id).
                                 orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",id));

            Category category = modelMapper.map(categoryDTO,Category.class);

            category.setCategoryId(id);

             saveCategory  =  categoryRepository.save(category);
             CategoryDTO  savedCategoryDto = modelMapper.map(saveCategory,CategoryDTO.class);
             return savedCategoryDto;

            //old way
//        Optional<Category> optionalCategory = categories.stream()
//                .filter(c -> (c.getCategoryId()).equals(id))
//                .findFirst();
//
//        if(optionalCategory.isPresent()){
//            Category existingCategory  = optionalCategory.get();
//            existingCategory.setCategoryName(category.getCategoryName());
//              Category saveCategory = categoryRepository.save(existingCategory);
//            return saveCategory;
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found");
//        }


    }


}
