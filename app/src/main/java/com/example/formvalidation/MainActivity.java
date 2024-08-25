package com.example.formvalidation;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.validatorlibrary.Field;
import com.example.validatorlibrary.FieldType;
import com.example.validatorlibrary.FieldValidator;
import com.example.validatorlibrary.Form;
import com.example.validatorlibrary.FormListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements FormListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout formLayout = findViewById(R.id.formLayout);

        List<Field> fields = new ArrayList<>();
        fields.add(new Field("name", "Enter name", FieldType.TEXT, true));
        fields.add(new Field("email", "Enter email address", FieldType.EMAIL, true));
        fields.add(new Field("password", "Enter password", FieldType.PASSWORD, true, new FieldValidator("\"password must be at-least 6 letter long\"") {
            @Override
            public boolean test(String s) {
                return s.length() >= 6;
            }
        }));



        Form form = new Form(fields, formLayout, this);

        form.create();




}

    @Override
    public void OnFormSubmit(Map<String, String> values) {
        String name = values.get("name");
        String email = values.get("email");
        String password = values.get("password");
        Toast.makeText(this, String.format("Name: %s, email:%s, password:%s", name,email,password),Toast.LENGTH_LONG).show();
    }
}