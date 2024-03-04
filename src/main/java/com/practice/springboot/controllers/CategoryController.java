package com.practice.springboot.controllers;

import com.practice.springboot.payloads.CategoryDto;
import com.practice.springboot.services.implementations.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryServiceImpl.createCategory(categoryDto);
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable int categoryId) {
        return categoryServiceImpl.updateCategory(categoryDto, categoryId);
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable int categoryId) {
        return categoryServiceImpl.getCategoryById(categoryId);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategory() {
        return categoryServiceImpl.getAllCategories();
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int categoryId) {
        categoryServiceImpl.deleteCategory(categoryId);
    }
}
