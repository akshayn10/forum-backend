package com.akshayan.forumbackend.controller;

import com.akshayan.forumbackend.dto.CategoryDto;
import com.akshayan.forumbackend.dto.CategoryRequestDto;
import com.akshayan.forumbackend.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryRequestDto> createCategory(@RequestBody CategoryRequestDto categoryRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.createCategory(categoryRequestDto));
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategory());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(id));
    }
}
