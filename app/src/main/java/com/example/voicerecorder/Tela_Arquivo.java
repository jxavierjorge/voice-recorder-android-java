package com.example.voicerecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tela_Arquivo extends AppCompatActivity {

    Button b_tela_principal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela__arquivo);

        b_tela_principal = findViewById(R.id.b_voltar);
        b_tela_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tela_Arquivo.this.finish();
            }
        });
    }
}
