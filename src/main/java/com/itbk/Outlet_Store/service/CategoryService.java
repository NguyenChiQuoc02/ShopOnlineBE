package com.itbk.Outlet_Store.service;

import com.itbk.Outlet_Store.domain.Category;
import com.itbk.Outlet_Store.domain.PageResult;
import com.itbk.Outlet_Store.domain.TypeProduct;
import com.itbk.Outlet_Store.repository.CategoryRepository;
import com.itbk.Outlet_Store.repository.TypeProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final TypeProductRepository  typeProductRepository;

  public CategoryService(CategoryRepository categoryRepository,TypeProductRepository typeProductRepository) {
    this.categoryRepository = categoryRepository;
    this.typeProductRepository = typeProductRepository;
  }

  public List<Category> getAllCategory(){
      return this.categoryRepository.findAll();
  }

  public Category getCategoryById(Long id){
    Optional<Category> currentCategory = this.categoryRepository.findById(id);
    if(currentCategory.isPresent()) {
      return currentCategory.get();
    }
    return null;
  }

  public Category getCategoryByName(String name){
    return this.categoryRepository.findByName(name);
  }



  public PageResult<Category> getCategorys(int page, int size, String keyword) {

    Pageable pageable = PageRequest.of(page, size);
    Page<Category> Categorys;

    if (keyword != null && !keyword.isEmpty()) {
      Categorys = categoryRepository.findByNameContaining(keyword, pageable);
    } else {
      Categorys = categoryRepository.findAll(pageable);
    }

    return new PageResult<>(Categorys.getContent(), Categorys.getTotalPages());
  }


  public Category createCategory( Category addCategory){
      Category category = new Category();
      category.setName(addCategory.getName());
      TypeProduct typeProduct = this.typeProductRepository.findById(addCategory.getIdTypeProduct().getId())
              .orElseThrow(() -> new RuntimeException("TypeProduct not found"));
      category.setIdTypeProduct(typeProduct);
      Category savedCategory = categoryRepository.save(category);
      return savedCategory;
  }

  public Category updateCategory(long id, String name, long idTypeProduct){
    Category existingCategory = categoryRepository.findById(id).orElse(null);
    if (existingCategory == null) {
      return null;
    }
    existingCategory.setName(name);
    TypeProduct typeProduct = this.typeProductRepository.findById(idTypeProduct)
            .orElseThrow(() -> new RuntimeException("TypeProduct not found"));
    existingCategory.setIdTypeProduct(typeProduct);
    Category updatedCategory = categoryRepository.save(existingCategory);
    return existingCategory;
  }

  public boolean deleteCategory(long id){
    Optional<Category> opt = categoryRepository.findById(id);
    if (opt.isPresent()) {
      Category category = opt.get();
      categoryRepository.delete(category);
      return  true;
    }
    return false;
  }

}
