package com.example.categories.service;

import com.example.categories.model.Category;
import com.example.categories.model.Keyword;
import com.example.categories.repository.CategoryRepository;
import com.example.categories.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

	private static CategoryService service;

	private static CategoryRepository repository;

	@BeforeAll
	public static void init() {
		repository = Mockito.mock(CategoryRepository.class);
		service = new CategoryServiceImpl(repository);
	}

	@Test
	public void shouldThrowExceptionOnEmptyCategory() {
		// Arrange
		String categoryName = "";

		// Act and Assert
		assertThrows(RuntimeException.class, () -> service.getKeywordsByCategoryName(categoryName));
	}

	@Test
	public void shouldThrowExceptionOnNullCategory() {
		// Arrange
		String categoryName = null;

		// Act and Assert
		assertThrows(RuntimeException.class, () -> service.getKeywordsByCategoryName(categoryName));
	}

	@Test
	public void shouldReturnAnEmptyListIfCategoryNotFound() {
		// Arrange
		String categoryName = "Invalid";
		when(repository.findByDisplayNameIgnoreCase(categoryName)).thenReturn(Optional.empty());

		// Act
		List<String> keywords = service.getKeywordsByCategoryName(categoryName);

		// Assert
		assertEquals(new ArrayList<>(), keywords);
		assertEquals(0, keywords.size());
	}

	@Test
	public void shouldReturnAListOfKeywords() {
		// Arrange
		String categoryName = "Home Appliances";

		Keyword keyword1 = new Keyword();
		keyword1.setName("Major Appliances");

		Keyword keyword2 = new Keyword();
		keyword2.setName("Minor Appliances");

		Category category = new Category();
		category.setKeywords(Arrays.asList(keyword1, keyword2));

		when(repository.findByDisplayNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));

		// Act
		List<String> keywords = service.getKeywordsByCategoryName(categoryName);

		// Assert
		assertEquals(2, keywords.size());
		assertEquals("Major Appliances", keywords.get(0));
		assertEquals("Minor Appliances", keywords.get(1));
	}

	@Test
	public void shouldReturnAListOfKeywordsFromParent() {
		// Arrange
		String categoryName = "Home Appliances";

		Keyword keyword1 = new Keyword();
		keyword1.setName("Major Appliances");

		Keyword keyword2 = new Keyword();
		keyword2.setName("Minor Appliances");

		Category parentCategory = new Category();
		parentCategory.setKeywords(Arrays.asList(keyword1, keyword2));

		Category category = new Category();
		category.setParentId(parentCategory);

		when(repository.findByDisplayNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));

		// Act
		List<String> keywords = service.getKeywordsByCategoryName(categoryName);

		// Assert
		assertEquals(2, keywords.size());
		assertEquals("Major Appliances", keywords.get(0));
		assertEquals("Minor Appliances", keywords.get(1));
	}

	@Test
	public void shouldThrowExceptionWhenEmptyCategoryOnLevel() {
		// Arrange
		String categoryName = "";

		// Act and Assert
		assertThrows(RuntimeException.class, () -> service.findCategoryLevel(categoryName));
	}

	@Test
	public void shouldThrowExceptionWhenNullCategoryOnLevel() {
		// Arrange
		String categoryName = null;

		// Act and Assert
		assertThrows(RuntimeException.class, () -> service.findCategoryLevel(categoryName));
	}

	@Test
	public void shouldThrowExceptionWhenCategoryDoesNotExists() {
		// Arrange
		String categoryName = "Invalid";
		when(repository.findByDisplayNameIgnoreCase(categoryName)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(RuntimeException.class, () -> service.findCategoryLevel(categoryName));
	}

	@Test
	public void shouldReturnRootLevelWhenCategoryDoesNotHaveParent() {
		// Arrange
		String categoryName = "Home Appliances";
		Category category = new Category();

		when(repository.findByDisplayNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));

		// Act
		Integer categoryLevel = service.findCategoryLevel(categoryName);

		// Assert
		assertEquals(1, categoryLevel);
	}

	@Test
	public void shouldReturnSecondLevelWhenCategoryHasAParent() {
		// Arrange
		Category parentCategory = new Category();
		parentCategory.setId(1L);

		String categoryName = "Major Appliances";
		Category category = new Category();
		category.setParentId(parentCategory);

		when(repository.findByDisplayNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));
		when(repository.findById(parentCategory.getId())).thenReturn(Optional.of(parentCategory));

		// Act
		Integer categoryLevel = service.findCategoryLevel(categoryName);

		// Assert
		assertEquals(2, categoryLevel);
	}
}
