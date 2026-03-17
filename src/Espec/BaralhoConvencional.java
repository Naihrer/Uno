package Espec;

import Modelo.Baralho;
import Modelo.Carta;

public class BaralhoConvencional extends Baralho {

    @Override
    public void criarBaralho() {
        cartas.clear(); //

        Carta.CorCarta[] cores = {
                Carta.CorCarta.VERMELHO, // Copas
                Carta.CorCarta.AMARELO,  // Ouros
                Carta.CorCarta.VERDE,    // Paus
                Carta.CorCarta.AZUL      // Espadas
        };

        for (Carta.CorCarta c : cores) {
            // Cartas numeradas de 2 a 10
            for (int i = 2; i <= 10; i++) {
                cartas.push(new CartaConvencional(c, Carta.TipoCarta.NUMERO, i));
            }

            // Atribuindo tipos específicos para J, Q, K para a validação funcionar
            cartas.push(new CartaConvencional(c, Carta.TipoCarta.SKIP, -1));    // J (Pulo)
            cartas.push(new CartaConvencional(c, Carta.TipoCarta.REVERSE, -1)); // Q (Inverter)
            cartas.push(new CartaConvencional(c, Carta.TipoCarta.MAIS_DOIS, -1));// K (+2)
        }

        // Coringas Pretos (Apenas 2 no convencional para equilíbrio)
        cartas.push(new CartaConvencional(Carta.CorCarta.CORINGA, Carta.TipoCarta.WILD, -1));
        cartas.push(new CartaConvencional(Carta.CorCarta.CORINGA, Carta.TipoCarta.WILD_MAIS_QUATRO, -1));

        this.embaralhar();
    }
}