package com.example.online_shop_mobile.api;

import com.example.online_shop_mobile.models.Product;
import com.example.online_shop_mobile.models.response.DefaultResponse;
import com.example.online_shop_mobile.models.response.LoginResponse;
import com.example.online_shop_mobile.models.response.OrderResponse;
import com.example.online_shop_mobile.models.response.ProductResponse;
import com.example.online_shop_mobile.models.User;
import com.example.online_shop_mobile.models.response.RegisterResponse;
import com.example.online_shop_mobile.models.response.TotalSumResponse;
import com.example.online_shop_mobile.models.response.UsersResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("signup")
    Call<RegisterResponse> createUser(
            @Query("email") String email,
            @Query("password") String password,
            @Query("firstName") String fName,
            @Query("lastName") String lName
    );

    @GET("login")
    Call<LoginResponse> loginUser(
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("get-products")
    Call<List<Product>> getProducts();

    @GET("get-cart-products")
    Call<List<Product>> getCartProducts(
        @Query("email") String email
    );

    @GET("get-total-sum")
    Call<TotalSumResponse> getTotalSum(
            @Query("email") String email
    );

    @GET("allusers")
    Call<UsersResponse> getUsers();

    @GET("update-profile")
    Call<User> updateUser(
            @Query("email") String email,
            @Query("fName") String fName,
            @Query("lName") String lName,
            @Query("city") String city,
            @Query("address") String address

    );

    @GET("add-to-cart")
    Call<OrderResponse> addToCart(
            @Query("productId") Long productId,
            @Query("email") String email
    );

    @GET("delete-from-cart")
    Call<OrderResponse> deleteFromCart(
            @Query("productId") Long productId,
            @Query("email") String email
    );

    @GET("purchase")
    Call<OrderResponse> purchase(
            @Query("email") String email
    );

    @GET("search-products")
    Call<List<Product>> searchProducts(
            @Query("keyword") String productName
    );

    @FormUrlEncoded
    @PUT("updatepassword")
    Call<DefaultResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("email") String email
    );

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteUser(@Path("id") int id);

}
