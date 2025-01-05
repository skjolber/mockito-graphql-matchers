package com.github.skjolber.mockito.graphql.matchers;

import java.io.Serializable;
import java.util.function.Function;

import org.mockito.ArgumentMatcher;

import graphql.language.Document;
import graphql.parser.Parser;

public class GraphQLDocument implements ArgumentMatcher<Object>, Serializable {

	private final Function<Document, Boolean> f;
	
	public GraphQLDocument(Function<Document, Boolean> f) {
		this.f = f;
	}

    @Override
    public boolean matches(Object actual) {
    	String query = null;
    	if(actual instanceof String) {
    		query = GraphQLParser.parseQuery((String) actual);
    	} else if(actual instanceof byte[]) {
    		query = GraphQLParser.parseQuery((byte[]) actual);
    	} else {
    		return false;
    	}
    	
    	if(query != null) {
    		Document document = Parser.parse(query);

    		return f.apply(document);
    	}
    	
        return false;
    }

    @Override
    public String toString() {
        return "<custom document matcher>";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }
    
}