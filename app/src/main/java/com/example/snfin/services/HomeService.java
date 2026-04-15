package com.example.snfin.services;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.example.snfin.LoginActivity;
import com.example.snfin.MainActivity;
import com.example.snfin.R;

public class HomeService {

    public static void VoltaPraHome(ImageButton btnHome, final Activity activity) {

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
        });

    }

    public static void configurarLogout(ImageButton btnLogout, final Activity activity) {

        btnLogout.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(activity)
                    .setTitle("Sair")
                    .setMessage("Deseja realmente sair?")
                    .setPositiveButton("Sim", (dialog, which) -> {

                        // limpa token
                        activity.getSharedPreferences("AppPrefs", Activity.MODE_PRIVATE)
                                .edit()
                                .remove("auth_token")
                                .apply();

                        // vai pro login
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);

                        activity.finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
}
