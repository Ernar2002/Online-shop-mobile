package com.example.online_shop_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.fragments.CartFragment;
import com.example.online_shop_mobile.fragments.HomeFragment;
import com.example.online_shop_mobile.fragments.ProductsFragment;
import com.example.online_shop_mobile.fragments.ProfileFragment;
import com.example.online_shop_mobile.fragments.SettingsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnItemSelectedListener(this);

        displayFragment(new ProductsFragment());

    }

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch(item.getItemId()){
            case R.id.menu_home:
                fragment = new ProductsFragment();
                break;
            case R.id.menu_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.menu_cart:
                fragment = new CartFragment();
                break;
            case R.id.menu_settings:
                fragment = new SettingsFragment();
                break;
        }

        if(fragment != null){
            displayFragment(fragment);
        }

        return false;
    }

}