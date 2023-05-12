package com.example.categories.service;

import com.example.categories.model.Category;

import java.util.List;

public interface CategoryService {

	List<String> getKeywordsByCategoryName(String categoryName);

	Integer findCategoryLevel(String categoryName);

	Category findById(Long categoryId);
}
