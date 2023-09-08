package com.l30k.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Mapa mapa;
    private ImageButton[][] botoes; // Matriz para armazenar as referências dos botões

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bloquear rotação
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mapa = new Mapa();

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        int numLinhas = 42;
        int numColunas = 42;

        // Tamanho das células, mantendo-as quadradas
        int tamanhoCelula = getResources().getDisplayMetrics().widthPixels / numColunas;

        // Inicialize a matriz para armazenar as referências dos botões
        botoes = new ImageButton[numLinhas][numColunas];

        for (int i = 0; i < numLinhas; i++) {
            for (int j = 0; j < numColunas; j++) {
                int valorCelula = mapa.getMapa(i, j);
                ImageButton botao = new ImageButton(this);

                // Defina a cor de fundo com base no valor da célula
                int corDeFundo = getCorParaValorCelula(valorCelula);
                botao.setBackgroundColor(corDeFundo);

                // Crie uma forma retangular para representar a borda
                ShapeDrawable bordaDrawable = new ShapeDrawable(new RectShape());
                bordaDrawable.getPaint().setColor(Color.rgb(0, 0, 0)); // Cor da borda
                bordaDrawable.getPaint().setStyle(Paint.Style.STROKE); // Estilo da borda
                bordaDrawable.getPaint().setStrokeWidth(1f); // Largura da borda em pixels

                // Combine a cor de fundo e a borda em uma camada
                Drawable[] camadas = {bordaDrawable, botao.getBackground()};
                LayerDrawable layerDrawable = new LayerDrawable(camadas);
                layerDrawable.setLayerInset(1, 1, 1, 1, 1); // Ajuste a borda dentro do botão

                botao.setBackground(layerDrawable);

                botao.setLayoutParams(new GridLayout.LayoutParams(
                        new ViewGroup.LayoutParams(tamanhoCelula, tamanhoCelula)));
                botao.setAdjustViewBounds(true);
                botao.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                botao.setPadding(0, 0, 0, 0);
                botao.setClickable(false);

                // Armazene a referência do botão na matriz
                botoes[i][j] = botao;

                gridLayout.addView(botao);
            }
        }

        resetarPosicaoDosPersonagem();
    }

    // Função para obter a cor de fundo com base no valor da célula
    private int getCorParaValorCelula(int valorCelula) {
        switch (valorCelula) {
            case 0:
                return Color.rgb(210, 105, 30); // Edifícios
            case 1:
                return Color.rgb(192, 192, 192); // Asfalto
            case 3:
                return Color.rgb(139, 69, 19); // Terra
            case 5:
                return Color.rgb(0, 255, 0); // Grama
            case 10:
                return Color.rgb(128, 128, 128); // Paralelepípedo
            default:
                return Color.WHITE; // Cor padrão para outros valores
        }
    }

    // Deixa os personagem nos locais iniciais
    private void resetarPosicaoDosPersonagem() {
        botoes[22][18].setImageResource(R.drawable.barbie); //ok

        botoes[4][12].setImageResource(R.drawable.carly); //ok

        botoes[9][8].setImageResource(R.drawable.brandon); //ok

        botoes[36][36].setImageResource(R.drawable.suzy); //ok

        botoes[23][37].setImageResource(R.drawable.mary); //ok

        botoes[35][14].setImageResource(R.drawable.carly); //ok

        botoes[5][34].setImageResource(R.drawable.ken); //ok
    }
}
