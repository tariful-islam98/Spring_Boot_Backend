package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.CategoryDto;

import java.util.List;

public interface CategoryServiceInterface {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, int categoryId);

    CategoryDto getCategoryById(int categoryId);

    List<CategoryDto> getAllCategories();

    void deleteCategory(int categoryId);
}
