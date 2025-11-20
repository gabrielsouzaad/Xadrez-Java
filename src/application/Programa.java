package application;


import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessPosicao;
import chess.PartidaDeXadrez;
import chess.PecaDeXadrez;

public class Programa {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		
		while (true) {
			try {
				UI.limparTela();
				UI.printPartida(partidaDeXadrez);
				System.out.println();
				System.out.print("Origem: ");
				ChessPosicao origem = UI.lerChessPosicao(sc);
			
				boolean[][] possivelMovimentacao = partidaDeXadrez.possivelMovimentacao(origem);
				UI.limparTela();
				UI.printTabuleiro(partidaDeXadrez.getPecas(), possivelMovimentacao);
				
				System.out.println();
				System.out.print("Destino: ");
				ChessPosicao destino = UI.lerChessPosicao(sc);

				
				PecaDeXadrez capturarPeca = partidaDeXadrez.movimentoDeXadrez(origem, destino);
				
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		
	}

}
