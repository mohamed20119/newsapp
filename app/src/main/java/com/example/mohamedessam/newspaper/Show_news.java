package com.example.mohamedessam.newspaper;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Show_news extends AppCompatActivity {

    Generate_News ref;
    android.support.v7.widget.RecyclerView recyclerView ;
    ArrayList<Articles> articles = new ArrayList<>();
    NewsRecycleAdaptor newsRecycleAdaptor;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        recyclerView = (android.support.v7.widget.RecyclerView)findViewById(R.id.news_recycle);
        newsRecycleAdaptor = new NewsRecycleAdaptor(articles);
        Intent i  =  new Intent(this,Generate_News.class);
        bindService(i,new Myserviceconnection(),BIND_AUTO_CREATE);
        recyclerView.setAdapter(newsRecycleAdaptor);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public class NewsRecycleAdaptor extends RecyclerView.Adapter<NewsHolder>
    {
        List<Articles> list;

        public NewsRecycleAdaptor(ArrayList<Articles> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.slid_show_news,viewGroup,false);
            return new NewsHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final NewsHolder newsHolder, int i) {

               newsHolder.title.setText(list.get(i).getTitle().toString());
               Picasso.get().load(list.get(i).getUrlToImage()).into( newsHolder.image);
               final int finalX = i;
               newsHolder.cardView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i  = new Intent(getBaseContext(),news_content.class);
                       i.putExtra("title",list.get(finalX).getTitle());
                       i.putExtra("pic",list.get(finalX).getUrlToImage());
                       i.putExtra("content",list.get(finalX).getContent());
                       startActivity(i);
                   }
               });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    public class NewsHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView title;
        android.support.v7.widget.CardView cardView ;
        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.news_title);
            image=(ImageView)itemView.findViewById(R.id.news_image);
            cardView = (android.support.v7.widget.CardView)itemView.findViewById(R.id.mycard);
        }
    }

    public class Myserviceconnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Generate_News.MoBind moBind = ( Generate_News.MoBind) service;
            ref = moBind.get_Generate_News_service();
            ref.generate_news_using_retrofit(newsRecycleAdaptor,articles);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
ref = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("current",linearLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if(savedInstanceState != null)
        {

            linearLayoutManager.onRestoreInstanceState( savedInstanceState.getParcelable("current"));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this,Login.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}


