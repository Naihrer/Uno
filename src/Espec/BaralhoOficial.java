package Espec;

import Modelo.Baralho;
import Modelo.Carta;

public class BaralhoOficial extends Baralho {

    @Override
    public void criarBaralho() {
        cartas.clear(); //

        Carta.CorCarta[] cores = {
                Carta.CorCarta.VERMELHO,
                Carta.CorCarta.AZUL,
                Carta.CorCarta.VERDE,
                Carta.CorCarta.AMARELO
        };

        for (Carta.CorCarta cor : cores) {
            cartas.push(new CartaOficial(cor, Carta.TipoCarta.NUMERO, 0)); // Apenas um '0'

            for (int i = 1; i <= 9; i++) {
                cartas.push(new CartaOficial(cor, Carta.TipoCarta.NUMERO, i)); // Duas de cada 1-9
                cartas.push(new CartaOficial(cor, Carta.TipoCarta.NUMERO, i));
            }

            //Duas de cada carta de ação por cor
            for (int i = 0; i < 2; i++) {
                cartas.push(new CartaOficial(cor, Carta.TipoCarta.SKIP, -1));
                cartas.push(new CartaOficial(cor, Carta.TipoCarta.REVERSE, -1));
                cartas.push(new CartaOficial(cor, Carta.TipoCarta.MAIS_DOIS, -1));
            }
        }

        for (int i = 0; i < 4; i++) {
            cartas.push(new CartaOficial(Carta.CorCarta.CORINGA, Carta.TipoCarta.WILD, -1));
            cartas.push(new CartaOficial(Carta.CorCarta.CORINGA, Carta.TipoCarta.WILD_MAIS_QUATRO, -1));
        }

        this.embaralhar();
    }
}