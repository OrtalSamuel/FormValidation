package com.example.validatorlibrary;

import java.util.function.Predicate;

// Abstract class for custom field validation
public abstract class FieldValidator implements Predicate<String> {

    // Error message to display if validation fails
    private final String errorMessage;

    public FieldValidator(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // The `test` method must be implemented by subclasses to define validation logic
}
