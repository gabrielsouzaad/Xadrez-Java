package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import chess.pieces.Bispo;
import chess.pieces.Cavalo;
import chess.pieces.Peao;
import chess.pieces.Rainha;
import chess.pieces.Rei;
import chess.pieces.Torre;

public class PartidaDeXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaDeXadrez enPassantVuneravel;

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

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaDeXadrez getEnPassantVuneravel() {
		return enPassantVuneravel;
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

	public boolean[][] possivelMovimentacao(ChessPosicao posicaoDeOrigem) {
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

		if (testCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturarPeca);
			throw new ChessException("Você não pode se colocar em check");
		}

		PecaDeXadrez pecaMovida = (PecaDeXadrez) tabuleiro.peca(destino);

		check = (testCheck(oponente(jogadorAtual))) ? true : false;

		if (testCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}

		// movimentação especial en passant
		if (pecaMovida instanceof Peao
				&& (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			enPassantVuneravel = pecaMovida;
		} else {
			enPassantVuneravel = null;
		}

		return (PecaDeXadrez) capturarPeca;
	}

	private Peca moverPeca(Posicao origem, Posicao destino) {
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(origem);
		p.aumentarContagemMovimentos();
		Peca capturarPeca = tabuleiro.removerPeca(destino);
		tabuleiro.lugarPeca(p, destino);

		if (capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			pecasCapturadas.add(capturarPeca);

		}

		// movimento especial roque do lado do rei
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.lugarPeca(torre, destinoT);
			torre.aumentarContagemMovimentos();
		}

		// movimento especial roque do lado da rainha
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.lugarPeca(torre, destinoT);
			torre.aumentarContagemMovimentos();
		}

		// movimentacao especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && capturarPeca == null) {
				Posicao peaoPosicao;
				if (p.getCor() == Cor.WHITE) {
					peaoPosicao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					peaoPosicao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}

				capturarPeca = tabuleiro.removerPeca(peaoPosicao);
				pecasCapturadas.add(capturarPeca);
				pecasNoTabuleiro.remove(capturarPeca);
			}
		}

		return capturarPeca;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturarPeca) {
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(destino);
		p.diminuirContagemMovimentos();
		tabuleiro.lugarPeca(p, origem);

		if (capturarPeca != null) {
			tabuleiro.lugarPeca(capturarPeca, destino);
			pecasCapturadas.remove(capturarPeca);
			pecasNoTabuleiro.add(capturarPeca);
		}
		// movimento especial roque do lado do rei
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.lugarPeca(torre, origemT);
			torre.diminuirContagemMovimentos();
		}

		// movimento especial roque do lado da rainha
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.lugarPeca(torre, origemT);
			torre.diminuirContagemMovimentos();
		}

		// movimentacao especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && capturarPeca == enPassantVuneravel) {
				PecaDeXadrez peao = (PecaDeXadrez)tabuleiro.removerPeca(destino);
				Posicao peaoPosicao;
				if (p.getCor() == Cor.WHITE) {
					peaoPosicao = new Posicao(3, destino.getColuna());
				} else {
					peaoPosicao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.lugarPeca(peao, peaoPosicao);
	
			}
		}

	}

	private void validacaoPosicaoDeOrigem(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new ChessException("Não existe peça nessa posição");
		}
		if (jogadorAtual != ((PecaDeXadrez) tabuleiro.peca(posicao)).getCor()) {
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

	private Cor oponente(Cor cor) {
		return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	private PecaDeXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaDeXadrez) p;
			}
		}
		throw new IllegalStateException("Não existe o rei da cor " + cor + " no tabuleiro");
	}

	private boolean testCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getChessPosicao().toPosicao();
		List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecasOponente) {
			boolean[][] mat = p.possibilidaDeMover();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Cor cor) {
		if (!testCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.possibilidaDeMover();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaDeXadrez) p).getChessPosicao().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca capturarPeca = moverPeca(origem, destino);
						boolean testCheck = testCheck(cor);
						desfazerMovimento(origem, destino, capturarPeca);
						if (!testCheck) {
							return false;
						}
					}
				}
			}

		}
		return true;

	}

	private void colocarPecaNova(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.lugarPeca(peca, new ChessPosicao(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void iniciarSetup() {
		colocarPecaNova('a', 1, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('b', 1, new Cavalo(tabuleiro, Cor.WHITE));
		colocarPecaNova('c', 1, new Bispo(tabuleiro, Cor.WHITE));
		colocarPecaNova('d', 1, new Rainha(tabuleiro, Cor.WHITE));
		colocarPecaNova('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('f', 1, new Bispo(tabuleiro, Cor.WHITE));
		colocarPecaNova('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
		colocarPecaNova('h', 1, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('a', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('b', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('c', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('d', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('e', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('f', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('g', 2, new Peao(tabuleiro, Cor.WHITE, this));
		colocarPecaNova('h', 2, new Peao(tabuleiro, Cor.WHITE, this));

		colocarPecaNova('a', 8, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
		colocarPecaNova('c', 8, new Bispo(tabuleiro, Cor.BLACK));
		colocarPecaNova('d', 8, new Rainha(tabuleiro, Cor.BLACK));
		colocarPecaNova('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('f', 8, new Bispo(tabuleiro, Cor.BLACK));
		colocarPecaNova('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
		colocarPecaNova('h', 8, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('a', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('b', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('c', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('d', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('e', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('f', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('g', 7, new Peao(tabuleiro, Cor.BLACK, this));
		colocarPecaNova('h', 7, new Peao(tabuleiro, Cor.BLACK, this));

	}
}
