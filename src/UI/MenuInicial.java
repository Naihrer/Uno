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
    private List<Jogador> jogadoresConfigurados;
    private JTextField campoNome;
    private JComboBox<String> comboBaralho;

    public MenuInicial() {
        jogadoresConfigurados = new ArrayList<>();
        modeloLista = new DefaultListModel<>();
        listaVisual = new JList<>(modeloLista);

        setTitle("Configuração de Partida - UNO");
        setSize(400, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel Superior
        JPanel painelEntrada = new JPanel(new GridLayout(4, 1, 5, 5));
        painelEntrada.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        campoNome = new JTextField();
        // LIMPO: Removido "Modelo.Jogador"
        JButton btnAddHumano = new JButton("Adicionar Jogador Real");
        JButton btnAddBot = new JButton("Adicionar Bot");

        // LIMPO: Removido "Modelo.Baralho" das opções
        String[] opcoesBaralho = {"Baralho Convencional (Poker)", "Baralho Oficial (UNO)"};
        comboBaralho = new JComboBox<>(opcoesBaralho);

        // LIMPO: Removido "Modelo.Jogador"
        painelEntrada.add(new JLabel("Nome do Jogador:"));
        painelEntrada.add(campoNome);

        JPanel painelBotoesAdd = new JPanel(new GridLayout(1, 2, 5, 5));
        painelBotoesAdd.add(btnAddHumano);
        painelBotoesAdd.add(btnAddBot);
        painelEntrada.add(painelBotoesAdd);

        JPanel painelCombo = new JPanel(new BorderLayout());
        // LIMPO: Removido "Modelo.Baralho"
        painelCombo.add(new JLabel("Tipo de Baralho: "), BorderLayout.WEST);
        painelCombo.add(comboBaralho, BorderLayout.CENTER);
        painelEntrada.add(painelCombo);

        add(painelEntrada, BorderLayout.NORTH);

        JScrollPane scrollLista = new JScrollPane(listaVisual);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Jogadores na Sala"));
        add(scrollLista, BorderLayout.CENTER);

        JPanel painelAcoes = new JPanel(new FlowLayout());
        JButton btnLimpar = new JButton("Limpar Lista");
        JButton btnIniciar = new JButton("INICIAR JOGO");
        btnIniciar.setBackground(new Color(0, 150, 0));
        btnIniciar.setForeground(Color.WHITE);

        painelAcoes.add(btnLimpar);
        painelAcoes.add(btnIniciar);
        add(painelAcoes, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnAddHumano.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            if (!nome.isEmpty()) {
                adicionarJogador(nome, false);
                campoNome.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Digite um nome!");
            }
        });

        btnAddBot.addActionListener(e -> {
            int numBot = (int) jogadoresConfigurados.stream().filter(Jogador::isBot).count() + 1;
            adicionarJogador("Bot " + numBot, true);
        });

        btnLimpar.addActionListener(e -> {
            jogadoresConfigurados.clear();
            modeloLista.clear();
        });

        btnIniciar.addActionListener(e -> {
            if (jogadoresConfigurados.size() >= 2) {
                iniciarPartida();
            } else {
                JOptionPane.showMessageDialog(this, "Adicione pelo menos 2 jogadores!");
            }
        });
    }

    private void adicionarJogador(String nome, boolean ehBot) {
        Jogador j = new Jogador(nome, ehBot);
        jogadoresConfigurados.add(j);
        modeloLista.addElement(nome + (ehBot ? " (Bot)" : " (Humano)"));
    }

    private void iniciarPartida() {
        Baralho b;

        if (comboBaralho.getSelectedIndex() == 0) {
            b = new BaralhoConvencional();
        } else {
            b = new BaralhoOficial();
        }

        b.criarBaralho();
        b.embaralhar();

        for (Jogador j : jogadoresConfigurados) {
            j.getMao().clear();
            for (int i = 0; i < 7; i++) j.adicionarCarta(b.comprar());
        }

        Carta inicial = b.comprar();
        while(inicial != null && inicial.isCoringa()) {
            inicial = b.comprar();
        }

        final Carta mesaFixa = inicial;
        new TelaJogo(jogadoresConfigurados, mesaFixa, b).setVisible(true);
        this.dispose();
    }
}