package com.example.rachaapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve User Data passed from Login/Cadastro
        String userName = getIntent().getStringExtra("USER_NAME");
        int avatarId = getIntent().getIntExtra("USER_AVATAR", 1);

        // Setup UI
        setupHeader(userName, avatarId);
    }

    private void setupHeader(String name, int avatarId) {
        TextView tvNome = findViewById(R.id.tvNomeUsuarioHome);
        ImageView imgPerfil = findViewById(R.id.imgPerfilHome);

        if (name != null) {
            tvNome.setText(name);
        }

        // Dynamic resource lookup for avatar
        String drawableName = "avatar_" + avatarId;
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());

        if (resId != 0) {
            imgPerfil.setImageResource(resId);
        }
    }
}