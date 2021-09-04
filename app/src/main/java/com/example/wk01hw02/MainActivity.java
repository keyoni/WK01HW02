package com.example.wk01hw02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ArrayList<User> userList;
    EditText usernameEt;
    EditText passwordEt;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEt = findViewById(R.id.tvUsername);
        passwordEt = findViewById(R.id.tvPassword);
        loginBtn = findViewById(R.id.btnLogin);
        userList = createUsers();

        for(User user: userList) {
            Log.d(TAG, user.getUsername());
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();

                usernameEt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.black));
                passwordEt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                for(User user: userList) {
                    if (user.getUsername().equals(username)) {
                        usernameEt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.green_good));

                        if (user.getPassword().equals(password)) {
                            passwordEt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.green_good));
                            Toast.makeText(MainActivity.this, "Welcome " + username + "!", Toast.LENGTH_LONG).show();

                            login(user.getId(), user.getUsername());
                        } else {
                           badPassword();
                        }
                        break;
                    } else {
                        badUsername();
                    }
                }

            }
        });
    }


    public ArrayList<User> createUsers() {
        ArrayList<User> users = new ArrayList<>();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<User>> call = jsonPlaceHolderApi.getUsers();
         call.enqueue(new Callback<List<User>>() {
             @Override
             public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                 if (!response.isSuccessful()) {
                     return;
                 }
                 List<User> userList = response.body();
                 for (User user : userList) {
                 User tempUser = new User(user.getId(), user.getUsername(), "password" + (user.getId()));
                 users.add(tempUser);
                 Log.d(TAG, (tempUser.getUsername()));
             }
         }
        @Override
        public void onFailure(Call<List<User>> call, Throwable t) {
        }
    });

        return users;
    }


    // Login Results
    public void badUsername() {
        usernameEt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.red_bad));
        Toast.makeText(MainActivity.this, "Username not found", Toast.LENGTH_LONG).show();

    }

    public void badPassword() {
        passwordEt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.red_bad));
        Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();

    }

    //Factory Pattern
    public void login(int userId,String username) {
        Intent intent = PostActivity.getIntent(getApplicationContext(),userId);
        intent.putExtra("POST_ACTIVITY_COM_WK01HW02_name",username);
        startActivity(intent);
    }

}