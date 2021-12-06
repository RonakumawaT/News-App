package com.example.samachar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {


    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private NewsRVAdapter newsRVAdapter;
    private CategoryRVAdapter categoryRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV = findViewById(R.id.RVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);

        getCategory();

        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }


        private void getCategory(){
            categoryRVModalArrayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1552012086-18eece80a2d9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=330&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1485827404703-89b55fcc595e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1507668077129-56e32842fceb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1477332552946-cfb384aeaf1c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1503442862980-50ccb3f1d085?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("Business","https://images.unsplash.com/39/lIZrwvbeRuuzqOoWJUEn_Photoaday_CSD%20(1%20of%201)-5.jpg?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1603190287605-e6ade32fa852?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
            categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1459865264687-595d652de67e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
            categoryRVAdapter.notifyDataSetChanged();
        }

    private void getNews(String category){

        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
            String categoryUrl="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=b7cca30f170c40c0adfd13fbf3a62210";

            String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=b7cca30f170c40c0adfd13fbf3a62210";
            String BASE_URL="https://newsapi.org/";
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
            Call<NewsModal> call;

            if(category.equals("All")){
                call=retrofitAPI.getAllNews(url);
            }
            else {
                call=retrofitAPI.getNewsByCategory(categoryUrl);
            }
            call.enqueue(new Callback<NewsModal>() {
                @Override
                public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                    NewsModal newsModal=response.body();
                    loadingPB.setVisibility(View.GONE);
                    ArrayList<Articles> articles=newsModal.getArticles();
                    for(int i=0;i<articles.size();i++){
                        articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),
                                articles.get(i).getUrl(),articles.get(i).getContent()));
                    }
                    newsRVAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(@NonNull Call<NewsModal> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    @Override
    public void onCategoryClick(int position) {
        String category=categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}