package com.example.lenovo.myapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private Button login;
    boolean flag = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<User> arrayList = new ArrayList<>();
    private EditText email, password;
    TextView link_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        link_signup = findViewById(R.id.link_signup);
        login = findViewById(R.id.button2);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("user");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    User user = userSnapShot.getValue(User.class);
                    arrayList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sharedPreferences = getSharedPreferences(AskForSignin.My_pref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login_edittext);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_login = email.getText().toString();
                String pass_login = password.getText().toString();
                for (int i = 0; i < arrayList.size(); i++) {
                    User user = arrayList.get(i);
                    String email_register = user.getEmail();
                    String password_register = user.getPassword();
                    if (email_login.equals(email_register) && pass_login.equals(password_register)) {
                        Intent intent = new Intent(Login.this, Home_page.class);
                        intent.putExtra("Login", "email");
                        editor.putBoolean("Email", true);
                        editor.commit();
                        startActivity(intent);
                        flag = true;
                    }
                }
                if (!flag) {
                    Toast.makeText(Login.this, "Enter valid Email and Password ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}
