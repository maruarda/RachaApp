package com.rachapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.rachapp.R;
import com.rachapp.data.model.Usuario;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.dialogs.AvatarSelectionDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private int avatarSelecionadoId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        findViewById(R.id.cardAvatarSelecionado).setOnClickListener(v -> abrirSeletorAvatar());
        findViewById(R.id.btnFinalizarCadastro).setOnClickListener(v -> tentarCadastrar());
    }

    private void abrirSeletorAvatar() {
        new AvatarSelectionDialog(this, selectedId -> {
            this.avatarSelecionadoId = selectedId;
            atualizarAvatarDisplay(selectedId);
        }).show();
    }

    private void atualizarAvatarDisplay(int avatarId) {
        ImageView imgDisplay = findViewById(R.id.imgAvatarDisplay);
        String drawableName = "avatar_" + avatarId;
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        if (resId != 0) imgDisplay.setImageResource(resId);
    }

    private void tentarCadastrar() {
        EditText etNome = findViewById(R.id.etNomeCompleto);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etSenha = findViewById(R.id.etSenha);
        EditText etTelefone = findViewById(R.id.etTelefone);

        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario novoUsuario = new Usuario(nome, email, senha, telefone, avatarSelecionadoId);

        Call<Usuario> call = RetrofitClient.getInstance().getApi().criarUsuario(novoUsuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro no servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Falha na conexão.", Toast.LENGTH_LONG).show();
            }
        });
    }
}