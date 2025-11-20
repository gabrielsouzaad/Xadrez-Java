package chess.pieces;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "R";
	}
	
	private boolean podeMover(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
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
	    
	    return mat;
	}

}
