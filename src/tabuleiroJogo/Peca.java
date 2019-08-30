package tabuleiroJogo;

public class Peca {
	protected Posicao posicao;
	private Tabuleiro tabuleiro;
	
	public Peca(Tabuleiro tabuleiro) {
			this.tabuleiro = tabuleiro;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}
	
}
