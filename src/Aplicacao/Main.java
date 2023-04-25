package Aplicacao;

import Jogo.Tabuleiro;
import Xadrez.ChessException;
import Xadrez.PartidaXadrez;
import Xadrez.PecaXadrez;
import Xadrez.PosicaoXadrez;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PartidaXadrez partidaXadrez = new PartidaXadrez();
        List<PecaXadrez> capturada = new ArrayList<>();

        while(!partidaXadrez.getXequemate()){
            try {
                UI.limparTela();
                UI.printPartida(partidaXadrez, capturada);
                System.out.println();
                System.out.print("Origem: ");
                PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

                boolean[][] movimentosPossiveis = partidaXadrez.movimentosPossiveis(origem);


                System.out.println();
                System.out.println("Alvo: ");
                PosicaoXadrez alvo = UI.lerPosicaoXadrez(sc);

                PecaXadrez pecaCapturada = partidaXadrez.movimentoXadrez(origem, alvo);

                if (pecaCapturada != null){
                    capturada.add(pecaCapturada);
                }
            }
            catch (ChessException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e ){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            UI.limparTela();
            UI.printPartida(partidaXadrez, capturada);
        }
    }
}