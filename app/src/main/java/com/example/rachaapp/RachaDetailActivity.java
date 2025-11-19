package com.example.rachaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RachaDetailActivity extends AppCompatActivity {

    // Lista de amigos (Dados falsos para teste)
    private List<Participante> listaAmigos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racha_detail);

        // 1. Carregar dados iniciais (Maria, José, Ana)
        carregarDadosFalsosComItens();

        // 2. Configurar Botão de Adicionar Novo Item (Abre o Pop-up de divisão)
        Button btnAdd = findViewById(R.id.btnAdicionarItem);
        btnAdd.setOnClickListener(v -> mostrarPopupDivisao());

        // 3. Configurar RecyclerView Principal (Lista de Amigos e seus totais)
        // TODO: Aqui você deve configurar o Adapter da lista principal (rvDetalhesParticipantes).
        // Quando criar esse Adapter, no evento de clique (onClick) do item, chame:
        // mostrarDetalhesParticipante(participanteClicado);

        // Exemplo de teste rápido: Se quiser testar o pop-up de detalhes agora,
        // descomente a linha abaixo para abrir o da Maria ao iniciar a tela.
        // mostrarDetalhesParticipante(listaAmigos.get(0));
    }

    // --- DADOS FALSOS PARA TESTE ---
    private void carregarDadosFalsosComItens() {
        // Cria Maria e adiciona dívidas
        Participante maria = new Participante("Maria");
        maria.adicionarDivida("Carne", 30.00);
        maria.adicionarDivida("Cerveja", 30.00);

        // Cria José e adiciona dívidas
        Participante jose = new Participante("José");
        jose.adicionarDivida("Carne", 30.00);
        jose.adicionarDivida("Cerveja", 30.00);

        // Cria Ana (Não bebe, só come)
        Participante ana = new Participante("Ana");
        ana.adicionarDivida("Carne", 30.00);

        // Adiciona todos à lista do Racha
        listaAmigos.add(maria);
        listaAmigos.add(jose);
        listaAmigos.add(ana);
    }

    // --- POP-UP 1: ADICIONAR NOVO ITEM / DIVIDIR ---
    private void mostrarPopupDivisao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Infla o layout do pop-up de adição
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        // Fundo Transparente para respeitar bordas arredondadas
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Referências aos componentes do dialog
        EditText etValor = view.findViewById(R.id.etValorItem);
        TextView tvResultado = view.findViewById(R.id.tvValorPorPessoa);
        RecyclerView rvParticipantes = view.findViewById(R.id.rvParticipantesDialog);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarDivisao);

        // Configurar a Lista de Checkboxes (Quem vai dividir?)
        rvParticipantes.setLayoutManager(new LinearLayoutManager(this));

        // Adaptador que recalcula o valor sempre que alguém é marcado/desmarcado
        ParticipanteDialogAdapter adapter = new ParticipanteDialogAdapter(listaAmigos, () -> {
            calcularValorTempoReal(etValor.getText().toString(), tvResultado, rvParticipantes);
        });
        rvParticipantes.setAdapter(adapter);

        // Configurar Input de Valor (Recalcula enquanto digita)
        etValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularValorTempoReal(s.toString(), tvResultado, rvParticipantes);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botão Confirmar
        btnConfirmar.setOnClickListener(v -> {
            // Aqui entraria a lógica para salvar o item na lista de cada um
            Toast.makeText(this, "Item adicionado com sucesso!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    // Lógica Matemática: (Valor Total) / (Pessoas Selecionadas)
    private void calcularValorTempoReal(String valorTexto, TextView tvOutput, RecyclerView rv) {
        ParticipanteDialogAdapter adapter = (ParticipanteDialogAdapter) rv.getAdapter();
        if (adapter == null) return;

        int qtdSelecionados = adapter.getContagemSelecionados();
        double valorTotal = 0.0;

        try {
            if (!valorTexto.isEmpty()) {
                // Troca vírgula por ponto para evitar erros
                String valorLimpo = valorTexto.replace(",", ".");
                valorTotal = Double.parseDouble(valorLimpo);
            }
        } catch (NumberFormatException e) {
            valorTotal = 0.0;
        }

        if (qtdSelecionados > 0 && valorTotal > 0) {
            double parteDeCada = valorTotal / qtdSelecionados;
            tvOutput.setText(String.format("R$ %.2f", parteDeCada));
        } else {
            tvOutput.setText("R$ 0,00");
        }
    }

    // --- POP-UP 2: VER DETALHES DO PARTICIPANTE (NOVO) ---
    public void mostrarDetalhesParticipante(Participante p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Infla o layout de detalhes
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_detalhe_participante, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Preencher dados do participante no layout
        TextView tvNome = view.findViewById(R.id.tvNomeParticipanteDetalhe);
        TextView tvTotal = view.findViewById(R.id.tvTotalDetalhe);
        RecyclerView rvItens = view.findViewById(R.id.rvItensDetalhe);
        Button btnFechar = view.findViewById(R.id.btnFecharDetalhe);

        tvNome.setText(p.getNome());
        tvTotal.setText(String.format("R$ %.2f", p.getTotalPagar()));

        // Configurar lista de itens (Carne 30, Cerveja 30...)
        rvItens.setLayoutManager(new LinearLayoutManager(this));
        DetalhesAdapter adapter = new DetalhesAdapter(p.getItens());
        rvItens.setAdapter(adapter);

        btnFechar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}