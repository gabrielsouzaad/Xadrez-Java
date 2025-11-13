package chess;

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
	
	
	private void colocarPecaNova(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.lugarPeca(peca, new ChessPosicao(coluna, linha).toPosicao());
	}
	
	private void iniciarSetup() {
		colocarPecaNova('b', 6, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('e', 8,new Rei(tabuleiro, Cor.BLACK));
		colocarPecaNova('e', 1,new Rei(tabuleiro, Cor.WHITE));
	}
}
