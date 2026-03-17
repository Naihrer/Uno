package Espec;

import Modelo.Carta;
import Modelo.Jogo;

public class CartaConvencional extends Carta {

    // O construtor apenas repassa os dados para a classe pai (Modelo.Carta)
    public CartaConvencional(CorCarta cor, TipoCarta tipo, int numero) {
        super(cor, tipo, numero);
    }

    @Override
    public void aplicarEfeito(Jogo controlador) {
        // Você pode deixar vazio por enquanto ou implementar a lógica
        // de pular/comprar aqui depois.
    }
}