package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import tabuleiroJogo.Tabuleiro;
import xadrez.ExeculsaoXadrez;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Programa {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturar = new ArrayList<>();
		
		
		
		while(!partidaXadrez.getCheckMate()) {
			try {
			UI.limparTela();
			UI.imprimirPartida(partidaXadrez, capturar);;
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);
			
			boolean[][] movimentosPossiveis = partidaXadrez.movimentosPossiveis(origem);
			UI.limparTela();
			UI.printTabuleiro(partidaXadrez.getPecas(), movimentosPossiveis);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);
			
			PecaXadrez capturePeca = partidaXadrez.execultarXadrezMove(origem, destino);
			
			if(capturePeca != null) {
				capturar.add(capturePeca);
			}
			if(partidaXadrez.getPromovido() !=null) {
				System.out.println("digite a peça promovida (/B/C/T/Q): ");
				String tipo =  sc.nextLine();
				partidaXadrez.substituirPecaPromovida(tipo);
			}
		}
		catch(ExeculsaoXadrez e){
			System.out.println(e.getMessage());
			sc.nextLine();
		}catch(InputMismatchException e) {
			System.out.println(e.getMessage());
			sc.nextLine();
			
			}	
		}
		UI.limparTela();
		UI.imprimirPartida(partidaXadrez, capturar);
	}
}
