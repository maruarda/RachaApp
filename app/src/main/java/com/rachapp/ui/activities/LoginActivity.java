package com.rachapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.rachapp.R;
import com.rachapp.ui.dialogs.LoginDialog;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnEntrar).setOnClickListener(v -> mostrarPopupLogin());

        findViewById(R.id.btnCriarConta).setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }

    private void mostrarPopupLogin() {
        new LoginDialog(this).show();
    }
}