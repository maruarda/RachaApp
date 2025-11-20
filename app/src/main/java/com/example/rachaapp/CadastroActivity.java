package com.example.rachaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CadastroActivity extends AppCompatActivity {

    private int avatarSelecionadoId = -1; // Armazena o ID (tag) do avatar escolhido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Configura o comportamento de clique nos avatares
        setupAvatares();

        // Configura o botão de finalizar cadastro
        findViewById(R.id.btnFinalizarCadastro).setOnClickListener(v -> tentarCadastrar());
    }

    private void setupAvatares() {
        LinearLayout container = findViewById(R.id.containerAvatares);

        // Percorre os filhos do container (que agora são CardViews)
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);

            // Verifica se o item é um CardView (nossos avatares)
            if (child instanceof CardView) {
                CardView card = (CardView) child;

                // Procura a imagem dentro do Card para configurar o clique
                if (card.getChildCount() > 0 && card.getChildAt(0) instanceof ImageView) {
                    ImageView img = (ImageView) card.getChildAt(0);

                    // Quando clicar na imagem, chama a função de seleção
                    img.setOnClickListener(v -> onAvatarClick(card, img));

                    // Opcional: Seleciona o primeiro avatar por padrão ao abrir a tela
                    // (Remova o if abaixo se quiser que comece sem nenhum selecionado)
                    /*
                    if (i == 0) {
                        onAvatarClick(card, img);
                    }
                    */
                }
            }
        }
    }

    private void onAvatarClick(CardView selectedCard, ImageView selectedImg) {
        LinearLayout container = findViewById(R.id.containerAvatares);

        // 1. Limpa a seleção de todos os Cards (remove a borda colorida)
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof CardView) {
                // Remove o background de borda
                child.setBackgroundResource(0);
                // Remove o padding para a imagem voltar ao tamanho normal (preencher o círculo)
                child.setPadding(0,0,0,0);
            }
        }

        // 2. Marca o Card selecionado visualmente
        // Aplica o drawable de borda como fundo do CardView
        // Certifique-se de que o arquivo 'bg_avatar_selected.xml' existe na pasta drawable
        selectedCard.setBackgroundResource(R.drawable.bg_avatar_selected);

        // Adiciona um pequeno padding para que a borda apareça "em volta" da imagem
        // sem ser cortada pelo CardView. (8 pixels de borda interna)
        int padding = 8;
        selectedCard.setPadding(padding, padding, padding, padding);

        // 3. Salva o ID do avatar escolhido na variável global
        Object tag = selectedImg.getTag();
        if (tag != null) {
            try {
                avatarSelecionadoId = Integer.parseInt(tag.toString());
            } catch (NumberFormatException e) {
                avatarSelecionadoId = 1; // Valor padrão se der erro
            }
        }
    }

    private void tentarCadastrar() {
        // Referências aos campos de texto
        EditText etNome = findViewById(R.id.etNomeCompleto);
        EditText etUsuario = findViewById(R.id.etUsuarioCadastro);
        EditText etSenha = findViewById(R.id.etSenhaCadastro);

        String nome = etNome.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        // Validação Simples
        if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (avatarSelecionadoId == -1) {
            Toast.makeText(this, "Escolha um avatar para o seu perfil!", Toast.LENGTH_SHORT).show();
            return;
        }

        // SUCESSO!
        String mensagem = "Bem-vindo(a), " + nome + "! Conta criada.";
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

        // Redireciona para a tela Principal (ou Login)
        // Certifique-se de que LoginActivity.class existe. Se quiser ir direto pro app, use RachaDetailActivity.class
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a tela de cadastro para não voltar com o botão "Voltar"
    }
}