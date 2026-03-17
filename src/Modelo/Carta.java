package Modelo;

public abstract class Carta {
    protected CorCarta cor;
    protected TipoCarta tipo;
    protected int numero;

    public enum CorCarta {
        VERMELHO, AZUL, VERDE, AMARELO, CORINGA
    }

    public enum TipoCarta {
        NUMERO, SKIP, REVERSE, MAIS_DOIS, WILD, WILD_MAIS_QUATRO
    }

    public Carta(CorCarta cor, TipoCarta tipo, int numero) {
        this.cor = cor;
        this.tipo = tipo;
        this.numero = numero;
    }

    public String getCor() {
        if (isCoringa() && this.cor == CorCarta.CORINGA) {
            return "Preto";
        }

        return switch (this.cor) {
            case VERMELHO -> "Vermelho";
            case AZUL -> "Azul";
            case VERDE -> "Verde";
            case AMARELO -> "Amarelo";
            default -> "Preto";
        };
    }

    public String getValor() {
        return switch (this.tipo) {
            case NUMERO -> String.valueOf(numero);
            case SKIP -> "J";
            case REVERSE -> "Q";
            case MAIS_DOIS -> "K";
            case WILD -> "Coringa";
            case WILD_MAIS_QUATRO -> "Joker+4";
        };
    }

    public boolean podeJogarSobre(Carta outra) {
        // 1. Se a carta atual for um Coringa, ela sempre pode ser jogada.
        if (this.isCoringa()) {
            return true;
        }

        if (outra.isCoringa()) {
            return this.cor == outra.cor;
        }

        boolean mesmaCor = (this.cor == outra.cor);
        boolean mesmoValorVisual = this.getValor().equals(outra.getValor());

        return mesmaCor || mesmoValorVisual;
    }

    public abstract void aplicarEfeito(Jogo controlador);

    public boolean isCoringa() {
        return this.tipo == TipoCarta.WILD || this.tipo == TipoCarta.WILD_MAIS_QUATRO;
    }

    public void setCor(String corString) {
        this.cor = switch (corString) {
            case "Vermelho" -> CorCarta.VERMELHO;
            case "Azul" -> CorCarta.AZUL;
            case "Verde" -> CorCarta.VERDE;
            case "Amarelo" -> CorCarta.AMARELO;
            default -> CorCarta.CORINGA; // Retorna ao estado neutro/preto
        };
    }

    public TipoCarta getTipo() {
        return this.tipo;
    }

    public int getNumero() {
        return this.numero;
    }
}