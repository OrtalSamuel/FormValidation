package com.example.validatorlibrary;

import androidx.annotation.Nullable;

public class Field {

    private final  String name;

    private final String placeholder;

    private final FieldType type;

    private boolean required;

    // Optional custom validator for the field
    @Nullable
    private FieldValidator fieldValidator;

    // Constructor to initialize a field without a custom validator
    public Field(String name, String placeholder, FieldType type, boolean required){
        this.name = name;
        this.placeholder = placeholder;
        this.type = type;
        this.required = required;
    }

    // Constructor to initialize a field without a custom validator
    public Field(String name, String placeholder, FieldType type,boolean required,@Nullable FieldValidator fieldValidator){
        this.name = name;
        this.placeholder = placeholder;
        this.type = type;
        this.fieldValidator = fieldValidator;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public Field setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public FieldType getType() {
        return type;
    }

    @Nullable
    public FieldValidator getFieldValidator() {
        return fieldValidator;
    }

    public Field setFieldValidator(@Nullable FieldValidator fieldValidator) {
        this.fieldValidator = fieldValidator;
        return this;
    }
}


