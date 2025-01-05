package com.github.skjolber.mockito.graphql.matchers;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.mockito.ArgumentMatcher;

import graphql.language.Document;
import graphql.language.OperationDefinition.Operation;

public class ArgumentMatchers {

	
    public static <T> T anyQuery() {
        reportMatcher(AnyQuery.ANY);
        return null;
    }

    public static <T> T anyMutation() {
        reportMatcher(AnyMutation.ANY);
        return null;
    }
    
    public static <T> T anyGraphQL() {
        reportMatcher(AnyGraphQL.ANY);
        return null;
    }

    public static <T> T queryName(String name, Object ... arguments) {
        reportMatcher(new QueryName(name, toMap(arguments)));
        return null;
    }
    
    public static <T> T mutationName(String name, Object ... arguments) {
        reportMatcher(new MutationName(name, toMap(arguments)));
        return null;
    }
    
    public static <T> T selectionSetName(String name, Operation operation, Object ... arguments) {
        reportMatcher(new SelectionSetName(name, toMap(arguments), operation));
        return null;
    }
    
    public static <T> T document(Function<Document, Boolean> decider) {
        reportMatcher(new GraphQLDocument(decider));
        return null;
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
    
    private static Map<String, Object> toMap(Object[] arguments) {
    	if(arguments != null && arguments.length > 0) {
        	Map<String, Object> result = new HashMap<>();
        	
        	if(arguments.length % 2 != 0) {
        		throw new IllegalArgumentException("Expected key-value pairs");
        	}
        	
        	for (int i = 0; i < arguments.length; i+= 2) {
				Object key = arguments[i];
				Object value = arguments[i + 1];
				if(key instanceof String) {
					result.put((String)key, value);
				} else {
	        		throw new IllegalArgumentException("Expected String key");
				}
			}
    		
        	return result;
    	}
    	return Collections.emptyMap();
    }
}
