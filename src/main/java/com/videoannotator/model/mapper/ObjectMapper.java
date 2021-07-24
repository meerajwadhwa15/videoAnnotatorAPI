package com.videoannotator.model.mapper;

import com.videoannotator.model.Category;
import com.videoannotator.model.SubCategory;
import com.videoannotator.model.response.CategoryResponse;
import com.videoannotator.model.response.SubCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class ObjectMapper {
    public static final ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);
    public abstract List<CategoryResponse> categoryToResponse(List<Category> categories);
    public abstract List<SubCategoryResponse> subCategoryToResponse (List<SubCategory> subCategory);
}
