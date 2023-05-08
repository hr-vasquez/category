package com.example.categories.service;

import com.example.categories.model.Category;
import com.example.categories.model.Keyword;
import com.example.categories.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
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
		return category.getKeywords().stream()
			.map(Keyword::getName)
			.collect(Collectors.toList());
	}

	public Integer findCategoryLevel(String categoryName) {
		if (null == categoryName || "".equals(categoryName.trim())) {
			throw new RuntimeException("Category name cannot be empty or null.");
		}

		Optional<Category> optionalCategory = categoryRepository.findByDisplayNameIgnoreCase(categoryName);
		if (optionalCategory.isEmpty()) {
			throw new RuntimeException("Unable to find the category level for " + categoryName + ".");
		}

		Category category = optionalCategory.get();
		if (null == category.getParentId()) {
			return 1;
		}

		return findCategoryLevel(category.getParentId().getId(), 2);

	}

	private Integer findCategoryLevel(Long categoryId, Integer level) {
		Category category = findById(categoryId);

		if (null == category.getParentId()) {
			return level;
		}

		return findCategoryLevel(category.getParentId().getId(), level + 1);
	}

	private Category findById(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isEmpty()) {
			throw new RuntimeException("Unable to find the category with id " + categoryId + ".");
		}

		return optionalCategory.get();
	}
}
