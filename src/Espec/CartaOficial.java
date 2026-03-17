package Espec;
import Modelo.Jogo;
import Modelo.Carta;

public class CartaOficial extends Carta {

    // Construtor: recebe os dados e repassa para a classe pai (super)
    public CartaOficial(CorCarta cor, TipoCarta tipo, int numero) {
        super(cor, tipo, numero);

        // Validação: Garante que cartas de número sigam a regra do UNO (0 a 9)
        if (tipo == TipoCarta.NUMERO) {
            if (numero < 0 || numero > 9) {
                // Lança um erro se alguém tentar criar uma carta com número inválido
                throw new IllegalArgumentException("Número da carta deve ser entre 0 e 9.");
            }
        } else {
            // Se for carta de ação (Pular, Inverter, etc.), o número é fixado em -1
            this.numero = -1;
        }
    }

    // Como a lógica de efeito pode variar, você pode sobrescrever o aplicarEfeito aqui
    @Override
    public void aplicarEfeito(Jogo controlador) {
        // Lógica específica para as regras oficiais
    }
}