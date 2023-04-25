package Pecas.Xadrez;

import Jogo.Posicao;
import Jogo.Tabuleiro;
import Xadrez.Cor;
import Xadrez.PecaXadrez;

public class Torre extends PecaXadrez {
    public Torre(Tabuleiro tabuleiro, Cor cor){
        super(tabuleiro, cor);
    }

    @Override
    public String toString(){
        return "T";
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        //acima
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() );
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() - 1);
        }
        if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }


        //baixo
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() );
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() + 1);
        }
        if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }


        //esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1 );
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() - 1);
        }
        if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }

        //direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
            mat[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() + 1);
        }
        if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)){
            mat[p.getLinha()][p.getColuna()] = true;
        }
        return mat;

    }
}
