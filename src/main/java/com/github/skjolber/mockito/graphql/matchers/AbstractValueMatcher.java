package com.github.skjolber.mockito.graphql.matchers;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.ArgumentMatcher;

import graphql.language.Argument;
import graphql.language.ArrayValue;
import graphql.language.BooleanValue;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.NullValue;
import graphql.language.OperationDefinition;
import graphql.language.OperationDefinition.Operation;
import graphql.language.SelectionSet;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.parser.Parser;

public abstract class AbstractValueMatcher implements ArgumentMatcher<Object>, Serializable {

    protected final String name;
    protected final Map<String, Object> variables;
    protected final Operation operation;
    
    public AbstractValueMatcher(String name, Map<String, Object> variables, Operation operation) {
    	this.name = name;
    	this.variables = variables;
		this.operation = operation;
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
    		
    		Optional<OperationDefinition> optional = document.getFirstDefinitionOfType(OperationDefinition.class);
    		if(!optional.isPresent()) {
    			return false;
    		}
    		OperationDefinition operationDefinition = optional.get();
    		if(operationDefinition.getOperation() != operation) {
    			return false;
    		}

    		return match(operationDefinition);
    	}
    	
        return false;
    }
    
    protected abstract boolean match(OperationDefinition operationDefinition);

	protected boolean matchSelectionSetName(OperationDefinition operationDefinition) {
		SelectionSet selectionSet = operationDefinition.getSelectionSet();
		List<Field> selectionsOfType = selectionSet.getSelectionsOfType(graphql.language.Field.class);
		
		for (Field field : selectionsOfType) {
			if(field.getName().equals(this.name)) {
				
				if(!variables.isEmpty()) {
					
					List<Argument> arguments = field.getArguments();
					if(arguments.size() != variables.size()) {
						return false;
					}
					for (Argument argument : arguments) {
						Value value = argument.getValue();
						
						if(value instanceof ArrayValue) {
    						Object object = variables.get(argument.getName());

							ArrayValue v = (ArrayValue)value;
							List<Value> values = v.getValues();
							
							if(object instanceof Object[]) {

								Object[] a = (Object[])object;
								if(a.length != values.size()) {
									return false;
								}
								for(int i = 0; i < a.length; i++) {
	    							if(!equals(a[i], values.get(i))) {
	    								return false;
	    							}
								}
							} else if(object instanceof List) {
								List<?> a = (List<?>)object;
								if(a.size() != values.size()) {
									return false;
								}
								for(int i = 0; i < a.size(); i++) {
	    							if(!equals(a.get(i), values.get(i))) {
	    								return false;
	    							}
								}
							} else {
								return false;
							}
						} else {
    						Object object = variables.get(argument.getName());

							if(!equals(object, value)) {
								return false;
							}
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	

	protected boolean equals(Object object, Value value) {
		if(object == null) {
			return value instanceof NullValue;
		}
		if(value instanceof BooleanValue) {
			BooleanValue v = (BooleanValue)value;
			return object.equals(v.isValue());
		}
		if(value instanceof FloatValue) {
			FloatValue v = (FloatValue)value;
			return object.equals(v.getValue());
		}
		if(value instanceof IntValue) {
			IntValue v = (IntValue)value;
			Number n = (Number)object;
			BigInteger other = BigInteger.valueOf(n.longValue());
			
			return other.equals(v.getValue());
		}
		if(value instanceof StringValue) {
			StringValue stringValue = (StringValue)value;
			return object.equals(stringValue.getValue());
		}

		if(value instanceof ArrayValue) {
			ArrayValue v = (ArrayValue)value;
			
			List<Value> values = v.getValues();
			
			if(object instanceof Object[]) {

				Object[] a = (Object[])object;
				if(a.length != values.size()) {
					return false;
				}
				for(int i = 0; i < a.length; i++) {
					if(!equals(a[i], values.get(i))) {
						return false;
					}
				}
			} else if(object instanceof List) {
				List<?> a = (List<?>)object;
				if(a.size() != values.size()) {
					return false;
				}
				for(int i = 0; i < a.size(); i++) {
					if(!equals(a.get(i), values.get(i))) {
						return false;
					}
				}
			} else {
				return false;
			}		
		}
		return false;
    }
}
