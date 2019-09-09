package xadrez.peca;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Color;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	public Rei(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}
	@Override
	public String toString() {
		return "R";
	}
	
	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p != null || p.getColor() != getColor();
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0,0);
		
		//acima
		p.setValores(posicao.getLinha() - 1, posicao.getColuna());
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//abaixo
		p.setValores(posicao.getLinha() + 1, posicao.getColuna());
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		//esquerda
		p.setValores(posicao.getLinha(), posicao.getColuna()-1);
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		//direita
		p.setValores(posicao.getLinha(), posicao.getColuna()+1);
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		//diagonal do rei
		//Noroeste
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		//nordeste
		p.setValores(posicao.getLinha() - 1, posicao.getColuna() -1);
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		//sudoeste
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() -1);
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
		//sudeste
		p.setValores(posicao.getLinha() + 1, posicao.getColuna() +1);
		if(getTabuleiro().existePosicao(p) && podeMover(p)) {
		mat[p.getLinha()][p.getColuna()] = true;
		}
				
		return mat;
	}

	
}
