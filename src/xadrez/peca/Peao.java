package xadrez.peca;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Color;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {
	
	private PartidaXadrez partidaXadrez;

	public Peao(Tabuleiro tabuleiro, Color color,PartidaXadrez partidaXadrez ) {
		super(tabuleiro, color);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		
		if(getColor() == Color.WHITE) {
			p.setValores(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p) &&
					getTabuleiro().existePosicao(p2) && !getTabuleiro().haPeca(p2) && getMoveContagem() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			//en passant
			if(posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);                            //falta .getValores
				if(getTabuleiro().existePosicao(esquerda) && existeOponentePeca(esquerda) && getTabuleiro().peca(esquerda) == partidaXadrez.getValoresPassante()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);                            //falta .getValores
				if(getTabuleiro().existePosicao(direita) && existeOponentePeca(direita) && getTabuleiro().peca(direita) == partidaXadrez.getValoresPassante()) {
					mat[direita.getLinha() - 1][direita.getColuna()] = true;
				}
			}
		}
		else {
			p.setValores(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().existePosicao(p) && !getTabuleiro().haPeca(p) &&
					getTabuleiro().existePosicao(p2) && !getTabuleiro().haPeca(p2) && getMoveContagem() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if(getTabuleiro().existePosicao(p) && existeOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			//en passant
			if(posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);                            //falta .getValores
				if(getTabuleiro().existePosicao(esquerda) && existeOponentePeca(esquerda) && getTabuleiro().peca(esquerda) == partidaXadrez.getValoresPassante()) {
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);                            //falta .getValores
				if(getTabuleiro().existePosicao(direita) && existeOponentePeca(direita) && getTabuleiro().peca(direita) == partidaXadrez.getValoresPassante()) {
					mat[direita.getLinha() + 1][direita.getColuna()] = true;
				}
			}
			
		}
		return mat;
	}
	@Override
	public String toString() {
		return "P";
	}

}
