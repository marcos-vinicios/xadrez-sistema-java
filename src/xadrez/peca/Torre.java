package xadrez.peca;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Color;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez{

	
	public Torre(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}
	@Override
	public String toString() {
		return "T";
	}
	@Override
	public boolean[][] movimentosPossiveis(){
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0,0);
		//acima
		p.setValores(posicao.getLinha() -1, posicao.getColuna());
		while(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);
		}
		if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//esquerda
		p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		while(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);
		}
		if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//direita
		p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		while(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);
		}
		if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		//abaixo
		p.setValores(posicao.getLinha() + 1, posicao.getColuna());
		while(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() + 1);
		}
		if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		return mat;
	}
}
