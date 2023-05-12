package com.example.categories.service.impl;

import com.example.categories.model.Category;
import com.example.categories.model.Keyword;
import com.example.categories.repository.CategoryRepository;
import com.example.categories.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<String> getKeywordsByCategoryName(String categoryName) {
		if (null == categoryName || "".equals(categoryName.trim())) {
			throw new RuntimeException("Category name cannot be empty or null.");
		}

		Optional<Category> optionalCategory = categoryRepository.findByDisplayNameIgnoreCase(categoryName);
		if (optionalCategory.isEmpty()) {
			return new ArrayList<>();
		}

		Category category = optionalCategory.get();

		List<Keyword> keywords = category.getKeywords();
		if (null == keywords || keywords.isEmpty()) {
			keywords = getParentKeywords(category.getParentId(), new ArrayList<>());
		}

		return keywords.stream()
			.map(Keyword::getName)
			.collect(Collectors.toList());
	}

	private List<Keyword> getParentKeywords(Category category, List<Keyword> keywords) {
		if (null == category) {
			return keywords;
		}

		List<Keyword> categoryKeywords = category.getKeywords();
		if (null != categoryKeywords && !categoryKeywords.isEmpty()) {
			keywords.addAll(category.getKeywords());
		}

		return getParentKeywords(category.getParentId(), keywords);
	}

	public Integer findCategoryLevel(String categoryName) {
		if (null == categoryName || "".equals(categoryName.trim())) {
			throw new RuntimeException("Category name cannot be empty or null.");
		}

		Optional<Category> optionalCategory = categoryRepository.findByDisplayNameIgnoreCase(categoryName);
		if (optionalCategory.isEmpty()) {
			throw new RuntimeException("Unable to find the category level for " + categoryName + ".");
		}

		int rootLevel = 1;
		Category category = optionalCategory.get();
		if (null == category.getParentId()) {
			return rootLevel;
		}

		return findCategoryLevel(category.getParentId().getId(), rootLevel + 1);
	}

	private Integer findCategoryLevel(Long categoryId, Integer level) {
		Category category = findById(categoryId);

		if (null == category.getParentId()) {
			return level;
		}

		return findCategoryLevel(category.getParentId().getId(), level + 1);
	}

	public Category findById(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isEmpty()) {
			throw new RuntimeException("Unable to find the category with id " + categoryId + ".");
		}

		return optionalCategory.get();
	}
}
