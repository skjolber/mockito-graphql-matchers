package com.github.skjolber.mockito.graphql.matchers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.json.JsonFactory;

public class GraphQLWrapper {

	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	
	public static String wrap(String query, String operationName, String variables) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(query.length() + 512);
		
		try (JsonGenerator generator = JSON_FACTORY.createGenerator(ObjectWriteContext.empty(), bout, JsonEncoding.UTF8)) {
			generator.writeStartObject();
			generator.writeStringProperty("query", query);
			generator.writeStringProperty("operationName", operationName);
			if(variables != null) {
				generator.writeName("variables");
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
