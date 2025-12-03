package chess.pieces;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.Cor;
import chess.PecaDeXadrez;

public class Bispo extends PecaDeXadrez {

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] possibilidaDeMover() {
	    boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
	    
	    Posicao p = new Posicao(0,0);
	    
	    //noroeste
	    p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
	    while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
	    	mat[p.getLinha()][p.getColuna()] = true;
	    	p.setValor(p.getLinha() - 1, p.getColuna() - 1);
	    }
	    if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p) ) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	  //nordeste
	    p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
	    while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
	    	mat[p.getLinha()][p.getColuna()] = true;
	    	p.setValor(p.getLinha() - 1, p.getColuna() + 1);
	    }
	    if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p) ) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	    //sudeste
	    p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
	    while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
	    	mat[p.getLinha()][p.getColuna()] = true;
	    	p.setValor(p.getLinha() + 1, p.getColuna() + 1);
	    }
	    if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p) ) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	    //sudoeste
	    p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
	    while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)){
	    	mat[p.getLinha()][p.getColuna()] = true;
	    	p.setValor(p.getLinha() + 1, p.getColuna() - 1);
	    }
	    if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p) ) {
	    	mat[p.getLinha()][p.getColuna()] = true;
	    }
	    
	    return mat;
	}
	
	
}
