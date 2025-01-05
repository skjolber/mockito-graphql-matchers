package com.github.skjolber.mockito.graphql.matchers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class GraphQLParser {

	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	
	public static String parseQuery(String content) {
		try (JsonParser parser = JSON_FACTORY.createParser(content)) {
			return parseQuery(parser);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String parseQuery(byte[] content) {
		try (JsonParser parser = JSON_FACTORY.createParser(content)) {
			return parseQuery(parser);
		} catch(Exception e) {
			return null;
		}
	}

	public static String parseQuery(JsonParser parser) throws IOException {
		JsonToken nextToken = parser.nextToken();
		if(nextToken != JsonToken.START_OBJECT) {
			throw new IllegalStateException();
		}
			
		int level = 1;
		
		do {
			nextToken = parser.nextToken();
			if(nextToken == JsonToken.START_OBJECT) {
				level++;
			} else if(nextToken == JsonToken.END_OBJECT) {
				level--;
			} else {
				if(level == 1) {
					if(nextToken == JsonToken.FIELD_NAME) {
						String name = parser.currentName();
						if(name.equals("query")) {
							if(parser.nextToken() == JsonToken.VALUE_STRING) {
								return parser.getText();
							} else {
								break;
							}
						}
					}
				}
			}
		} while(level > 0);
		
		return null;
	}
	
	public static String parseOperationName(String content) {
		try (JsonParser parser = JSON_FACTORY.createParser(content)) {
			return parseOperationName(parser);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String parseOperationName(byte[] content) {
		try (JsonParser parser = JSON_FACTORY.createParser(content)) {
			return parseOperationName(parser);
		} catch(Exception e) {
			return null;
		}
	}

	public static String parseOperationName(JsonParser parser) throws IOException {
		JsonToken nextToken = parser.nextToken();
		if(nextToken != JsonToken.START_OBJECT) {
			throw new IllegalStateException();
		}
			
		int level = 1;
		
		do {
			nextToken = parser.nextToken();
			if(nextToken == JsonToken.START_OBJECT) {
				level++;
			} else if(nextToken == JsonToken.END_OBJECT) {
				level--;
			} else {
				if(level == 1) {
					if(nextToken == JsonToken.FIELD_NAME) {
						String name = parser.currentName();
						if(name.equals("operationName")) {
							if(parser.nextToken() == JsonToken.START_OBJECT) {
								return parser.getText();
							} else {
								break;
							}
						}
					}
				}
			}
		} while(level > 0);
		
		return null;
	}
	
	public static JsonParser parseVariables(String content) {
		try {
			JsonParser parser = JSON_FACTORY.createParser(content);
			
			return parseVariables(parser);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static JsonParser parseVariables(byte[] content) {
		try {
			JsonParser parser = JSON_FACTORY.createParser(content);
			
			return parseVariables(parser);
		} catch(Exception e) {
			return null;
		}
	}

	public static JsonParser parseVariables(JsonParser parser) throws IOException {
		JsonToken nextToken = parser.nextToken();
		if(nextToken != JsonToken.START_OBJECT) {
			throw new IllegalStateException();
		}
			
		int level = 1;
		
		do {
			nextToken = parser.nextToken();
			if(nextToken == JsonToken.START_OBJECT) {
				level++;
			} else if(nextToken == JsonToken.END_OBJECT) {
				level--;
			} else {
				if(level == 1) {
					if(nextToken == JsonToken.FIELD_NAME) {
						String name = parser.currentName();
						if(name.equals("variables")) {
							if(parser.nextToken() == JsonToken.START_OBJECT) {
								return parser;
							} else {
								break;
							}
						}
					}
				}
			}
		} while(level > 0);
		
		return null;
	}
	
	public static boolean isGrapQL(String content) {
		try(JsonParser parser = JSON_FACTORY.createParser(content)) {
			return isGrapQL(parser);
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean isGrapQL(byte[] content) {
		try(JsonParser parser = JSON_FACTORY.createParser(content)){
			return isGrapQL(parser);
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean isGrapQL(JsonParser parser) throws IOException {
		JsonToken nextToken = parser.nextToken();
		if(nextToken != JsonToken.START_OBJECT) {
			throw new IllegalStateException();
		}
			
		boolean query = false;
		boolean operationName = false;
		
		// variables is optional
		
		int level = 1;
		
		do {
			nextToken = parser.nextToken();
			if(nextToken == JsonToken.START_OBJECT) {
				level++;
			} else if(nextToken == JsonToken.END_OBJECT) {
				level--;
			} else {
				if(level == 1) {
					if(nextToken == JsonToken.FIELD_NAME) {
						String name = parser.currentName();
						switch(name) {
							case "query": {
								if(operationName) {
									return true;
								}
								query = true;
								break;
							}
							case "operationName": {
								if(query) {
									return true;
								}
								operationName = true;
								break;
							}
						}
					}
				}
			}
		} while(level > 0);
		
		return false;
	}

}
