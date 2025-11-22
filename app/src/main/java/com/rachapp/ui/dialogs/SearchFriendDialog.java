package com.rachapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.ui.adapters.SearchUserAdapter;
import com.rachapp.data.model.Usuario;
import com.rachapp.data.network.RetrofitClient;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFriendDialog extends Dialog {

    private final long currentUserId;
    private final Runnable onFriendAddedCallback;

    public SearchFriendDialog(@NonNull Context context, long currentUserId, Runnable onFriendAddedCallback) {
        super(context);
        this.currentUserId = currentUserId;
        this.onFriendAddedCallback = onFriendAddedCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search_friend);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        findViewById(R.id.btnPerformSearch).setOnClickListener(v -> performSearch());
        findViewById(R.id.btnCloseSearch).setOnClickListener(v -> dismiss());
    }

    private void performSearch() {
        EditText etQuery = findViewById(R.id.etSearchQuery);
        String query = etQuery.getText().toString().trim();

        if (query.isEmpty()) return;

        Call<List<Usuario>> call = RetrofitClient.getInstance().getApi().searchUsuarios(query);
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupRecyclerView(response.body());
                } else {
                    Toast.makeText(getContext(), "Nenhum usuário encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "Erro na busca: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<Usuario> results) {
        RecyclerView rv = findViewById(R.id.rvSearchResults);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        SearchUserAdapter adapter = new SearchUserAdapter(getContext(), results, userToAdd -> {
            addFriend(userToAdd.getIdUsuario());
        });
        rv.setAdapter(adapter);
    }

    private void addFriend(Long friendId) {
        if (friendId == currentUserId) {
            Toast.makeText(getContext(), "Você não pode adicionar a si mesmo", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().addFriend(currentUserId, friendId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Amigo adicionado!", Toast.LENGTH_SHORT).show();
                    if (onFriendAddedCallback != null) onFriendAddedCallback.run();
                } else {
                    Toast.makeText(getContext(), "Erro ou já são amigos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}