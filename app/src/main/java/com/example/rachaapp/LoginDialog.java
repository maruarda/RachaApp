package com.example.rachaapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.rachaapp.model.Usuario;
import com.example.rachaapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDialog extends Dialog {

    public LoginDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login);

        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViewById(R.id.btnConfirmarLogin).setOnClickListener(v -> fazerLogin());
    }

    private void fazerLogin() {
        EditText etEmail = findViewById(R.id.etEmailLogin);
        EditText etSenha = findViewById(R.id.etSenhaLogin);

        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(getContext(), "Preencha email e senha", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario loginData = new Usuario();
        loginData.setEmail(email);
        loginData.setSenha(senha);

        Call<Usuario> call = RetrofitClient.getInstance().getApi().login(loginData);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario user = response.body();
                    Toast.makeText(getContext(), "Bem-vindo, " + user.getNome() + "!", Toast.LENGTH_SHORT).show();

                    // CHANGED: Navigate to HomeActivity instead of MainActivity
                    Intent intent = new Intent(getContext(), HomeActivity.class);

                    // Pass User Data to Home Screen
                    intent.putExtra("USER_NAME", user.getNome());
                    intent.putExtra("USER_AVATAR", user.getAvatarId());

                    getContext().startActivity(intent);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}