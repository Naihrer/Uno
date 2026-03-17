package UI;

import Espec.BaralhoConvencional;
import Espec.BaralhoOficial;
import Modelo.Baralho;
import Modelo.Carta;
import Modelo.Jogador;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuInicial extends JFrame {
    private DefaultListModel<String> modeloLista;
    private JList<String> listaVisual;
    private List<Jogador> jogadores;
    private JTextField campoNome;
    private JComboBox<String> comboBaralho;

    public MenuInicial() {
        jogadores = new ArrayList<>();
        modeloLista = new DefaultListModel<>();
        listaVisual = new JList<>(modeloLista);

        setTitle("Configuracao de Partida");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(4, 1));
        campoNome = new JTextField();
        JButton btnHumano = new JButton("Adicionar Jogador");
        JButton btnBot = new JButton("Adicionar Robo");

        String[] opcoes = {"Baralho Comum", "Baralho UNO"};
        comboBaralho = new JComboBox<>(opcoes);

        painelEntrada.add(new JLabel("Nome:"));
        painelEntrada.add(campoNome);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2));
        painelBotoes.add(btnHumano);
        painelBotoes.add(btnBot);
        painelEntrada.add(painelBotoes);
        painelEntrada.add(comboBaralho);

        add(painelEntrada, BorderLayout.NORTH);
        add(new JScrollPane(listaVisual), BorderLayout.CENTER);

        JButton btnIniciar = new JButton("INICIAR");
        btnIniciar.addActionListener(e -> iniciar());
        add(btnIniciar, BorderLayout.SOUTH);

        btnHumano.addActionListener(e -> {
            String nome = campoNome.getText();
            if (!nome.isEmpty()) {
                Jogador j = new Jogador(nome, false);
                jogadores.add(j);
                modeloLista.addElement(nome + " (Humano)");
                campoNome.setText("");
            }
        });

        btnBot.addActionListener(e -> {
            String nomeBot = "Robo " + (jogadores.size() + 1);
            Jogador j = new Jogador(nomeBot, true);
            jogadores.add(j);
            modeloLista.addElement(nomeBot);
        });
    }

    private void iniciar() {
        if (jogadores.size() < 2) {
            JOptionPane.showMessageDialog(this, "Adicione 2 jogadores!");
            return;
        }

        Baralho b;
        if (comboBaralho.getSelectedIndex() == 0) {
            b = new BaralhoConvencional();
        } else {
            b = new BaralhoOficial();
        }

        b.criarBaralho();
        b.embaralhar();

        for (int i = 0; i < jogadores.size(); i++) {
            Jogador j = jogadores.get(i);
            j.getMao().clear();
            for (int c = 0; c < 7; c++) {
                j.adicionarCarta(b.comprar());
            }
        }

        Carta inicial = b.comprar();
        while (inicial != null && inicial.isCoringa()) {
            inicial = b.comprar();
        }

        new TelaJogo(jogadores, inicial, b).setVisible(true);
        this.dispose();
    }
}