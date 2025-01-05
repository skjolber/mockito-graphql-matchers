package com.github.skjolber.mockito.graphql.matchers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class GraphQLWrapper {

	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	
	public static String wrap(String query, String operationName, String variables) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(query.length() + 512);
		
		try (JsonGenerator generator = JSON_FACTORY.createGenerator(bout)) {
			generator.writeStartObject();
			generator.writeStringField("query", query);
			generator.writeStringField("operationName", operationName);
			if(variables != null) {
				generator.writeFieldName("variables");
				generator.writeRawValue(variables);
			}
			generator.writeEndObject();
		}
		
		return bout.toString("UTF-8");
	}

	public static String wrapQuery(String query) throws IOException {
		return wrap(query, "mockOperationName", null);
	}

	public static String optionallyWrapQuery(String content) throws IOException {
		if(smellsLikeJson(content)) {
			return content;
		}
		return wrap(content, "mockOperationName", null);
	}

	public static boolean smellsLikeJson(String content) {
		boolean start = false;
		for(int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			if(Character.isWhitespace(c)) {
				continue;
			}
			if(c != '{') {
				return false;
			}
			start = true;
			
			break;
		}

		boolean end = false;
		for(int i = content.length() - 1; i >= 0; i++) {
			char c = content.charAt(i);
			if(Character.isWhitespace(c)) {
				continue;
			}
			if(c != '}') {
				return false;
			}
			end = true;
			break;
		}
		return start && end;
	}
}
