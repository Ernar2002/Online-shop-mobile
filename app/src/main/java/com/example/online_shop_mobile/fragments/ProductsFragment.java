package com.example.online_shop_mobile.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.adapters.ProductsAdapter;
import com.example.online_shop_mobile.api.RetrofitClient;
import com.example.online_shop_mobile.models.Product;
import com.example.online_shop_mobile.models.response.OrderResponse;
import com.example.online_shop_mobile.models.response.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList;
    private EditText searchEditText;
    private Button searchBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchEditText = view.findViewById(R.id.searchEditText);
        searchBtn = view.findViewById(R.id.searchBtn);

        Call<List<Product>> call = RetrofitClient.getInstance().getApi().getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                productList = response.body();
                adapter = new ProductsAdapter(getActivity(), productList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });

        searchBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println(searchBtn.getText());
                        if (searchBtn.getText().equals("Search")) {
                            Call<List<Product>> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .searchProducts(searchEditText.getText().toString());

                            call.enqueue(new Callback<List<Product>>() {
                                @Override
                                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                    productList = response.body();
                                    adapter.clearList();
                                    adapter = new ProductsAdapter(getActivity(), productList);
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(Call<List<Product>> call, Throwable t) {

                                }
                            });

                            searchBtn.setText("Reload");
                            searchBtn.setBackgroundColor(Color.parseColor("#3F51B5"));
                        } else {
                            Call<List<Product>> call = RetrofitClient.getInstance().getApi().getProducts();

                            call.enqueue(new Callback<List<Product>>() {
                                @Override
                                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                                    productList = response.body();
                                    adapter.clearList();
                                    adapter = new ProductsAdapter(getActivity(), productList);
                                    recyclerView.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(Call<List<Product>> call, Throwable t) {

                                }
                            });

                            searchEditText.setText("");
                            searchBtn.setText("Search");
                            searchBtn.setBackgroundColor(Color.parseColor("#B50F8F14"));
                        }
                    }
                }
        );

    }
}