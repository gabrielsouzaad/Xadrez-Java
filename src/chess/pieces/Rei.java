package chess.pieces;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PartidaDeXadrez;
import chess.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

	private PartidaDeXadrez partidaDeXadrez;
	
	public Rei(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaDeXadrez) {
		super(tabuleiro, cor);
		this.partidaDeXadrez = partidaDeXadrez;
	}

	@Override
	public String toString() {
		return "R";
	}
	
	private boolean podeMover(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
				}
	
	private boolean testTorreRoque(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContagemMovimentos() == 0;
	}

	@Override
	public boolean[][] possibilidaDeMover() {
	    boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
	    
	    Posicao p = new Posicao(0,0);
	    
	    //acima
	    p.setValor(posicao.getLinha() - 1, posicao.getColuna());
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	  //abaixo
	    p.setValor(posicao.getLinha() + 1, posicao.getColuna());
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	  //esquerda
	    p.setValor(posicao.getLinha(), posicao.getColuna() - 1);
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	  //direita
	    p.setValor(posicao.getLinha(), posicao.getColuna() + 1);
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	    //noroeste
	    p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	  //nordeste
	    p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	    //suduoeste
	    p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	 	//sudeste
	    p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
	    if (getTabuleiro().posicaoExiste(p)&& podeMover(p)) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	    // Roque movimento especial
	    if (getContagemMovimentos() == 0 && !partidaDeXadrez.getCheck()) {
	    	// roque lado do Rei
	    	Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
	    	if (testTorreRoque(posT1)) {
	    		Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
	    		Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
	    		if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
	    			mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
	    		}

	    	}
	    	// roque lado da rainha
	    	Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
	    	if (testTorreRoque(posT2)) {
	    		Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
	    		Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
	    		Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
	    		if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
	    			mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
	    		}

	    	}
	    }
	    
	    return mat;
	}

}
