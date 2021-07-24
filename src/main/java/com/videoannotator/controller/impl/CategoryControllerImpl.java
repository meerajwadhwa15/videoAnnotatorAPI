package com.videoannotator.controller.impl;

import com.videoannotator.controller.ICategoryController;
import com.videoannotator.model.response.CategoryResponse;
import com.videoannotator.model.response.SubCategoryResponse;
import com.videoannotator.service.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
@CrossOrigin
@AllArgsConstructor
public class CategoryControllerImpl implements ICategoryController {
    private final ICategoryService categoryService;

    @Override
    public ResponseEntity<List<CategoryResponse>> listCategory() {
        List<CategoryResponse> responses = categoryService.listCategory();
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<SubCategoryResponse>> listSubCategory(Long categoryId) {
        List<SubCategoryResponse> responses = categoryService.listSubCategory(categoryId);
        return ResponseEntity.ok(responses);
    }
}
