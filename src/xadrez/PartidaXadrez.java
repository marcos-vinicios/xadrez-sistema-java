package xadrez;

import java.util.ArrayList;
import java.util.IllegalFormatWidthException;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.peca.Bispo;
import xadrez.peca.Cavalo;
import xadrez.peca.Peao;
import xadrez.peca.Rei;
import xadrez.peca.Torre;

public class PartidaXadrez {
	
	private int time;
	private Color jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> capturarPecas = new ArrayList<>();
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8,8);
		time = 1;
		jogadorAtual = Color.WHITE;
		iniciaPartida();
	}
	
	public int getTime() {
		return time;
	}
	public Color getJogadorAtual() {
		return jogadorAtual;
	}
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i=0; i<tabuleiro.getLinhas(); i++) {
			for(int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoOrigem){/*verificar*/
		Posicao posicao = posicaoOrigem.toPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	
	
	public PecaXadrez execultarXadrezMove(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao destino = posicaoDestino.toPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem,destino);
		Peca capturaPeca = fazerMove(origem, destino);
		if( testeCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturaPeca);
			throw new ExeculsaoXadrez("Voc� n�o pode colocar em check");
		}
		
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		if(testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}else {
			proximoTime();
		}
		proximoTime();
		return (PecaXadrez) capturaPeca;
	}
	private Peca fazerMove(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(origem);
		p.inclementarMoveContagem();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		
		if(pecaCapturada !=null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			capturarPecas.add(pecaCapturada);
		}
		
		return pecaCapturada;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturarPeca) {
		
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(destino);
		p.decrescendoMoveContagem();
		tabuleiro.colocarPeca(p, origem);
		
		if(capturarPeca != null) {
			tabuleiro.colocarPeca(capturarPeca, destino);
			capturarPecas.remove(capturarPeca);
			pecasNoTabuleiro.add(capturarPeca);
		}
	}
	
	public void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.haPeca(posicao)) {
			throw new ExeculsaoXadrez("N�o existe pe�a na posi��o de origem.");
		}
		if(jogadorAtual !=((PecaXadrez) tabuleiro.peca(posicao)).getColor()) {
			throw new ExeculsaoXadrez("Pe�a escolhida n�o � a sua.");
		}
		if(!tabuleiro.peca(posicao).existeAlgumMovimentoPeca()) {
			throw new ExeculsaoXadrez("N�o existe movimentos possiveis para a pe�a escolhinda.");
		}
	}
	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new ExeculsaoXadrez("A pe�a escolhida n�o pode se mover para a posi��o de destino.");
		}
	}
	
	private void proximoTime() {
		time++;
		jogadorAtual = (jogadorAtual == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	private Color oponente(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private PecaXadrez rei(Color color) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getColor() == color).collect(Collectors.toList());
		for (Peca p : list) {
			if(p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("N�o existe " + color + " o rei no tabuleiro.");
	}
	
	private boolean testeCheckMate(Color color) {
		if(!testeCheck(color)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getColor() == color).collect(Collectors.toList());
		for(Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for(int i=0; i<tabuleiro.getLinhas(); i++) {
				for( int j=0; j<tabuleiro.getColunas(); j++) {
					if(mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i,j);
						Peca capturarPeca = fazerMove(origem, destino);
						boolean testeCheck = testeCheck(color);
						desfazerMovimento(origem, destino, capturarPeca);
						if(!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean testeCheck(Color color) {
		Posicao posicaoRei = rei(color).getPosicaoXadrez().toPosicao();
		List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getColor() == oponente(color)).collect(Collectors.toList());
		for(Peca p : pecasOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	
	private void iniciaPartida() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Color.WHITE));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Color.WHITE));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Color.WHITE));
		
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Color.BLACK));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Color.BLACK));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Color.BLACK));
		
		}
}
