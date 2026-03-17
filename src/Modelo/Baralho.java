package Modelo;

import java.util.Collections;
import java.util.Stack;

public abstract class Baralho {
    protected Stack<Carta> cartas = new Stack<>();
    protected Stack<Carta> descarte = new Stack<>(); // Nova pilha para cartas jogadas

    public abstract void criarBaralho();

    public void embaralhar(){
        Collections.shuffle(cartas);
    }

    public Carta comprar(){
        // Se o monte principal acabar, recicla o descarte!
        if (cartas.isEmpty()) {
            reembaralharDescarte();
        }
        return cartas.isEmpty() ? null : cartas.pop();
    }

    // Adiciona uma carta à pilha de descarte
    public void adicionarAoDescarte(Carta c) {
        if (c != null) descarte.push(c);
    }

    // Pega o que está no descarte e coloca de volta no monte principal
    public void reembaralharDescarte() {
        if (descarte.isEmpty()) return;

        System.out.println("LOG: O baralho acabou. Reembaralhando o descarte...");

        // Move todas as cartas do descarte para o monte de compra
        while (!descarte.isEmpty()) {
            cartas.push(descarte.pop());
        }

        // Mistura tudo para o jogo continuar aleatório
        embaralhar();
    }
}