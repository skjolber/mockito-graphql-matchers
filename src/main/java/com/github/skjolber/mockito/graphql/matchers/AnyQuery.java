package com.github.skjolber.mockito.graphql.matchers;
import java.io.Serializable;

import org.mockito.ArgumentMatcher;

public class AnyQuery implements ArgumentMatcher<Object>, Serializable {

    public static final AnyQuery ANY = new AnyQuery();

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
    		if(query.startsWith("query ")) {
    			return true;
    		}
    	}
    	
        return false;
    }

    @Override
    public String toString() {
        return "<any query>";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }
}
