package Modelo;

public class Jogo {
    public void pularProximo() {
        System.out.println("Ação: Próximo jogador pulado.");
    }
    public void inverterSentido() {
        System.out.println("Ação: Sentido invertido.");
    }
    public void proximoCompra(int q) {
        System.out.println("Ação: Próximo compra " + q + " cartas.");
    }
    public void pedirNovaCor() {
        System.out.println("Ação: Coringa jogado, cor alterada.");
    }
}