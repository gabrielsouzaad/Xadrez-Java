package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pieces.Rei;
import chess.pieces.Torre;



public class PartidaDeXadrez {
	
	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();
	
	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.WHITE;
		iniciarSetup();
	}
	
	public int getTurno() {
		return turno;
	}
	
	public Cor getJogadorAtual() {
		return jogadorAtual;
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
	public boolean [][] possivelMovimentacao(ChessPosicao posicaoDeOrigem){
		Posicao posicao = posicaoDeOrigem.toPosicao();
		validacaoPosicaoDeOrigem(posicao);
		return tabuleiro.peca(posicao).possibilidaDeMover();
	}
	
	public PecaDeXadrez movimentoDeXadrez(ChessPosicao posicaoDeOrigem, ChessPosicao posicaoDeDestino) {
		Posicao origem = posicaoDeOrigem.toPosicao();
		Posicao destino = posicaoDeDestino.toPosicao();
		validacaoPosicaoDeOrigem(origem);
		validacaoPosicaoDestino(origem, destino);
		Peca capturarPeca = moverPeca(origem, destino);
		proximoTurno();
		return (PecaDeXadrez)capturarPeca;
	}
	
	private Peca moverPeca(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca capturarPeca = tabuleiro.removerPeca(destino);
		tabuleiro.lugarPeca(p, destino);
		
		if(capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			pecasCapturadas.add(capturarPeca);
			
		}
		
		
		return capturarPeca;
	}
	
	private void validacaoPosicaoDeOrigem(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new ChessException("Não existe peça nessa posição");
		}
		if (jogadorAtual != ((PecaDeXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new ChessException("A peça escolhida não é sua");
		}
		if (!tabuleiro.peca(posicao).possivelMovimento()) {
			throw new ChessException("Não existe movimento disponivel para a peça escolhida");
		}
	}
	
	private void validacaoPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).possibilidaDeMover(destino)) {
			throw new ChessException("A peça escolhida não pode se mover para posição de destino");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	
	private void colocarPecaNova(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.lugarPeca(peca, new ChessPosicao(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
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
