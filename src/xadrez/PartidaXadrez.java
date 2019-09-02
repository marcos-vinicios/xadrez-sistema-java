package xadrez;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.peca.Rei;
import xadrez.peca.Torre;

public class PartidaXadrez {
	
	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8,8);
		iniciaPartida();
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
	private void iniciaPartida() {
		tabuleiro.colocarPeca(new Torre(tabuleiro, Color.WHITE),new Posicao(2,1));
		tabuleiro.colocarPeca(new Rei(tabuleiro,Color.BLACK),new Posicao(2 ,1));
		tabuleiro.colocarPeca(new Rei(tabuleiro,Color.BLACK),new Posicao(7 ,4));
	}
}
