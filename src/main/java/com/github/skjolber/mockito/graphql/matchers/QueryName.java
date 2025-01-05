package com.github.skjolber.mockito.graphql.matchers;
import java.util.List;
import java.util.Map;

import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.OperationDefinition;
import graphql.language.OperationDefinition.Operation;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.language.VariableDefinition;

public class QueryName extends AbstractValueMatcher {

    public QueryName(String name, Map<String, Object> variables) {
		super(name, variables, Operation.QUERY);
	}

    @Override
    public String toString() {
        return "<query " + name + ">";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }

	@Override
	protected boolean match(OperationDefinition operationDefinition) {
		String operationName = operationDefinition.getName();
		if(operationName == null) {
			return matchSelectionSetName(operationDefinition);
		} else if(!name.equals(operationName)) {
			return false;
		}
		
		if(!variables.isEmpty()) {
			List<VariableDefinition> variableDefinitions = operationDefinition.getVariableDefinitions();
			
			if(variableDefinitions.size() != variables.size()) {
				return false;
			}
			
			for (VariableDefinition variableDefinition : variableDefinitions) {
				Object object = variables.get(variableDefinition.getName());
				
				if(object == null) {
					return false;
				}
				
				Type type = variableDefinition.getType();
				if(!matches(type, object)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean matches(Type type, Object object) {
		if(type instanceof NonNullType) {
			String string = object.toString();
			if(!string.endsWith("!")) {
				return false;
			}
			NonNullType nonNullType = (NonNullType)type;
			return matches(nonNullType.getType(), string.substring(0, string.length() - 1));
		}
		
		if(type instanceof ListType) {
			ListType listType = (ListType)type;
			return matches(listType.getType(), object);
		}
		if(type instanceof TypeName) {
			TypeName typeName = (TypeName)type;
			
			if(typeName.getName().equals(object)) {
				return true;
			}
			
			if(object instanceof Class) {
				if(Integer.class == object || Long.class == object || Short.class == object || Byte.class == object) {
					return typeName.getName().equals("Int");
				}
				if(String.class == object) {
					return typeName.getName().equals("String");
				}
				if(Boolean.class == object) {
					return typeName.getName().equals("Boolean");
				}
				if(Float.class == object || Double.class == object) {
					return typeName.getName().equals("Float");
				}				
			}
			
			return false;
		}
		
		return false;
	}
}
