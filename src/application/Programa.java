package application;


import boardgame.Tabuleiro;
import chess.PartidaDeXadrez;

public class Programa {

	public static void main(String[] args) {
		
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		UI.printTabuleiro(partidaDeXadrez.getPecas());
		
		
	}

}
