package com.videoannotator.service.impl;

import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.Category;
import com.videoannotator.model.mapper.ObjectMapper;
import com.videoannotator.model.response.CategoryResponse;
import com.videoannotator.model.response.SubCategoryResponse;
import com.videoannotator.repository.CategoryRepository;
import com.videoannotator.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> listCategory() {
        List<Category> categories = categoryRepository.findAll();
        return ObjectMapper.INSTANCE.categoryToListResponse(categories);
    }

    @Override
    public List<SubCategoryResponse> listSubCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);
        return ObjectMapper.INSTANCE.subCategoryToListResponse(category.getSubCategoryList());
    }
}
