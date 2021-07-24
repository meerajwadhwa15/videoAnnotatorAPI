package com.videoannotator.service;

import com.videoannotator.model.response.CategoryResponse;
import com.videoannotator.model.response.SubCategoryResponse;

import java.util.List;

/**
 * Service to CRUD category and sub-category
 */
public interface ICategoryService {
    /**
     * Get list category
     *
     * @return List(category)         - List category response
     */
    List<CategoryResponse> listCategory();

    /**
     * Get list sub-category
     *
     * @return List(sub-category)         - List sub-category response
     */
    List<SubCategoryResponse> listSubCategory(Long categoryId);
}
