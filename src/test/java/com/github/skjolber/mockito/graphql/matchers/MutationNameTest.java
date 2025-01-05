package com.github.skjolber.mockito.graphql.matchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import graphql.language.Definition;
import graphql.language.Document;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;

public class MutationNameTest {

	@Test
	public void test() throws IOException {
		MutationName matcher = new MutationName("setName", Collections.emptyMap());

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("mutation setName { setName(name: \"Homer\") { newName } }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("mutation setValue { setValue(name: \"Homer\") { newName } }")));
	}
	
	@Test
	public void testArguments() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("devicePicSize", "Int");

		MutationName matcher = new MutationName("setName", map);
		
		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("mutation setName ($devicePicSize: Int) { setName(input: $devicePicSize) }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("mutation setOtherName ($devicePicSize: Int) { setName(input: $devicePicSize) }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("mutation setName ($otherPicSize: Int) { setName(input: $otherPicSize) }")));
	}
	
	@Test
	public void testArgumentRequired() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("devicePicSize", "Int!");

		MutationName matcher = new MutationName("setName", map);
		
		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("mutation setName ($devicePicSize: Int!) { setName(input: $devicePicSize) }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("mutation setOtherName ($devicePicSize: Int!) { setName(input: $devicePicSize) }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("mutation setName ($otherPicSize: Int!) { setName(input: $otherPicSize) }")));
	}

	@Test
	public void testSelectionSet() throws IOException {
		MutationName matcher = new MutationName("setName", Collections.emptyMap());

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("mutation { setName(name: \"Homer\") { newName } }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("mutation { setValue(name: \"Homer\") { newName } }")));
	}

	@Test
	public void testSelectionSetArguments() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("test", 123);
		
		MutationName matcher = new MutationName("shop", map);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("mutation { shop(test:123) { name } }")));
	}

}
