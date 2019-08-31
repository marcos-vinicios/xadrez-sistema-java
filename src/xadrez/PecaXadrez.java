package xadrez;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Tabuleiro;

public class PecaXadrez extends Peca{
	private Color color;

	public PecaXadrez(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
}
