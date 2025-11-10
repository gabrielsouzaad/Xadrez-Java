package chess;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pieces.Rei;
import chess.pieces.Torre;



public class PartidaDeXadrez {
	
	private Tabuleiro tabuleiro;
	
	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		iniciarSetup();
	}
	
	public PecaDeXadrez[][] getPecas() {
		PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
			
			}
		}
		return mat;
	}
	
	private void iniciarSetup() {
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.WHITE), new Posicao(2,1));
		tabuleiro.lugarPeca(new Rei(tabuleiro, Cor.BLACK), new Posicao(0,4));
		tabuleiro.lugarPeca(new Rei(tabuleiro, Cor.WHITE), new Posicao(7,4));
	}
}
