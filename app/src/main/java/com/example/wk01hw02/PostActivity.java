package com.example.wk01hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    private TextView textViewResult;
    private TextView greeting;
    public static final String ACTIVITY_LABEL = "POST_ACTIVITY_COM_WK01HW02";
    public static final String ACTIVITY_LABEL_NAME = "POST_ACTIVITY_COM_WK01HW02_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textViewResult = findViewById(R.id.tvResults);
        greeting = findViewById(R.id.tvGreeting);

        int userId = getIntent().getIntExtra(ACTIVITY_LABEL,0);
        String username = getIntent().getStringExtra(ACTIVITY_LABEL_NAME);

        userGreeting(username);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts = response.body();
                for (Post post : posts) {
                    if (post.getUserId() == userId) {
                        String content = "";
                        content+= "User ID: " +post.getUserId() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n\n";
                        textViewResult.append(content);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }

    public static Intent getIntent(Context context, int userId) {
        Intent intent = new Intent(context,PostActivity.class);

        intent.putExtra(PostActivity.ACTIVITY_LABEL,userId);

        return intent;

    }

    public void userGreeting(String username) {

        greeting.setText(new StringBuilder().append("Welcome ").append(username).append("!").toString());

        Toast.makeText(PostActivity.this, "Welcome " + username + "!", Toast.LENGTH_LONG).show();

    }

}

