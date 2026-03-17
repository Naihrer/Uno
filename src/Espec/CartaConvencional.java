package Espec;

import Modelo.Carta;
import Modelo.Jogo;

public class CartaConvencional extends Carta {

    public CartaConvencional(CorCarta cor, TipoCarta tipo, int numero) {
        super(cor, tipo, numero);
    }

    @Override
    public void aplicarEfeito(Jogo controlador) {
    }
}