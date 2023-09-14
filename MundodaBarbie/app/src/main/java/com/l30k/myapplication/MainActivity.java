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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Mapa mapa;
    private ImageButton[][] botoes; // Matriz para armazenar as referências dos botões

    private boolean aceitarAltomatico = false;

    private boolean estaExecutando = false;

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


    public void onInicar(View view) {
        if(estaExecutando){
            return;
        }

        estaExecutando = true;

        resetarPosicaoDosPersonagem();

        List<int[]> casas = new ArrayList<>(); // Inicialize a lista de casas

        List<List<Node>> Fim = new ArrayList<>();

        TextView textTempoVariavel = findViewById(R.id.textTempoVariavel);


        int[] ponto1 = {4, 12};
        int[] ponto2 = {9, 8};
        int[] ponto3 = {36, 36};
        int[] ponto4 = {23, 37};
        int[] ponto5 = {35, 14};
        int[] ponto6 = {5, 34};

        casas.add(ponto1);
        casas.add(ponto2);
        casas.add(ponto3);
        casas.add(ponto4);
        casas.add(ponto5);
        casas.add(ponto6);

        AStar aStar = new AStar(mapa.getMapaInteiro());
        List<Node> path = null;
        List<List<Node>> aux = new ArrayList<>();
        int x = 22, y = 18;

        int aceito = 0;
        int recuso = 0;
        final int[] tempoFim = {0};

        while (aceito < 4) {
            // Verifique se já aceitamos 3 destinos
            if (aceito >= 3) {
                // Retorne para o ponto inicial
                path = aStar.findPath(x, y, 22, 18); // Volte para (22, 18)
                for (int i = 0; i < path.size(); i++) {
                    Log.d("AStar Caminho", "X: " + path.get(i).getX() + ", Y: " + path.get(i).getY() + ", Cost: " + path.get(i).getCostPorMovimento());
                }

                Fim.add(path);
                break;
            }

            for (int i = 0; i < casas.size(); i++) {
                List<Node> ass = aStar.findPath(x, y, casas.get(i)[0], casas.get(i)[1]);
                aux.add(ass);
            }

            if (!aux.isEmpty()) {
                path = aux.get(0);
                int pos = 0;

                for (int i = 0; i < aux.size(); i++) {
                    if (aux.get(i).get(aux.get(i).size() - 1).getCost() < path.get(path.size() - 1).getCost()) {
                        path = aux.get(i);
                        pos = i;
                    }
                }

                if (path != null) {
                    for (int i = 0; i < path.size(); i++) {
                        Log.d("AStar Caminho", "X: " + path.get(i).getX() + ", Y: " + path.get(i).getY() + ", Cost: " + path.get(i).getCostPorMovimento());
                    }
                    Fim.add(path);

                    x = path.get(path.size() - 1).getX();
                    y = path.get(path.size() - 1).getY();


                    if(aceitarAltomatico){
                        aceito++;
                    }else{
                        Random random = new Random();
                        if (random.nextBoolean()) {
                            aceito++;
                        } else {
                            if(recuso == 3){
                                aceito++;
                            }else{
                                recuso++;
                            }
                        }
                    }

                    casas.remove(pos);
                }
            } else {
                Log.d("AStar Caminho", "No path found.");
                break; // Saia do loop se não houver mais caminhos possíveis
            }

            aux.clear(); // Limpe a lista auxiliar para a próxima iteração
        }

        TextView textAceitoVariavel = findViewById(R.id.textAceitoVariavel);
        TextView textRecusoVariavel = findViewById(R.id.textRecusoVariavel);
        textAceitoVariavel.setText("" + aceito);
        if(!aceitarAltomatico){
            textRecusoVariavel.setText("" + recuso);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Fim.size(); i++) {
                    for (int j = 0; j < Fim.get(i).size(); j++) {
                        final int finalI = i;
                        final int finalJ = j;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                botoes[Fim.get(finalI).get(finalJ).getX()][Fim.get(finalI).get(finalJ).getY()].setImageResource(R.drawable.barbie);
                                if (finalJ > 0) {
                                    botoes[Fim.get(finalI).get(finalJ - 1).getX()][Fim.get(finalI).get(finalJ - 1).getY()].setImageResource(0);
                                }

                                tempoFim[0] += Fim.get(finalI).get(finalJ).getCostPorMovimento();

                                int minutos = tempoFim[0];
                                int horas = minutos / 60;
                                minutos %= 60;

                                String tempoFormatado = horas + ":" + (minutos < 10 ? "0" : "") + minutos + ":00 ou " + tempoFim[0];
                                textTempoVariavel.setText(tempoFormatado);

                            }
                        });

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                estaExecutando = false;
            }
        }).start();
    }

    public void onAcitar(View view){
        if(aceitarAltomatico){
            aceitarAltomatico = false;
            TextView textRecusoVariavel = findViewById(R.id.textRecusoVariavel);
            textRecusoVariavel.setText("0");
        }else{
            aceitarAltomatico = true;
            TextView textRecusoVariavel = findViewById(R.id.textRecusoVariavel);
            textRecusoVariavel.setText("ninguém");
        }
    }
}
