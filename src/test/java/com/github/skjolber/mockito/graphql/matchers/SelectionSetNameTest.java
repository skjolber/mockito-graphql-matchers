package com.github.skjolber.mockito.graphql.matchers;

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
import graphql.language.OperationDefinition.Operation;
import graphql.parser.Parser;

public class SelectionSetNameTest {

	@Test
	public void test() throws IOException {
		SelectionSetName matcher = new SelectionSetName("shop", Collections.emptyMap(), Operation.QUERY);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("{ shop { name } }")));
	}
	
	@Test
	public void testArguments() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("test", 123);
		
		SelectionSetName matcher = new SelectionSetName("shop", map, Operation.QUERY);

		assertTrue(matcher.matches(GraphQLWrapper.wrapQuery("{ shop(test:123) { name } }")));
	}

}
