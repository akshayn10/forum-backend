package com.akshayan.forumbackend.service;

import com.akshayan.forumbackend.dto.CategoryDto;
import com.akshayan.forumbackend.dto.CategoryRequestDto;
import com.akshayan.forumbackend.model.Category;
import com.akshayan.forumbackend.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryRequestDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category =  mapCategoryRequestDtoToCategory(categoryRequestDto);
        categoryRepository.save(category);
        categoryRequestDto.setId(category.getId());
        return categoryRequestDto;

    }

    private Category mapCategoryRequestDtoToCategory(CategoryRequestDto categoryRequestDto) {
        return Category.builder()
                .name(categoryRequestDto.getCategoryName())
                .description(categoryRequestDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategory() {
       return   categoryRepository.findAll()
                .stream()
                .map(this::mapCategoryToCategoryDto)
                .collect(Collectors.toList());
    }

    private CategoryDto mapCategoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getName())
                .description(category.getDescription())
                .numberOfPosts(category.getPosts().size())
                .build();

    }
}
