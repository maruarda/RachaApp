package com.example.rachaapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class LoginDialog extends Dialog {

    public LoginDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViewById(R.id.btnConfirmarLogin).setOnClickListener(v -> {
            // Lógica de autenticação aqui
            Toast.makeText(getContext(), "Login realizado!", Toast.LENGTH_SHORT).show();
            getContext().startActivity(new Intent(getContext(), MainActivity.class)); // Vai para a Home
            dismiss();
        });
    }
}