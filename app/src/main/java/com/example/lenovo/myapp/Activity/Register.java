package com.example.lenovo.myapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myapp.Classes.User;
import com.example.lenovo.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private String id, firstName_register, lastName_register, mobile_register, email_register, password_register, confirmPass_register;
    private EditText firstName, lastName;
    private EditText mobile;
    private EditText email;
    private EditText password, confirmPass;
    private Button register;
    private CheckBox checkbox;
    private DatabaseReference mref;
    private TextView alredyRegister;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mref = FirebaseDatabase.getInstance().getReference("user");

        firstName = (EditText) findViewById(R.id.first_name_edittext);
        lastName = (EditText) findViewById(R.id.last_name_edittext);
        mobile = (EditText) findViewById(R.id.mobile_number_edittext);
        email = (EditText) findViewById(R.id.email_editText);
        password = (EditText) findViewById(R.id.password_edittext);
        confirmPass = (EditText) findViewById(R.id.confirm_password_edittext);
        checkbox = (CheckBox) findViewById(R.id.checkedTextView);
        register = (Button) findViewById(R.id.registeration_button);
        alredyRegister = (TextView) findViewById(R.id.alredy_register);

        alredyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName_register = firstName.getText().toString();
                lastName_register = lastName.getText().toString();
                mobile_register = mobile.getText().toString();
                email_register = email.getText().toString();
                password_register = password.getText().toString();
                confirmPass_register = confirmPass.getText().toString();
                if (firstName_register.matches("[a-zA-Z]+") && firstName_register.length() > 0) {
                    if (lastName_register.matches("[a-zA-Z]+") && lastName_register.length() > 0) {
                        if (mobile_register.matches("[0-9]+") && mobile_register.length() == 10) {
                            if (email_register.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && email_register.length() > 0) {
                                if (password_register.length() > 6 && password_register.length() < 15) {
                                    if (confirmPass_register.equals(password_register)) {
                                        mref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    User userData = dataSnapshot1.getValue(User.class);
                                                    if (userData.getMobileNo() == Long.parseLong(mobile_register)) {
                                                        flag = true;
                                                    }
                                                }
                                                if (!flag) {

                                                    Intent intent = new Intent(Register.this, OneTimePass.class);
                                                    intent.putExtra("FirstName", firstName_register);
                                                    intent.putExtra("LastName", lastName_register);
                                                    intent.putExtra("Number", mobile_register);
                                                    intent.putExtra("Email", email_register);
                                                    intent.putExtra("Password", password_register);
                                                    intent.putExtra("ConfirmPassword", confirmPass_register);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Toast.makeText(Register.this, "Already register", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(Register.this, "Both password not match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Register.this, "Password length must be between 7 and 14", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register.this, "Wrong input in Email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "Wrong input in MObile no", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Wrong input in Last name", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Register.this, "Wrong input in First name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
