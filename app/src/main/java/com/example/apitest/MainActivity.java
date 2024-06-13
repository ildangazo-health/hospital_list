package com.example.apitest;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<ApiResponse.Item> itemList;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        myAdapter = new MyAdapter(itemList, this::showLocationOnMap);
        recyclerView.setAdapter(myAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi1.nhis.or.kr/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        fetchItems();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

    private void showLocationOnMap(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                LatLng location = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(location).title("Selected Hospital"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
