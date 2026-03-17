package Modelo;

/**
 * Classe abstrata que define o comportamento base de uma carta no jogo.
 */
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

    /**
     * Retorna o nome da cor em String.
     * Ajustado para tratar Coringas que ainda não tiveram cor definida como "Preto".
     */
    public String getCor() {
        // Se for um coringa e a cor interna ainda for a padrão (CORINGA), tratamos como Preto
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

    /**
     * Retorna a representação visual do valor da carta.
     */
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

    /**
     * Valida se esta carta pode ser jogada sobre a carta que está na mesa.
     */
    public boolean podeJogarSobre(Carta outra) {
        // 1. Se a carta atual for um Coringa, ela sempre pode ser jogada.
        if (this.isCoringa()) {
            return true;
        }

        // 2. Se a carta na mesa for um Coringa, esta carta deve bater com a COR escolhida.
        if (outra.isCoringa()) {
            return this.cor == outra.cor;
        }

        // 3. Regra de Ouro: Deve bater a COR ou o VALOR VISUAL (Ex: J sobre J, ou Vermelho sobre Vermelho).
        boolean mesmaCor = (this.cor == outra.cor);
        boolean mesmoValorVisual = this.getValor().equals(outra.getValor());

        return mesmaCor || mesmoValorVisual;
    }

    /**
     * Método abstrato para aplicar as ações especiais da carta (Pulo, Inverter, etc).
     */
    public abstract void aplicarEfeito(Jogo controlador);

    /**
     * Verifica se a carta possui o tipo WILD ou WILD_MAIS_QUATRO.
     */
    public boolean isCoringa() {
        return this.tipo == TipoCarta.WILD || this.tipo == TipoCarta.WILD_MAIS_QUATRO;
    }

    /**
     * Altera a cor da carta (usado após um Coringa ser jogado).
     */
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