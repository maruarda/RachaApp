package com.rachapp.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.ui.adapters.AmigosAdapter;
import com.rachapp.data.model.Usuario;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.dialogs.SearchFriendDialog;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvAmigos;
    private long currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String userName = getIntent().getStringExtra("USER_NAME");
        int avatarId = getIntent().getIntExtra("USER_AVATAR", 1);
        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        setupHeader(userName, avatarId);

        rvAmigos = findViewById(R.id.rvAmigosHome);
        rvAmigos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (currentUserId != -1) {
            carregarAmigos();
        }

        findViewById(R.id.btnAddAmigo).setOnClickListener(v -> {
            if (currentUserId != -1) {
                new SearchFriendDialog(this, currentUserId, this::carregarAmigos).show();
            }
        });
    }

    private void setupHeader(String name, int avatarId) {
        TextView tvNome = findViewById(R.id.tvNomeUsuarioHome);
        ImageView imgPerfil = findViewById(R.id.imgPerfilHome);

        if (name != null) {
            tvNome.setText(name.split(" ")[0]);
        }

        String drawableName = "avatar_" + avatarId;
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        if (resId != 0) imgPerfil.setImageResource(resId);
    }

    private void carregarAmigos() {
        Call<List<Usuario>> call = RetrofitClient.getInstance().getApi().getAmigos(currentUserId);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> amigos = response.body();
                    AmigosAdapter adapter = new AmigosAdapter(HomeActivity.this, amigos);
                    rvAmigos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Erro ao carregar amigos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}