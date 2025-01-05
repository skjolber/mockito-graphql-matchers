package com.github.skjolber.mockito.graphql.matchers;
import java.io.Serializable;

import org.mockito.ArgumentMatcher;

public class AnyGraphQL implements ArgumentMatcher<Object>, Serializable {

    public static final AnyGraphQL ANY = new AnyGraphQL();

    @Override
    public boolean matches(Object actual) {
    	if(actual instanceof String) {
    		return GraphQLParser.isGrapQL((String) actual);
    	} else if(actual instanceof byte[]) {
    		return GraphQLParser.isGrapQL((byte[]) actual);
    	} else {
    		return false;
    	}
    }

    @Override
    public String toString() {
        return "<any graphql>";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }
}
