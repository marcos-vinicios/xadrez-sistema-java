package xadrez;

import java.security.InvalidParameterException;
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
import xadrez.peca.Rainha;
import xadrez.peca.Rei;
import xadrez.peca.Torre;

public class PartidaXadrez {
	
	private int time;
	private Color jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez valoresPassante;
	private PecaXadrez promovido;
	
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
	
	public PecaXadrez getValoresPassante() {
		return valoresPassante;
	}

	public PecaXadrez getPromovido() {
		return promovido;
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
	public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoOrigem){
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
			throw new ExeculsaoXadrez("Você não pode colocar em check");
		}
		
		PecaXadrez movePeca = (PecaXadrez)tabuleiro.peca(destino);
		
		//especial promovido
		promovido = null;
		if(movePeca instanceof Peao) {
			if((movePeca.getColor() == Color.WHITE && destino.getLinha() == 0) || (movePeca.getColor() == Color.WHITE && destino.getLinha() == 7)) {
				promovido = (PecaXadrez)tabuleiro.peca(destino);
				promovido = substituirPecaPromovida("Q");
			}
		}
		
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
	
		if(testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}else {
			proximoTime();
		}
		//em passant
		if(movePeca instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			valoresPassante =  movePeca;
		}
		else {
			valoresPassante = null;
		}
		
		return (PecaXadrez)capturaPeca;
	}
	public PecaXadrez substituirPecaPromovida(String tipo) {
		if(promovido == null) {
			throw new IllegalStateException("Não a peça para ser promovida. ");
		}
		if(!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") & !tipo.equals("Q")) {
			throw new InvalidParameterException("Invalido tipo de promoção.");
		}
		
		Posicao pos = promovido.getPosicaoXadrez().toPosicao();
		Peca p = tabuleiro.removePeca(pos);
		pecasNoTabuleiro.remove(p);
		
		PecaXadrez novaPeca = novaPeca(tipo, promovido.getColor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		
		return novaPeca;
	}
	
	private PecaXadrez novaPeca(String tipo, Color color) {
		if(tipo.equals("B")) return new Bispo(tabuleiro,color);
		if(tipo.equals("C")) return new Cavalo(tabuleiro,color);
		if(tipo.equals("Q")) return new Rainha(tabuleiro,color);
		return new Torre(tabuleiro,color);
	}
	
	private Peca fazerMove(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(origem);
		p.inclementarMoveContagem();
		Peca capturarPeca = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		
		if(capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			capturarPecas.add(capturarPeca);
		}
		//roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.inclementarMoveContagem();
		}
		//roque grande
				if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
					Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
					Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
					PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(origemTorre);
					tabuleiro.colocarPeca(torre, destinoTorre);
					torre.inclementarMoveContagem();
				}
		// em passam
		if(p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && capturarPeca == null) {
				Posicao peaoPosicao;
				if(p.getColor() == Color.WHITE) {
					peaoPosicao =  new Posicao(destino.getLinha() + 1, destino.getColuna());
				}
				else {
					peaoPosicao =  new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				capturarPeca = tabuleiro.removePeca(peaoPosicao);
				capturarPecas.add(capturarPeca);
				pecasNoTabuleiro.remove(capturarPeca);
			}
		}
		
		return capturarPeca;
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
		//roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
					Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
					Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
					PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(origemTorre);
					tabuleiro.colocarPeca(torre, origemTorre);
					torre.decrescendoMoveContagem();
				}
				//roque grande
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
							Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
							Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
							PecaXadrez torre = (PecaXadrez)tabuleiro.removePeca(destinoTorre);
							tabuleiro.colocarPeca(torre, origemTorre);
							torre.inclementarMoveContagem();
						}
		// em passam
		if(p instanceof Peao) {
					if(origem.getColuna() != destino.getColuna() && capturarPeca == valoresPassante) {
						PecaXadrez peao = (PecaXadrez)tabuleiro.removePeca(destino);
						Posicao peaoPosicao;
						if(p.getColor() == Color.WHITE) {
							peaoPosicao =  new Posicao(3, destino.getColuna());
						}
						else {
							peaoPosicao =  new Posicao(4, destino.getColuna());
						}
						tabuleiro.colocarPeca(peao, peaoPosicao);
					}
				}						
	}
	
	
	public void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.haPeca(posicao)) {
			throw new ExeculsaoXadrez("Não existe peça na posição de origem.");
		}
		if(jogadorAtual !=((PecaXadrez) tabuleiro.peca(posicao)).getColor()) {
			throw new ExeculsaoXadrez("Peça escolhida não é a sua.");
		}
		if(!tabuleiro.peca(posicao).existeAlgumMovimentoPeca()) {
			throw new ExeculsaoXadrez("Não existe movimentos possiveis para a peça escolhinda.");
		}
	}
	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new ExeculsaoXadrez("A peça escolhida não pode se mover para a posição de destino.");
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
		throw new IllegalStateException("Não existe " + color + " o rei no tabuleiro.");
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
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	
	private void iniciaPartida() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Color.WHITE));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Color.WHITE));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Color.WHITE));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Color.WHITE));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Color.WHITE,this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Color.WHITE,this));
		
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Color.BLACK));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Color.BLACK));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Color.BLACK));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Color.BLACK));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Color.BLACK,this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Color.BLACK,this));
		
		}
}
