package com.github.skjolber.mockito.graphql.matchers;
import java.io.Serializable;

import org.mockito.ArgumentMatcher;

public class AnyMutation implements ArgumentMatcher<Object>, Serializable {

    public static final AnyMutation ANY = new AnyMutation();

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
    		if(query.startsWith("mutation ")) {
    			return true;
    		}
    	}
    	
        return false;
    }

    @Override
    public String toString() {
        return "<any mutation>";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }
}
