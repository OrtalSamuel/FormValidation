package com.example.validatorlibrary;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Form {

    private List<Field> fields;

    private LinearLayout linearLayout;

    private final String submitButtonTitle;

    // Listener for form submission
    private final FormListener listener;


    // Map of field names to error messages
    private final Map<String,String> errors = new HashMap<>();

    // Map of field names to their current values
    private final Map<String, String> values = new HashMap<>();

    // Map of field names to their corresponding TextInputLayout and TextInputEditText views
    private final Map<String, Pair<TextInputLayout,  TextInputEditText>> fieldMap= new HashMap<>();

    private boolean isFormValid = false;

    public Form(List<Field> fields, LinearLayout linearLayout,FormListener listener, String submitButtonTitle){
        this.linearLayout = linearLayout;
        this.submitButtonTitle = submitButtonTitle;
        this.listener = listener;
        this.fields = fields;

        // Initialize errors for required fields or fields with custom validators
        for(Field filed : fields) {
            if(filed.isRequired() || filed.getFieldValidator()!= null){
                errors.put(filed.getName(), "N/A"); // "N/A" indicates not tested
            }

        }
    }

    //  constructor with a default submit button title
    public Form(List<Field> fields, LinearLayout linearLayout,FormListener listener){
        this.linearLayout = linearLayout;
        this.submitButtonTitle= "Submit";
        this.listener = listener;
        this.fields = fields;

        for(Field filed : fields) {
            if(filed.isRequired() || filed.getFieldValidator()!= null){
                errors.put(filed.getName(), "Not tested");
            }

        }
    }

    public void create(){
        linearLayout.removeAllViews(); // Clear existing views in the layout
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for(Field field: fields){
            TextInputLayout layout = new TextInputLayout(linearLayout.getContext());
            layout.setHint(field.getPlaceholder());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,12,0,12);

            TextInputEditText editText = new TextInputEditText(linearLayout.getContext());
            layout.addView(editText);
            linearLayout.addView(layout);

            if(field.getType() == FieldType.PASSWORD){
                layout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            else if(field.getType() == FieldType.EMAIL){
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            else if(field.getType() == FieldType.NUMBER){
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else if(field.getType() == FieldType.PHONE){
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            fieldMap.put(field.getName(), new Pair<>(layout, editText));

            // Add TextWatcher to handle real-time validation
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    String str = charSequence.toString();

                    boolean typeError = false;
                    if(field.getType() == FieldType.EMAIL){
                        if(!Patterns.EMAIL_ADDRESS.matcher(str).matches()){
                            layout.setError("Invalid email address");
                            errors.put(field.getName(),"Invalid email address");
                            typeError = true;
                        }
                        else {
                            errors.remove(field.getName());
                            layout.setError(null);
                        }
                    }

                    if(field.getType() == FieldType.PHONE){
                        if(!Patterns.PHONE.matcher(str).matches()){
                            layout.setError("Invalid Phone number");
                            errors.put(field.getName(),"Invalid Phone number");
                            typeError = true;
                        }
                        else {
                            errors.remove(field.getName());
                            layout.setError(null);
                        }
                    }
                    if(field.getFieldValidator() != null){
                        if(!field.getFieldValidator().test(str)){
                            layout.setError(field.getFieldValidator().getErrorMessage());
                            errors.put(field.getName(),field.getFieldValidator().getErrorMessage());

                        }
                        else {
                            errors.remove(field.getName());
                            layout.setError(null);
                        }
                    }else if(field.isRequired() && charSequence.length() == 0) {
                        layout.setError("Required field");
                        errors.put(field.getName(),"Required field");
                    }
                    else {
                        errors.remove(field.getName());
                        if(!typeError){
                            layout.setError(null);

                        }
                    }

                    if(errors.isEmpty()){
                        isFormValid = true;
                    }
                    values.put(field.getName(), charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        Button b = new Button(linearLayout.getContext());
        b.setText(submitButtonTitle);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,12,0,12);
        linearLayout.addView(b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFormValid){
                    for(Field field : fields) {
                        TextInputEditText editText = fieldMap.get(field.getName()).second;
                        if(field.isRequired() ){
                            if(editText.getText().toString().isEmpty()){
                                fieldMap.get(field.getName()).first.setError("Required filed");
                            }
                        }
                        if(field.getFieldValidator() != null){
                            if (!field.getFieldValidator().test(editText.getText().toString())) {
                                fieldMap.get(field.getName()).first.setError(field.getFieldValidator().getErrorMessage());
                            }
                        }
                    }

                }
                else {

                    listener.OnFormSubmit(values);
                }
            }

        });


    }

}
