package com.example.online_shop_mobile.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.storage.SharedPrefManager;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView textViewEmail, textViewFName, textViewLName, textViewCity, textViewAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewFName = view.findViewById(R.id.textViewFName);
        textViewLName = view.findViewById(R.id.textViewLName);
        textViewCity = view.findViewById(R.id.textViewCity);
        textViewAddress = view.findViewById(R.id.textViewAddress);

        textViewEmail.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
        textViewFName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getFName());
        textViewLName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getLName());
        textViewCity.setText(SharedPrefManager.getInstance(getActivity()).getUser().getCity());
        textViewAddress.setText(SharedPrefManager.getInstance(getActivity()).getUser().getAddress());
    }
}