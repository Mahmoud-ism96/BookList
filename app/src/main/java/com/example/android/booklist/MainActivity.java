package com.example.android.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static String mContain;
    public static String mTitle;
    public static String mAuthor;
    public String USGS_REQUEST_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        EditText contain= (EditText) findViewById(R.id.book_contain);
//        mContain=contain.getText().toString();
//        EditText title= (EditText) findViewById(R.id.book_title);
//        mTitle=title.getText().toString();
//        EditText author= (EditText) findViewById(R.id.book_author);
//        mAuthor= author.getText().toString();

        Button searchButton = (Button) findViewById(R.id.search);
        ImageView img1 = (ImageView) findViewById(R.id.img1);
        img1.setAlpha((float)0.1);


        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText containChecker = (EditText) findViewById(R.id.book_contain);
                String checker=containChecker.getText().toString();
                if (checker.equals("")) {
                    containChecker.setError("Please Enter a Keyword");
                } else {
                    updateUrl();
                    Intent intent = new Intent(MainActivity.this, Result.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void updateUrl() {
        EditText contain = (EditText) findViewById(R.id.book_contain);
        mContain = contain.getText().toString();
        EditText title = (EditText) findViewById(R.id.book_title);
        mTitle = title.getText().toString();
        EditText author = (EditText) findViewById(R.id.book_author);
        mAuthor = author.getText().toString();
        USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + mContain + "+intitle:" + mTitle + "+inauthor:" + mAuthor + "&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";
    }
}
