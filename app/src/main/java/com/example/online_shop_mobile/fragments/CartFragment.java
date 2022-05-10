package com.example.online_shop_mobile.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.adapters.CartAdapter;
import com.example.online_shop_mobile.api.RetrofitClient;
import com.example.online_shop_mobile.models.Product;
import com.example.online_shop_mobile.models.User;
import com.example.online_shop_mobile.models.response.OrderResponse;
import com.example.online_shop_mobile.models.response.TotalSumResponse;
import com.example.online_shop_mobile.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Product> productList;
    private TextView totalSum;
    private Button purchaseBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        User user = SharedPrefManager.getInstance(getActivity())
                .getUser();

        totalSum = view.findViewById(R.id.totalSum);
        purchaseBtn = view.findViewById(R.id.purchaseBtn);

        purchaseBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<OrderResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .purchase(user.getEmail());

                        call.enqueue(new Callback<OrderResponse>() {
                            @Override
                            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                System.out.println(response.body().getMessage());
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<OrderResponse> call, Throwable t) {

                            }
                        });
                    }
                }
        );

        Call<TotalSumResponse> totalSumCall = RetrofitClient
                .getInstance()
                .getApi()
                .getTotalSum(user.getEmail());

        totalSumCall.enqueue(new Callback<TotalSumResponse>() {
            @Override
            public void onResponse(Call<TotalSumResponse> call, Response<TotalSumResponse> response) {
                TotalSumResponse totalSumResponse = response.body();

                if(!totalSumResponse.isError()) {
                    totalSum.setText(totalSumResponse.getTotalSum() + " ₸");
                } else {
                    System.out.println("User not found");
                }

            }

            @Override
            public void onFailure(Call<TotalSumResponse> call, Throwable t) {

            }
        });

        Call<List<Product>> getCartProductsCall = RetrofitClient
                .getInstance()
                .getApi()
                .getCartProducts(user.getEmail());

        getCartProductsCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                productList = response.body();
                adapter = new CartAdapter(getActivity(), productList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });

    }


}