package com.example.online_shop_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.api.RetrofitClient;
import com.example.online_shop_mobile.models.response.RegisterResponse;
import com.example.online_shop_mobile.storage.SharedPrefManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail, editTextPassword, editTextFName, editTextLName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFName = findViewById(R.id.editTextFName);
        editTextLName = findViewById(R.id.editTextLName);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonRegister).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userRegister() {
        String email = editTextEmail.getText().toString().trim();
        String fName = editTextFName.getText().toString().trim();
        String lName = editTextLName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 character long");
            editTextPassword.requestFocus();
            return;
        }

        if (fName.isEmpty()) {
            editTextFName.setError("First Name required");
            editTextFName.requestFocus();
            return;
        }

        if (lName.isEmpty()) {
            editTextLName.setError("Last Name required");
            editTextLName.requestFocus();
            return;
        }

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(email, password, fName, lName);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                System.out.println(response.body().getMessage());
                if (!response.body().isError()) {

//                    ResponseBody dr = response.body();
//                    System.out.println(response.body());
                    Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                userRegister();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
    }
}