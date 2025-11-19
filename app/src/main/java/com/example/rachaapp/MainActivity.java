package com.example.rachaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
// Importações de Intent, View, etc., virão depois

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Link com o XML da Tela 1
    }
}