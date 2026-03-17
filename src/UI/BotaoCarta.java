package UI;

import Espec.CartaOficial;
import Modelo.Carta;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BotaoCarta extends JButton {

    public BotaoCarta(Carta carta, ActionListener acao) {
        boolean ehUno = (carta instanceof CartaOficial);

        // 1. TRATAMENTO DO VALOR (Transforma J, Q, K em nomes de efeito se for UNO)
        String valorExibido = carta.getValor();
        if (ehUno) {
            if (valorExibido.equals("J")) valorExibido = "PULO";
            else if (valorExibido.equals("Q")) valorExibido = "INV";
            else if (valorExibido.equals("K")) valorExibido = "+2";
        }

        // 2. TRATAMENTO DO SUBTITULO (Naipe para Poker, Cor para UNO)
        String subtitulo;
        if (carta.isCoringa() && carta.getCor().equals("Preto")) {
            subtitulo = ehUno ? "(COR)" : "(NAIPE)";
        } else if (ehUno) {
            subtitulo = carta.getCor();
        } else {
            subtitulo = switch (carta.getCor()) {
                case "Vermelho" -> "Copas";
                case "Amarelo" -> "Ouros";
                case "Verde" -> "Paus";
                case "Azul" -> "Espadas";
                default -> "Coringa";
            };
        }

        this.setText("<html><center>" + valorExibido + "<br>" + subtitulo + "</center></html>");

        this.setPreferredSize(new Dimension(100, 130));

        Color corFundo = switch (carta.getCor()) {
            case "Vermelho" -> Color.RED;
            case "Amarelo" -> Color.YELLOW;
            case "Verde" -> Color.GREEN;
            case "Azul" -> Color.BLUE;
            default -> Color.BLACK;
        };

        this.setBackground(corFundo);

        if (corFundo == Color.YELLOW || corFundo == Color.GREEN) {
            this.setForeground(Color.BLACK);
        } else {
            this.setForeground(Color.WHITE);
        }

        if (acao != null) {
            this.addActionListener(acao);
        }
    }
}