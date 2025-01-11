package com.github.skjolber.mockito.graphql.matchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	
	@Test
	public void testQuotedQuery() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("first", 1);
		map.put("query", "name:#1234");
		
		QueryName matcher = new QueryName("orders", map);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("{ orders(first:1, query:\"name:#1234\") { name } }")));
	}
	
	@Test
	public void testMockito() throws IOException {
		GraphQLExchange exchange = mock(GraphQLExchange.class);
		
		when(exchange.exchange(ArgumentMatchers.queryName("shop"))).thenReturn("A");
		when(exchange.exchange(ArgumentMatchers.queryName("location"))).thenReturn("B");

		assertEquals("A", exchange.exchange(GraphQLWrapper.wrapQuery("{ shop(test:123) { name } }")));
		assertEquals("B", exchange.exchange(GraphQLWrapper.wrapQuery("{ location(test:123) { name } }")));
	}
	
	@Test
	public void testMockitoWithArguments() throws IOException {
		GraphQLExchange exchange = mock(GraphQLExchange.class);
		
		when(exchange.exchange(ArgumentMatchers.queryName("location", "id", 1))).thenReturn("A");
		when(exchange.exchange(ArgumentMatchers.queryName("location", "id", 2))).thenReturn("B");

		assertEquals("A", exchange.exchange(GraphQLWrapper.wrapQuery("{ location(id:1) { name } }")));
		assertEquals("B", exchange.exchange(GraphQLWrapper.wrapQuery("{ location(id:2) { name } }")));
	}


}
