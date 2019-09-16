package xadrez.peca;

import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;
import xadrez.Color;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {
	
	private PartidaXadrez partidaXadrez;

	public Rei(Tabuleiro tabuleiro, Color color, PartidaXadrez partidaXadrez) {
		super(tabuleiro, color);
		this.partidaXadrez = partidaXadrez;
	}
	@Override
	public String toString() {
		return "R";
	}
	
	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getColor() != getColor();
	}
	
	private boolean testeTorreRoque(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getColor() == getColor() && p.getMoveContagem() ==0;
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
		
		// movimento especial castling
		if(getMoveContagem() == 0 && !partidaXadrez.getCheck()) {
			// movimento especial castling Reiside torre
			Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if(testeTorreRoque(posT1)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
		}
		//movemento especial castling rainhaside torre
		Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
		if(testeTorreRoque(posT2)) {
			Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
			Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
			Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
			if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
				mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
			}
		}
				
		return mat;
	}

	
}
