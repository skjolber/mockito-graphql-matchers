package com.github.skjolber.mockito.graphql.matchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class QueryNameTest {

	@Test
	public void test() throws IOException {
		QueryName matcher = new QueryName("getUserProfile", Collections.emptyMap());

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("query getUserProfile($devicePicSize: Int){ me }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("query getOtherProfile($devicePicSize: Int){ me }")));
	}

	@Test
	public void testArguments() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("devicePicSize", "Int");

		QueryName matcher = new QueryName("getUserProfile", map);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("query getUserProfile($devicePicSize: Int){ me }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("query getOtherProfile($devicePicSize: Int){ me }")));
		assertFalse(matcher.matches(GraphQLWrapper.wrapQuery("query getUserProfile($other: Int){ me }")));
	}
	
	@Test
	public void testArgumentRequired() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("devicePicSize", "Int!");

		QueryName matcher = new QueryName("getProfile", map);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("query getProfile($devicePicSize: Int!){ me }")));
	}
	
	@Test
	public void testSelectionSet() throws IOException {
		QueryName matcher = new QueryName("shop", Collections.emptyMap());

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("{ shop { name } }")));
	}
	
	@Test
	public void testSelectionSetArguments() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("test", 123);
		
		QueryName matcher = new QueryName("shop", map);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("{ shop(test:123) { name } }")));
	}

}
