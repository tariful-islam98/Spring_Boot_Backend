package com.practice.springboot.services.implementations;

import com.practice.springboot.entities.Category;
import com.practice.springboot.exceptions.NotNullViolationException;
import com.practice.springboot.payloads.CategoryDto;
import com.practice.springboot.repositories.CategoryRepo;
import com.practice.springboot.services.interfaces.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepo categoryRepo;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(@Valid CategoryDto categoryDto) {
        Category categoryEntity = modelMapper.map(categoryDto, Category.class);

        try {
            Category savedCategory = categoryRepo.save(categoryEntity);

            return modelMapper.map(savedCategory, CategoryDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Title already exists", e);
        } catch (NotNullViolationException e) {
            throw new NotNullViolationException("Error creating category: categoryTitle field is required and cannot be blank", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", e);
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));
        try {
            if (categoryDto.getCategoryTitle() != null && !categoryDto.getCategoryTitle().isBlank()) {
                String newTitle = categoryDto.getCategoryTitle();

                if (!newTitle.equals(category.getCategoryTitle())) {
                    System.out.println("Before existsByCategoryTitleIgnoreCase check");
                    if (categoryRepo.existsByCategoryTitleIgnoreCaseAndCategoryIdNot(newTitle, categoryId)) {
                        System.out.println("Category Title already exists");
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Title already exists");
                    }
                    System.out.println("After existsByCategoryTitleIgnoreCase check");
                }

                category.setCategoryTitle(newTitle);

            }
            if (categoryDto.getDescription() != null) {
                category.setDescription(categoryDto.getDescription());
            }

            Category updateCategory = categoryRepo.save(category);
            return modelMapper.map(updateCategory, CategoryDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating category", e);
        }
    }

    @Override
    public CategoryDto getCategoryById(int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categoryList = categoryRepo.findAll();

        return categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(int categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

        categoryRepo.delete(category);
    }
}
