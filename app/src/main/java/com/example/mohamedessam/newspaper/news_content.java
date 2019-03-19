package com.example.mohamedessam.newspaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class news_content extends Activity {
String url , title,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        Intent i =getIntent();
        url = i.getStringExtra("pic");
        title = i.getStringExtra("title");
        content = i.getStringExtra("content");



        ImageView imageView = (ImageView)findViewById(R.id.imageView2);
        TextView textView = (TextView)findViewById(R.id.textView);
        Picasso.get().load(Uri.parse(url)).into(imageView);
        textView.setText(content);
    }

}
