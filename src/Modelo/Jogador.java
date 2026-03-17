package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String nome;
    private List<Carta> mao = new ArrayList<>();
    private boolean ehBot;

    public Jogador(String nome, boolean ehBot) {
        this.nome = nome;
        this.ehBot = ehBot;
    }

    public String getNome() { return nome; }
    public List<Carta> getMao() { return mao; }
    public boolean isBot() { return ehBot; }
    public void adicionarCarta(Carta c) { if(c != null) mao.add(c); }
}