package com.example.online_shop_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_shop_mobile.R;
import com.example.online_shop_mobile.activities.LoginActivity;
import com.example.online_shop_mobile.activities.ProfileActivity;
import com.example.online_shop_mobile.api.RetrofitClient;
import com.example.online_shop_mobile.fragments.CartFragment;
import com.example.online_shop_mobile.models.Product;
import com.example.online_shop_mobile.models.User;
import com.example.online_shop_mobile.models.response.DefaultResponse;
import com.example.online_shop_mobile.models.response.LoginResponse;
import com.example.online_shop_mobile.models.response.OrderResponse;
import com.example.online_shop_mobile.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private Context mCtx;
    private List<Product> productList;

    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_products, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Product product = productList.get(position);
        User user = SharedPrefManager.getInstance(mCtx.getApplicationContext()).getUser();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!" + mCtx.getApplicationContext().getClass());
        if(mCtx.getApplicationContext().equals(new CartFragment())){
            holder.addToCartBtn.setVisibility(View.INVISIBLE);
        }

        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductPrice.setText(product.getProductPrice() + " ₸" );
        Picasso.get()
                .load(product.getImage())
                .into(holder.imageViewProductImage);

        holder.addToCartBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<OrderResponse> call = RetrofitClient
                                .getInstance()
                                .getApi()
                                .addToCart(product.getProductId(),
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

    static class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProductName, textViewProductPrice;
        ImageView imageViewProductImage;
        Button addToCartBtn;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            imageViewProductImage = itemView.findViewById(R.id.textViewProductImage);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }

    public void clearList(){
        productList.clear();
    }
}