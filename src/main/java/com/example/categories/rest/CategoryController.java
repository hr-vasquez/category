package com.example.categories.rest;

import com.example.categories.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@RequestMapping(
		value = "/keywords",
		method = RequestMethod.GET
	)
	public ResponseEntity<List<String>> getKeywords(@RequestParam String category) {
		List<String> keywords = categoryService.getKeywordsByCategoryName(category);
		return ResponseEntity.ok(keywords);
	}

	@RequestMapping(
		value = "/level",
		method = RequestMethod.GET
	)
	public ResponseEntity<Integer> getCategoryLevel(@RequestParam String category) {
		Integer categoryLevel = categoryService.findCategoryLevel(category);
		return ResponseEntity.ok(categoryLevel);
	}
}
