package com.example.online_shop_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.api.RetrofitClient;
import com.example.online_shop_mobile.models.Product;
import com.example.online_shop_mobile.models.User;
import com.example.online_shop_mobile.models.response.OrderResponse;
import com.example.online_shop_mobile.models.response.TotalSumResponse;
import com.example.online_shop_mobile.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mCtx;
    private List<Product> productList;

    public CartAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_cart_roducts, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        User user = SharedPrefManager.getInstance(mCtx.getApplicationContext()).getUser();

        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductPrice.setText(product.getProductPrice() + " ₸" );
        Picasso.get()
                .load(product.getImage())
                .into(holder.imageViewProductImage);

        Call<TotalSumResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTotalSum(user.getEmail());

        call.enqueue(new Callback<TotalSumResponse>() {
            @Override
            public void onResponse(Call<TotalSumResponse> call, Response<TotalSumResponse> response) {
                TotalSumResponse totalSumResponse = response.body();

//                if(!totalSumResponse.isError()) {
//                    holder.textViewTotalSum.setText(totalSumResponse.getTotalSum() + " ₸");
//                } else {
//                    System.out.println("User not found");
//                }

            }

            @Override
            public void onFailure(Call<TotalSumResponse> call, Throwable t) {

            }
        });

        holder.deleteFromCartBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<OrderResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .deleteFromCart(product.getProductId(),
                                        user.getEmail());

                        call.enqueue(new Callback<OrderResponse>() {
                            @Override
                            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                Toast.makeText(mCtx.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<OrderResponse> call, Throwable t) {

                            }
                        });
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProductName, textViewProductPrice, textViewTotalSum;
        ImageView imageViewProductImage;
        Button deleteFromCartBtn, purchaseBtn;

        public CartViewHolder(View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            imageViewProductImage = itemView.findViewById(R.id.textViewProductImage);
            textViewTotalSum = itemView.findViewById(R.id.totalSum);

            deleteFromCartBtn = itemView.findViewById(R.id.deleteFromCartBtn);
            purchaseBtn = itemView.findViewById(R.id.purchaseBtn);
        }
    }
}