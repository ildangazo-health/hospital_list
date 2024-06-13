package com.example.apitest;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<ApiResponse.Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        myAdapter = new MyAdapter(itemList);
        recyclerView.setAdapter(myAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi1.nhis.or.kr/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        fetchItems();
    }

    private void fetchItems() {
        String apiKey = "Z+Kh1rt1wx8gWPRHJlF//NXPLfHdKxDhcpJOh1CSlsd15QkADSm6Cg8XOEsS3XEyzakb31kMLvWinJai0LpB5Q==";  // 여기에 디코딩된 API 키를 입력하세요.
        Call<ApiResponse> call = apiService.getItems(apiKey, "청주", "43", "113");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.body != null && apiResponse.body.items != null) {
                        itemList.addAll(apiResponse.body.items);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("API_RESPONSE", "응답에 데이터가 없습니다. body: " + apiResponse.body);
                    }
                } else {
                    Log.e("API_ERROR", "응답이 성공적이지 않음: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API_ERROR", "요청 실패", t);
            }
        });
    }
}
