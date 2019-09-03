package tabuleiroJogo;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private Peca[][] pecas;//matriz de pecas

	public Tabuleiro(int linhas, int colunas) {
		if( linhas < 1 || colunas < 1) {
			throw new PlacaDeExecucao("Erro criando tabuleiro: é"
					+ " necessario que haja pelo menos 1 linha ou uma coluna");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];//criado manualmente para informa a matriz
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
	public Peca peca(int linha, int coluna) {
		if( !existePosicao(linha, coluna)) {
			throw new PlacaDeExecucao("Posicao não existe no tabuleiro");
		}
		return pecas[linha][coluna];
	}
	public Peca peca(Posicao posicao) {
		if( !existePosicao(posicao)) {
			throw new PlacaDeExecucao("Posicao não existe no tabuleiro");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void colocarPeca(Peca peca, Posicao posicao) {
		if(haPeca(posicao)) {
			throw new PlacaDeExecucao("já existe uma peça na posicão. " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removePeca(Posicao posicao) {
		if(!existePosicao(posicao)) {
			throw new PlacaDeExecucao("Posicao não existe no tabuleiro");
		}
		if(peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
	}
	
	
	private boolean existePosicao(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}
	
	public boolean existePosicao(Posicao posicao) {
		return existePosicao(posicao.getLinha(), posicao.getColuna());
	}
	public boolean haPeca(Posicao posicao) {
		if( !existePosicao(posicao)) {
			throw new PlacaDeExecucao("Posicao não existe no tabuleiro");
		}
		return peca(posicao) != null;
	}
}
