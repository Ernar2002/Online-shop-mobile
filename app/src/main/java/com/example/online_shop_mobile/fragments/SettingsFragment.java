package com.example.online_shop_mobile.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.activities.LoginActivity;
import com.example.online_shop_mobile.activities.MainActivity;
import com.example.online_shop_mobile.api.RetrofitClient;
import com.example.online_shop_mobile.models.response.DefaultResponse;
import com.example.online_shop_mobile.models.User;
import com.example.online_shop_mobile.storage.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText editTextFName, editTextLName, editTextCity, editTextAddress;
    private EditText editTextCurrentPassword, editTextNewPassword;
    private GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextFName = view.findViewById(R.id.editTextFName);
        editTextLName = view.findViewById(R.id.editTextLName);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextAddress = view.findViewById(R.id.editTextAddress);

        editTextCurrentPassword = view.findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        if (user.getFName() != null) {
            editTextFName.setText(user.getFName());
        }
        if (user.getLName() != null) {
            editTextLName.setText(user.getLName());
        }
        if (user.getCity() != null) {
            editTextCity.setText(user.getCity());
        }
        if (user.getAddress() != null) {
            editTextAddress.setText(user.getAddress());
        }

        view.findViewById(R.id.buttonSave).setOnClickListener(this);
        view.findViewById(R.id.buttonChangePassword).setOnClickListener(this);
        view.findViewById(R.id.buttonLogout).setOnClickListener(this);
        view.findViewById(R.id.buttonDelete).setOnClickListener(this);
    }

    private void updateProfile() {
        String firstName = editTextFName.getText().toString().trim();
        String lastName = editTextLName.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<User> call = RetrofitClient.getInstance()
                .getApi()
                .updateUser(
                        user.getEmail(),
                        firstName,
                        lastName,
                        city,
                        address
                );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

//                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    User updatedUser = new User(response.body().getId(), response.body().getEmail(),
                            response.body().getFName(),
                            response.body().getLName(),
                            response.body().getCity(),
                            response.body().getAddress());
//                if (!response.body().isError()) {
                    SharedPrefManager.getInstance(getActivity()).saveUser(updatedUser);
//                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void updatePassword() {
        String currentpassword = editTextCurrentPassword.getText().toString().trim();
        String newpassword = editTextNewPassword.getText().toString().trim();

        if (currentpassword.isEmpty()) {
            editTextCurrentPassword.setError("Password required");
            editTextCurrentPassword.requestFocus();
            return;
        }

        if (newpassword.isEmpty()) {
            editTextNewPassword.setError("Enter new password");
            editTextNewPassword.requestFocus();
            return;
        }


        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi()
                .updatePassword(currentpassword, newpassword, user.getEmail());

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void logout() {
        SharedPrefManager.getInstance(getActivity()).clear();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        if(gso != null){
            signOut();
        }

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void deleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?");
        builder.setMessage("This action is irreversible...");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = SharedPrefManager.getInstance(getActivity()).getUser();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteUser(user.getId());

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                        if (!response.body().isErr()) {
                            SharedPrefManager.getInstance(getActivity()).clear();
                            SharedPrefManager.getInstance(getActivity()).clear();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                updateProfile();
                break;
            case R.id.buttonChangePassword:
                updatePassword();
                break;
            case R.id.buttonLogout:
                logout();
                break;
            case R.id.buttonDelete:
                deleteUser();
                break;
        }
    }
}