package Modelo;

import java.util.Collections;
import java.util.Stack;

public abstract class Baralho {
    protected Stack<Carta> cartas = new Stack<>();
    protected Stack<Carta> descarte = new Stack<>();

    public abstract void criarBaralho();

    public void embaralhar(){
        Collections.shuffle(cartas);
    }

    public Carta comprar(){
        if (cartas.isEmpty()) {
            reembaralharDescarte();
        }
        return cartas.isEmpty() ? null : cartas.pop();
    }

    public void adicionarAoDescarte(Carta c) {
        if (c != null) descarte.push(c);
    }

    public void reembaralharDescarte() {
        if (descarte.isEmpty()) return;
        System.out.println("LOG: O baralho acabou. Reembaralhando o descarte...");
        while (!descarte.isEmpty()) {
            cartas.push(descarte.pop());
        }
        embaralhar();
    }
}