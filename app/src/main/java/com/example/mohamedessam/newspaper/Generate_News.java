package com.example.mohamedessam.newspaper;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Generate_News extends Service {
    // url = https://newsapi.org/v2/everything?domains=wsj.com&apiKey=e860f7ce9cf946019129b3723b3d65e2

    IBinder iBinder = new MoBind();
    interface NewsAPI
    {
        @GET("everything?domains=wsj.com&apiKey=e860f7ce9cf946019129b3723b3d65e2")
        Call<Example> get_articles();

    }

    public Generate_News() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void generate_news_using_retrofit(final Show_news.NewsRecycleAdaptor newsRecycleAdaptor , final ArrayList<Articles> articles )
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://newsapi.org/v2/").addConverterFactory(GsonConverterFactory.create()).build();
        Generate_News.NewsAPI newsAPI = retrofit.create(Generate_News.NewsAPI.class);
        newsAPI.get_articles().enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                for(int x=0;x<response.body().getArticles().size();x++)
                {
                    articles.add(response.body().getArticles().get(x));
                    newsRecycleAdaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.e("Error is : ",t.getMessage().toString());
            }
        });
    }

    public class MoBind extends Binder
    {
        Generate_News get_Generate_News_service()
        {
            return Generate_News.this;
        }
    }
}
