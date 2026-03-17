package UI;

import Modelo.Baralho;
import Modelo.Carta;
import Modelo.Jogador;
import Espec.CartaOficial;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class TelaJogo extends JFrame {
    private JPanel painelMao, painelMesa;
    private JTextPane areaLog;
    private JLabel labelTurno, labelCor, labelOrdem;
    private JButton btnUno;
    private List<Jogador> jogadores;
    private int indiceTurno = 0;
    private int sentido = 1;
    private Carta cartaNaMesa;
    private Baralho baralho;
    private boolean unoApertado = false;
    private boolean aguardandoConfirmacao = false;

    public TelaJogo(List<Jogador> jogadores, Carta inicial, Baralho baralho) {
        this.jogadores = jogadores;
        this.cartaNaMesa = inicial;
        this.baralho = baralho;

        setTitle("UNO POO - Partida");
        setSize(1280, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        setupUI();
        atualizarTela();
    }

    private void setupUI() {
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1));
        labelTurno = new JLabel("", SwingConstants.CENTER);
        labelTurno.setFont(new Font("Segoe UI", Font.BOLD, 32));
        labelOrdem = new JLabel("", SwingConstants.CENTER);
        labelOrdem.setFont(new Font("Monospaced", Font.BOLD, 16));
        painelSuperior.add(labelTurno);
        painelSuperior.add(labelOrdem);
        add(painelSuperior, BorderLayout.NORTH);

        painelMesa = new JPanel(new GridBagLayout());
        painelMesa.setBackground(new Color(0, 100, 0));
        add(painelMesa, BorderLayout.CENTER);

        JPanel painelInfo = new JPanel(new BorderLayout(10, 10));
        painelInfo.setPreferredSize(new Dimension(450, 0));
        labelCor = new JLabel("", SwingConstants.CENTER);
        labelCor.setFont(new Font("Segoe UI", Font.BOLD, 20));

        areaLog = new JTextPane();
        areaLog.setContentType("text/html");
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(25, 25, 25));

        HTMLDocument doc = (HTMLDocument) areaLog.getStyledDocument();
        doc.getStyleSheet().addRule("body { font-family: 'Segoe UI', sans-serif; color: white; margin: 10px; }");

        painelInfo.add(labelCor, BorderLayout.NORTH);
        painelInfo.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        add(painelInfo, BorderLayout.EAST);

        JPanel sul = new JPanel(new BorderLayout(15, 15));
        painelMao = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnComprar = new JButton("COMPRAR");
        btnComprar.addActionListener(e -> acaoComprar());

        btnUno = new JButton("UNO!");
        btnUno.setBackground(Color.RED);
        btnUno.setForeground(Color.WHITE);
        btnUno.setVisible(false);
        btnUno.addActionListener(e -> {
            unoApertado = true;
            log("VOCE GRITOU UNO!", "#FF0000");
            btnUno.setVisible(false);
        });

        painelBotoes.add(btnComprar);
        painelBotoes.add(btnUno);
        sul.add(new JScrollPane(painelMao), BorderLayout.CENTER);
        sul.add(painelBotoes, BorderLayout.EAST);
        add(sul, BorderLayout.SOUTH);
    }

    private void proximoTurno() {
        indiceTurno += sentido;
        if (indiceTurno >= jogadores.size()) indiceTurno = 0;
        else if (indiceTurno < 0) indiceTurno = jogadores.size() - 1;

        if (!jogadores.get(indiceTurno).isBot()) {
            aguardandoConfirmacao = true;
        }
    }

    private void atualizarTela() {
        Jogador atual = jogadores.get(indiceTurno);
        labelTurno.setText("VEZ DE: " + atual.getNome().toUpperCase());

        labelCor.setText("<html><div style='text-align:center;'>MESA: " + obterValorFormatado(cartaNaMesa) + " " + obterNaipeLog(cartaNaMesa).toUpperCase() + "</div></html>");

        String seta = (sentido == 1) ? " >> " : " << ";
        labelOrdem.setText("ORDEM: " + jogadores.stream().map(Jogador::getNome).collect(Collectors.joining(seta)));

        painelMesa.removeAll();
        painelMesa.add(new BotaoCarta(cartaNaMesa, null));

        painelMao.removeAll();

        if (!atual.isBot()) {
            if (aguardandoConfirmacao) {
                JButton btnConfirmar = new JButton("<html><center>CLIQUE PARA VER AS CARTAS DE<br><b>" + atual.getNome().toUpperCase() + "</b></center></html>");
                btnConfirmar.setPreferredSize(new Dimension(400, 100));
                btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btnConfirmar.addActionListener(e -> {
                    aguardandoConfirmacao = false;
                    atualizarTela();
                });
                painelMao.add(btnConfirmar);
                btnUno.setVisible(false);
            } else {
                for (Carta c : atual.getMao()) painelMao.add(new BotaoCarta(c, e -> acaoJogar(c)));
                btnUno.setVisible(atual.getMao().size() == 2);
            }
        } else {
            btnUno.setVisible(false);
        }

        revalidate();
        repaint();

        if (atual.isBot()) {
            Timer t = new Timer(1500, e -> vezDoBot(atual));
            t.setRepeats(false);
            t.start();
        }
    }

    private void acaoJogar(Carta c) {
        if (aguardandoConfirmacao) return;

        Jogador atual = jogadores.get(indiceTurno);
        if (!c.podeJogarSobre(this.cartaNaMesa)) return;

        if (!atual.isBot() && atual.getMao().size() == 2 && !unoApertado) {
            log("PUNICAO: Esqueceu o UNO! +2 cartas.", "#FFA500");
            for(int i=0; i<2; i++) {
                Carta comp = baralho.comprar();
                if(comp != null) atual.adicionarCarta(comp);
            }
        }

        unoApertado = false;
        baralho.adicionarAoDescarte(this.cartaNaMesa);
        atual.getMao().remove(c);
        this.cartaNaMesa = c;

        log(atual.getNome() + " JOGOU [" + obterValorFormatado(c) + " " + obterNaipeLog(c) + "]", obterCorJogador(atual));

        if (c.isCoringa()) tratarCoringa(atual);
        if (atual.getMao().isEmpty()) { mostrarVitoria(atual.getNome()); return; }

        processarEfeitos(c);
    }

    private void processarEfeitos(Carta c) {
        String v = c.getValor();
        if (v.equals("K") || v.equals("Joker+4") || v.equals("+2")) {
            proximoTurno();
            int qtd = (v.equals("Joker+4")) ? 4 : 2;
            for(int i=0; i<qtd; i++) {
                Carta comp = baralho.comprar();
                if(comp != null) jogadores.get(indiceTurno).adicionarCarta(comp);
            }
            log("EFEITO: " + jogadores.get(indiceTurno).getNome() + " bloqueado e comprou +" + qtd, "#00FFFF");
        } else if (v.equals("J") || v.equals("PULO")) {
            proximoTurno();
            log("EFEITO: " + jogadores.get(indiceTurno).getNome() + " foi pulado!", "#00FFFF");
        } else if (v.equals("Q") || v.equals("INV")) {
            sentido *= -1;
            log("EFEITO: Sentido invertido!", "#00FFFF");
            if(jogadores.size() == 2) proximoTurno();
        }
        proximoTurno();
        atualizarTela();
    }

    private void tratarCoringa(Jogador j) {
        Color[] coresSwing = {new Color(220, 20, 60), new Color(255, 215, 0), new Color(0, 128, 0), new Color(30, 144, 255)};
        String[] coresBase = {"Vermelho", "Amarelo", "Verde", "Azul"};

        boolean ehUno = (cartaNaMesa instanceof CartaOficial);
        String[] nomes = ehUno ? new String[]{"Vermelho", "Amarelo", "Verde", "Azul"} : new String[]{"Copas", "Ouros", "Paus", "Espadas"};

        final int[] escolha = {-1};
        if (!j.isBot()) {
            JPanel p = new JPanel(new GridLayout(1, 4, 5, 5));
            JDialog d = new JDialog(this, ehUno ? "Escolha a Cor" : "Escolha o Naipe", true);
            for (int i = 0; i < 4; i++) {
                int idx = i;
                JButton b = new JButton(nomes[i]);
                b.setBackground(coresSwing[idx]);
                b.setForeground(idx == 1 ? Color.BLACK : Color.WHITE);
                b.addActionListener(e -> { escolha[0] = idx; d.dispose(); });
                p.add(b);
            }
            d.add(p); d.pack(); d.setLocationRelativeTo(this); d.setVisible(true);
            if (escolha[0] == -1) escolha[0] = 0;
        } else {
            escolha[0] = (int)(Math.random() * 4);
        }
        cartaNaMesa.setCor(coresBase[escolha[0]]);
        log("CORINGA: " + j.getNome() + " escolheu " + nomes[escolha[0]].toUpperCase(), obterCorJogador(j));
    }

    private void vezDoBot(Jogador b) {
        Carta escolhida = b.getMao().stream()
                .filter(c -> c.podeJogarSobre(this.cartaNaMesa))
                .findFirst()
                .orElse(null);

        if (escolhida != null) {
            if (b.getMao().size() == 2) log(b.getNome() + ": UNO!", "#FF4500");
            acaoJogar(escolhida);
        } else {
            int contador = 0;
            Carta comprada;
            do {
                comprada = baralho.comprar();
                if (comprada != null) {
                    b.adicionarCarta(comprada);
                    contador++;
                } else break;
            } while (!comprada.podeJogarSobre(this.cartaNaMesa));

            log(b.getNome() + " comprou " + contador + " carta(s) ate servir", "#888888");

            if (comprada != null && comprada.podeJogarSobre(this.cartaNaMesa)) {
                acaoJogar(comprada);
            } else {
                proximoTurno();
                atualizarTela();
            }
        }
    }

    private void acaoComprar() {
        if (aguardandoConfirmacao) return;

        Jogador atual = jogadores.get(indiceTurno);
        if (atual.isBot()) return;

        boolean temOpcao = atual.getMao().stream().anyMatch(c -> c.podeJogarSobre(cartaNaMesa));
        if (temOpcao) {
            log("AVISO: Voce ja tem cartas validas!", "#FFFF00");
            return;
        }

        int contador = 0;
        Carta c;
        do {
            c = baralho.comprar();
            if (c != null) {
                atual.adicionarCarta(c);
                contador++;
            } else break;
        } while (c != null && !c.podeJogarSobre(cartaNaMesa));

        if (c != null) {
            log("Voce comprou " + contador + " carta(s) ate vir [" + obterValorFormatado(c) + " " + obterNaipeLog(c) + "]", "#FFFFFF");
        }
        atualizarTela();
    }

    private void mostrarVitoria(String n) {
        JOptionPane.showMessageDialog(this, n.toUpperCase() + " VENCEU!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private String obterNaipeLog(Carta c) {
        String corHex = switch (c.getCor()) {
            case "Vermelho" -> "#FF4444";
            case "Amarelo" -> "#FFD700";
            case "Verde" -> "#44FF44";
            case "Azul" -> "#4444FF";
            default -> "#FFFFFF";
        };

        boolean ehUno = (c instanceof CartaOficial);
        String nomeText = switch (c.getCor()) {
            case "Vermelho" -> ehUno ? "Vermelho" : "Copas";
            case "Amarelo" -> ehUno ? "Amarelo" : "Ouros";
            case "Verde" -> ehUno ? "Verde" : "Paus";
            case "Azul" -> ehUno ? "Azul" : "Espadas";
            default -> "Coringa";
        };

        return "<b style='color:" + corHex + "'>" + nomeText + "</b>";
    }

    private String obterValorFormatado(Carta c) {
        String v = c.getValor();
        boolean ehUno = (c instanceof CartaOficial);

        if (ehUno) {
            if (v.equals("J")) return "PULO";
            if (v.equals("Q")) return "INV";
            if (v.equals("K")) return "+2";
            return v;
        }
        return v; //convencional retorna J, Q, K
    }

    private String obterCorJogador(Jogador j) {
        if (!j.isBot()) return "#00FF00";
        if (j.getNome().contains("1")) return "#FF69B4";
        if (j.getNome().contains("2")) return "#1E90FF";
        return "#DA70D6";
    }

    private void log(String m, String corHex) {
        String icone = "";
        if (m.contains("Voce") || m.contains("a JOGOU") || m.contains("a escolheu")) icone = "P: ";
        else if (m.contains("Bot 1")) icone = "1: ";
        else if (m.contains("Bot 2")) icone = "2: ";
        else if (m.contains("Bot 3")) icone = "3: ";
        else if (m.contains("EFEITO")) icone = ">> ";

        String placar = "<small style='color:#888888'> [ " + jogadores.stream()
                .map(j -> j.getNome().charAt(0) + ":" + j.getMao().size())
                .collect(Collectors.joining(" | ")) + " ]</small>";

        String linha;
        if (m.contains("EFEITO")) {
            linha = "<div style='color:" + corHex + ";'>&nbsp;&nbsp;&nbsp;&nbsp;- " + m + " " + placar + "</div>";
        } else {
            linha = "<div style='color:" + corHex + ";'>" + icone + " <b>" + m + "</b> " + placar + "</div>";
        }

        try {
            HTMLDocument doc = (HTMLDocument) areaLog.getStyledDocument();
            HTMLEditorKit kit = (HTMLEditorKit) areaLog.getEditorKit();
            kit.insertHTML(doc, doc.getLength(), linha, 0, 0, null);
            areaLog.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}