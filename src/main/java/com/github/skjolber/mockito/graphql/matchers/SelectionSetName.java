package com.github.skjolber.mockito.graphql.matchers;
import java.util.Map;

import graphql.language.OperationDefinition;
import graphql.language.OperationDefinition.Operation;

public class SelectionSetName extends AbstractValueMatcher {

    public SelectionSetName(String name, Map<String, Object> variables, Operation operation) {
		super(name, variables, operation);
	}

    @Override
    public String toString() {
        return "<selection set " + name + ">";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }

	@Override
	protected boolean match(OperationDefinition operationDefinition) {
		return matchSelectionSetName(operationDefinition);
	}
	
}
