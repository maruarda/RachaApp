package com.example.rachaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ResumoFinanceiroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_financeiro);

        // Configurar Lista A RECEBER
        RecyclerView rvReceber = findViewById(R.id.rvReceber);
        TextView tvTotalReceber = findViewById(R.id.tvTotalReceber);

        List<ResumoItem> listaReceber = getDadosReceber();
        configurarLista(rvReceber, listaReceber);
        tvTotalReceber.setText(calcularTotal(listaReceber));

        // Configurar Lista A PAGAR
        RecyclerView rvPagar = findViewById(R.id.rvPagar);
        TextView tvTotalPagar = findViewById(R.id.tvTotalPagar);

        List<ResumoItem> listaPagar = getDadosPagar();
        configurarLista(rvPagar, listaPagar);
        tvTotalPagar.setText(calcularTotal(listaPagar));
    }

    private void configurarLista(RecyclerView rv, List<ResumoItem> dados) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        ResumoAdapter adapter = new ResumoAdapter(dados);
        rv.setAdapter(adapter);
    }

    private String calcularTotal(List<ResumoItem> lista) {
        double total = 0;
        for (ResumoItem item : lista) {
            total += item.getValor();
        }
        return String.format("R$ %.2f", total);
    }

    // --- DADOS FALSOS ---
    private List<ResumoItem> getDadosReceber() {
        List<ResumoItem> lista = new ArrayList<>();
        lista.add(new ResumoItem("Maria", "Churrasco", 60.00));
        lista.add(new ResumoItem("José", "Pizza Sexta", 25.50));
        lista.add(new ResumoItem("Maria", "Uber", 12.00));
        return lista;
    }

    private List<ResumoItem> getDadosPagar() {
        List<ResumoItem> lista = new ArrayList<>();
        lista.add(new ResumoItem("Pedro", "Cinema", 45.00));
        lista.add(new ResumoItem("Ana", "Presente Mãe", 100.00));
        return lista;
    }
}