package com.github.skjolber.mockito.graphql.matchers;

import static com.github.skjolber.mockito.graphql.matchers.ArgumentMatchers.queryName;
import static com.github.skjolber.mockito.graphql.matchers.ArgumentMatchers.selectionSetName;
import static com.github.skjolber.mockito.graphql.matchers.ArgumentMatchers.mutationName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import graphql.language.OperationDefinition.Operation;

public class ArgumentMatchersTest {

	interface Http {
		String exchange(String request);
	}
	
	@Test
	public void testQuery() throws IOException {
		Http http = mock(Http.class);
		
		when(http.exchange(queryName("getProfile"))).thenReturn("{ ... }");
		
		http.exchange(GraphQLWrapper.wrapQuery("query getProfile($devicePicSize: Int!){ me }"));
		
		ArgumentCaptor<String> captor = ArgumentCaptor.captor();
		
		verify(http).exchange(captor.capture());
		
		String value = captor.getValue();
		assertEquals(value, "{\"query\":\"query getProfile($devicePicSize: Int!){ me }\",\"operationName\":\"mockOperationName\"}");
	}
	
	@Test
	public void testDualQuery() throws IOException {
		Http http = mock(Http.class);
		
		when(http.exchange(queryName("getUser"))).thenReturn("getUser");
		when(http.exchange(queryName("getProfile"))).thenReturn("getProfile");
		
		String exchange1 = http.exchange(GraphQLWrapper.wrapQuery("query getProfile($devicePicSize: Int!){ me }"));
		assertEquals(exchange1, "getProfile");
		String exchange2 = http.exchange(GraphQLWrapper.wrapQuery("query getUser($devicePicSize: Int!){ me }"));
		assertEquals(exchange2, "getUser");
	}
	
	@Test
	public void testDualMutation() throws IOException {
		Http http = mock(Http.class);
		
		when(http.exchange(mutationName("setName"))).thenReturn("setName");
		when(http.exchange(mutationName("setUser"))).thenReturn("setUser");
		
		String exchange1 = http.exchange(GraphQLWrapper.wrapQuery("mutation setName { setName(name: \"Homer\") { newName } }"));
		assertEquals(exchange1, "setName");
		String exchange2 = http.exchange(GraphQLWrapper.wrapQuery("mutation setUser { setUser(name: \"Homer\") { newName } }"));
		assertEquals(exchange2, "setUser");
	}
	
	@Test
	public void testSelection() throws IOException {
		Http http = mock(Http.class);
		
		when(http.exchange(selectionSetName("shop", Operation.QUERY))).thenReturn("{ ... }");
		
		http.exchange(GraphQLWrapper.wrapQuery("{ shop { name } }"));
		
		ArgumentCaptor<String> captor = ArgumentCaptor.captor();
		
		verify(http).exchange(captor.capture());
		
		String value = captor.getValue();
		assertEquals(value, "{\"query\":\"{ shop { name } }\",\"operationName\":\"mockOperationName\"}");
	}

}
