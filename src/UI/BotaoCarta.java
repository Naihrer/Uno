package UI;

import Espec.CartaOficial;
import Modelo.Carta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotaoCarta extends JButton {
    private Color corOriginal;
    private Color corHover;
    private int arcWidth = 20;
    private int arcHeight = 20;

    public BotaoCarta(Carta carta, ActionListener acao) {
        boolean ehUno = (carta instanceof CartaOficial);

        // --- DEFINIÇÃO DO SUBTÍTULO (NAIPE OU COR) ---
        String subtitulo;

        // Se for um Coringa que ainda não teve cor definida (Preto)
        if (carta.isCoringa() && carta.getCor().equals("Preto")) {
            subtitulo = ehUno ? "ESCOLHA A COR" : "ESCOLHA O NAIPE";
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

        // --- DEFINIÇÃO DO VALOR E LEGENDA DO EFEITO ---
        String valor = carta.getValor();
        String legenda = "";

        if (!ehUno) {
            if (valor.equals("J")) legenda = "<br><font size='-3'>PULO</font>";
            else if (valor.equals("Q")) legenda = "<br><font size='-3'>INV</font>";
            else if (valor.equals("K")) legenda = "<br><font size='-3'>+2</font>";
        } else {
            if (valor.equals("J")) valor = "PULO";
            else if (valor.equals("Q")) valor = "INV";
            else if (valor.equals("K")) valor = "+2";
        }

        if (valor.equals("Joker+4")) valor = "+4";
        if (valor.equals("Coringa")) valor = "CORINGA";

        // Monta o visual com HTML
        this.setText("<html><center><font size='+1'><b>" + valor + "</b></font>"
                + legenda + "<br><font size='-2'>" + subtitulo + "</font></center></html>");

        // --- ESTILIZAÇÃO ---
        this.setPreferredSize(new Dimension(110, 150));
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        // Define a cor de fundo do botão baseada na cor atual da carta
        corOriginal = switch (carta.getCor()) {
            case "Vermelho" -> new Color(220, 20, 60);
            case "Amarelo" -> new Color(255, 215, 0);
            case "Verde" -> new Color(0, 128, 0);
            case "Azul" -> new Color(30, 144, 255);
            default -> new Color(40, 40, 40); // Cor para o Coringa neutro (Cinza Escuro/Preto)
        };

        this.setBackground(corOriginal);
        this.corHover = corOriginal.brighter();

        // Ajusta a cor do texto para melhor contraste
        if (carta.getCor().equals("Amarelo")) setForeground(Color.BLACK);
        else setForeground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(corHover);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(corOriginal);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        if (acao != null) this.addActionListener(acao);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sombra
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, arcWidth, arcHeight);

        // Corpo da carta
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, arcWidth, arcHeight);

        // Borda interna para brilho/detalhe
        g2.setColor(getForeground().equals(Color.WHITE) ? new Color(255,255,255,100) : new Color(0,0,0,100));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(1, 1, getWidth() - 5, getHeight() - 5, arcWidth, arcHeight);

        g2.dispose();
        super.paintComponent(g);
    }
}