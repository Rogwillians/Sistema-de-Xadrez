package Xadrez;

import Jogo.Peca;
import Jogo.Posicao;
import Jogo.Tabuleiro;
import Pecas.Xadrez.Rei;
import Pecas.Xadrez.Torre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PartidaXadrez{

    private Tabuleiro tabuleiro;
    private int turno;
    private Cor jogadorAtual;
    private boolean xeque;
    private boolean xequemate;


    private List<Peca> pecasNoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaXadrez(){
        tabuleiro = new Tabuleiro(8,8);
        turno = 1;
        jogadorAtual = Cor.BRANCO;

        posicaoInicial();
    }
    public int getTurno(){
        return turno;
    }
    public Cor getJogadorAtual(){
        return jogadorAtual;
    }
    public boolean getXeque(){ return xeque; }
    public boolean getXequemate(){ return xequemate; }
    public PecaXadrez[][] getPecas(){
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for(int i=0; i< tabuleiro.getLinhas(); i++){
            for(int j=0; j< tabuleiro.getColunas(); j++){
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i,j);
            }
        }
        return mat;
    }
    public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoInicial){
        Posicao posicao = posicaoInicial.paraPosicao();
        validarPosicaoInicial(posicao);
        return tabuleiro.peca(posicao).movimentosPossiveis();
    }

    public PecaXadrez movimentoXadrez(PosicaoXadrez posicaoInicial, PosicaoXadrez posicaoAlvo){
        Posicao inicial = posicaoInicial.paraPosicao();
        Posicao alvo = posicaoAlvo.paraPosicao();
        validarPosicaoInicial(inicial);
        validarPosicaoAlvo(inicial, alvo);
        Peca pecaCapturada = movimento(inicial, alvo);

        if (testeXeque(jogadorAtual)){
            desfazerMovimento(inicial, alvo, pecaCapturada);
            throw new ChessException("Você não pode se colocar em xeque");
        }
        xeque = (testeXeque(oponente(jogadorAtual))) ? true : false;

        if(testeXequemate(oponente(jogadorAtual))){
            xequemate = true;
        }
        else {
            proximoTurno();
        }
        return (PecaXadrez) pecaCapturada;
    }
    private Peca movimento(Posicao inicial, Posicao alvo){
        PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(inicial);
        p.aumentarContarMovimentos();
        Peca pecaCapturada = tabuleiro.removerPeca(alvo);
        tabuleiro.colocarPeca(p, alvo);

        if(pecaCapturada != null){
            pecasNoTabuleiro.remove(pecaCapturada);
            pecasCapturadas.add(pecaCapturada);
        }
        return pecaCapturada;
    }
    private void desfazerMovimento(Posicao origem, Posicao alvo, Peca pecaCapturada){
        PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(alvo);
        p.diminuirContarMovimentos();
        tabuleiro.colocarPeca(p, origem);

        if(pecaCapturada != null){
            tabuleiro.colocarPeca(pecaCapturada, alvo);
            pecasCapturadas.remove(pecaCapturada);
            pecasNoTabuleiro.add(pecaCapturada);
        }
    }

    private void validarPosicaoInicial(Posicao posicao){
        if(!tabuleiro.posicaoExiste(posicao)){
            throw new ChessException("não existe peca na posição de origem");
        }
        if(jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
            throw new ChessException("A peça escolhida não é sua");
        }
        if(!tabuleiro.peca(posicao).existeAlgumMovimento()){
            throw new ChessException("não existe movimentos possiveis para a peca escolhida");
        }
    }

    private void validarPosicaoAlvo(Posicao inicial, Posicao alvo){
        if(!tabuleiro.peca(inicial).movimentoPossivel(alvo)){
            throw new ChessException("a peça escolhida não pode se mover para posição de destino");
        }
    }
    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private Cor oponente(Cor cor){
        return(cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }

    private PecaXadrez rei(Cor cor){
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
        for(Peca p : list){
            if(p instanceof Rei){
                return (PecaXadrez)p;
            }
        }
        throw new IllegalStateException("Não existe um Rei " + cor + " no tabuleiro");
    }
    private boolean testeXeque(Cor cor){
        Posicao posicaoRei = rei(cor).getPosicaoXadrez().paraPosicao();
        List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
        for (Peca p : pecasOponentes){
            boolean[][] mat = p.movimentosPossiveis();
            if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]){
                return true;
            }
        }
        return false;
    }
    private boolean testeXequemate(Cor cor){
        if(!testeXeque(cor)){
            return false;
        }
        List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
        for(Peca p : list){
            boolean[][] mat = p.movimentosPossiveis();
            for(int i=0; i< tabuleiro.getLinhas(); i++){
                for (int j=0; j< tabuleiro.getColunas(); j++){
                    if(mat[i][j]){
                        Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().paraPosicao();
                        Posicao alvo = new Posicao(i, j);
                        Peca pecaCapturada = movimento(origem, alvo);
                        boolean testeXeque = testeXeque(cor);
                        desfazerMovimento(origem, alvo, pecaCapturada);
                        if(!testeXeque){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca){
        tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).paraPosicao());
        pecasNoTabuleiro.add(peca);
    }
    private void posicaoInicial(){
        colocarNovaPeca('h', 7, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('d', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));

        colocarNovaPeca('a', 8, new Rei(tabuleiro, Cor.PRETO));
        colocarNovaPeca('b', 8, new Torre(tabuleiro, Cor.PRETO));
    }

}
