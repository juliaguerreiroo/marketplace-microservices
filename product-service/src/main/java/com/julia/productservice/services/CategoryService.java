package com.julia.productservice.services;

import com.julia.productservice.entities.Category;
import com.julia.productservice.exceptions.CategoryNotFoundException;
import com.julia.productservice.exceptions.CategoryRegisteredException;
import com.julia.productservice.exceptions.InvalidDataException;
import com.julia.productservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        if(id == null){
            throw new InvalidDataException("Id is required");
        }

        Optional<Category> obj = categoryRepository.findById(id);
        if(obj.isEmpty()){
            throw new CategoryNotFoundException("Category does not exist");
        }
        return obj.get();
    }

    public Category insert(Category category){
        if(category == null){
            throw new InvalidDataException("Registration data is required");
        }

        if(category.getName() == null || category.getName().isBlank()){
            throw new InvalidDataException("Name is required");
        }

        category.setName(category.getName().trim());

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new CategoryRegisteredException("Category already registered");
        }

        return categoryRepository.save(category);
    }

    public void delete(Long id){
        findById(id);
        categoryRepository.deleteById(id);
    }

    public Category update(Long id, Category category){
        if(category.getName() == null || category.getName().isBlank()){
            throw new InvalidDataException("Name is required");
        }

        Category entity = findById(id);
        entity.setName(category.getName().trim());

        if (!entity.getName().equalsIgnoreCase(category.getName())
                && categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new CategoryRegisteredException("Category already registered");
        }

        return categoryRepository.save(entity);
    }

}
