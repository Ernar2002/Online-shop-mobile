package com.example.online_shop_mobile.models.response;

import android.widget.Toast;

import com.example.online_shop_mobile.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class TotalSumResponse {
    private boolean error;
    private int totalSum;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }
}
