package boardgame;

public abstract class Peca {
	
	protected Posicao posicao;
	private Tabuleiro tabuleiro;
	
	public Peca(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
		posicao = null;
	}

	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public abstract boolean [][] possibilidaDeMover(); 	
	
	public boolean possibilidaDeMover(Posicao posicao) {
		return possibilidaDeMover()[posicao.getLinha()][posicao.getColuna()];
	}
	
	public boolean possivelMovimento() {
		boolean[][] mat = possibilidaDeMover();
		for (int i = 0; i<mat.length; i++) {
			for (int j = 0; j<mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
		
	}
}
