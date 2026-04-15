package com.example.snfin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class LoadActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(LoadActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();;
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                String token = prefs.getString("auth_token", null);

                if (token != null && !token.isEmpty()) {
                    startActivity(new Intent(LoadActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(LoadActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);

    }
}
