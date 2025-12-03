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
		
		if (testCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturarPeca);
			throw new ChessException("Você não pode se colocar em check");
		}
		
		check = (testCheck(oponente(jogadorAtual))) ? true : false;
		
		if(testCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		
		return (PecaDeXadrez)capturarPeca;
	}
	
	private Peca moverPeca(Posicao origem, Posicao destino) {
		PecaDeXadrez p = (PecaDeXadrez)tabuleiro.removerPeca(origem);
		p.aumentarContagemMovimentos();
		Peca capturarPeca = tabuleiro.removerPeca(destino);
		tabuleiro.lugarPeca(p, destino);
		
		if(capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			pecasCapturadas.add(capturarPeca);
			
		}
		
		
		return capturarPeca;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturarPeca) {
		PecaDeXadrez p = (PecaDeXadrez)tabuleiro.removerPeca(destino);
		p.diminuirContagemMovimentos();
		tabuleiro.lugarPeca(p, origem);
		
		if (capturarPeca != null) {
			tabuleiro.lugarPeca(capturarPeca, destino);
			pecasCapturadas.remove(capturarPeca);
			pecasNoTabuleiro.add(capturarPeca);	
		}
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

	private Cor oponente(Cor cor) {
		return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}
	
	private PecaDeXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x ->((PecaDeXadrez)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			if (p instanceof Rei) {
				return (PecaDeXadrez)p;
			}
		}
		throw new IllegalStateException("Não existe o rei da cor " + cor + " no tabuleiro");
	}
	
	private boolean testCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getChessPosicao().toPosicao();
		List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for (Peca p : pecasOponente) {
			boolean [][] mat = p.possibilidaDeMover();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Cor cor) {
		if(!testCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			boolean[][] mat = p.possibilidaDeMover();
			for (int i=0; i < tabuleiro.getLinhas(); i++) {
				for( int j=0; j < tabuleiro.getColunas(); j++) {
					if(mat[i][j]) {
						Posicao origem = ((PecaDeXadrez)p).getChessPosicao().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca capturarPeca = moverPeca(origem, destino);
						boolean testCheck = testCheck(cor);
						desfazerMovimento(origem, destino, capturarPeca);
						if(!testCheck) {
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
		colocarPecaNova('e', 1, new Rei(tabuleiro, Cor.WHITE));
		colocarPecaNova('f', 1, new Bispo(tabuleiro, Cor.WHITE));
		colocarPecaNova('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
		colocarPecaNova('h', 1, new Torre(tabuleiro, Cor.WHITE));
		colocarPecaNova('a', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('b', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('c', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('d', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('e', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('f', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('g', 2, new Peao(tabuleiro, Cor.WHITE));
		colocarPecaNova('h', 2, new Peao(tabuleiro, Cor.WHITE));
	
		colocarPecaNova('a', 8, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
		colocarPecaNova('c', 8, new Bispo(tabuleiro, Cor.BLACK));
		colocarPecaNova('d', 8, new Rainha(tabuleiro, Cor.BLACK));
		colocarPecaNova('e', 8, new Rei(tabuleiro, Cor.BLACK));
		colocarPecaNova('f', 8, new Bispo(tabuleiro, Cor.BLACK));
		colocarPecaNova('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
		colocarPecaNova('h', 8, new Torre(tabuleiro, Cor.BLACK));
		colocarPecaNova('a', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('b', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('c', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('d', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('e', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('f', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('g', 7, new Peao(tabuleiro, Cor.BLACK));
		colocarPecaNova('h', 7, new Peao(tabuleiro, Cor.BLACK));
		
	
	
	}
}
