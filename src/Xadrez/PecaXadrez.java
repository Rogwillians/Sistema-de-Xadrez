package Xadrez;

import Jogo.Peca;
import Jogo.Posicao;
import Jogo.Tabuleiro;

public abstract class PecaXadrez extends Peca {
    private Cor cor;
    private int contarMovimentos;

    public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }


    public PosicaoXadrez getPosicaoXadrez(){
        return PosicaoXadrez.daPosicao(posicao);
    }

    public Cor getCor() {
        return cor;
    }

    public int getContarMovimentos(){
        return contarMovimentos;
    }

    public void aumentarContarMovimentos(){
        contarMovimentos++;
    }

    public void diminuirContarMovimentos(){
        contarMovimentos--;
    }

    protected boolean existePecaOponente(Posicao posicao){
        PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
        return p != null && p.getCor() != cor;
    }


}
