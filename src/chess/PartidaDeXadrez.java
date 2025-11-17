package chess;

import boardgame.Peca;
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
	
	public PecaDeXadrez movimentoDeXadrez(ChessPosicao posicaoDeOrigem, ChessPosicao posicaoDeDestino) {
		Posicao origem = posicaoDeOrigem.toPosicao();
		Posicao destino = posicaoDeDestino.toPosicao();
		validacaoPosicaoDeOrigem(origem);
		Peca capturarPeca = moverPeca(origem, destino);
		return (PecaDeXadrez)capturarPeca;
	}
	
	private Peca moverPeca(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca capturarPeca = tabuleiro.removerPeca(destino);
		tabuleiro.lugarPeca(p, destino);
		return capturarPeca;
	}
	
	private void validacaoPosicaoDeOrigem(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new ChessException("Não existe peça nessa posição");
		}
		if (!tabuleiro.peca(posicao).possivelMovimento()) {
			throw new ChessException("Não existe movimento disponivel para a peça escolhida");
		}
	}

	
	private void colocarPecaNova(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.lugarPeca(peca, new ChessPosicao(coluna, linha).toPosicao());
	}
	
	private void iniciarSetup() {
		colocarPecaNova('c', 1, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('c', 2, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('d', 2, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('e', 2, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('e', 1, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('d', 1, new Torre(tabuleiro, Cor.WHITE));

		colocarPecaNova('c', 7, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('c', 8, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('d', 7, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('e', 7, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('e', 8, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('d', 8, new Rei(tabuleiro, Cor.BLACK));
	}
}
