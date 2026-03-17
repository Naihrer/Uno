import UI.MenuInicial;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Tenta aplicar o visual do Sistema Operacional (Windows, Mac ou Linux)
        // Isso deixa os botões, campos de texto e a JList muito mais bonitos.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se falhar ao carregar o visual moderno, ele usa o padrão do Java
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Inicia a aplicação pela tela de Menu/Configuração
        javax.swing.SwingUtilities.invokeLater(() -> {
            MenuInicial menu = new MenuInicial();
            menu.setVisible(true);
        });
    }
}