package application;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessPosicao;
import chess.PartidaDeXadrez;
import chess.PecaDeXadrez;

public class Programa {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		List<PecaDeXadrez> capturada = new ArrayList<PecaDeXadrez>();
		
		while (!partidaDeXadrez.getCheckMate()) {
			try {
				UI.limparTela();
				UI.printPartida(partidaDeXadrez, capturada);
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
				
				if(capturarPeca != null) {
					capturada.add(capturarPeca);
				}
				
				if (partidaDeXadrez.getPromocao() != null) {
					System.out.print("Entre com a peça promovida (B/C/RA/T): ");
					String tipo = sc.nextLine().toUpperCase();
					while (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("RA")) {
						System.out.print("Valor inválido! Entre com a peça promovida (B/C/RA/T): ");
						tipo = sc.nextLine().toUpperCase();
					}
					partidaDeXadrez.substituirPecaPromovida(tipo);
				}
				
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
		UI.limparTela();
		UI.printPartida(partidaDeXadrez, capturada);
		
	}

}
