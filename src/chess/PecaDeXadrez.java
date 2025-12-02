package chess;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;

public abstract class PecaDeXadrez extends Peca{
	
	private Cor cor;
	private int contagemMovimentos;

	public PecaDeXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getContagemMovimentos() {
		return contagemMovimentos;
	}
	
	public void aumentarContagemMovimentos() {
		contagemMovimentos++;
	}
	
	public void diminuirContagemMovimentos() {
		contagemMovimentos--;
	}
	
	public ChessPosicao getChessPosicao() {
		return ChessPosicao.daPosicao(posicao);
	}

	protected boolean existePecaOponente(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}

	
	
	
	
}
