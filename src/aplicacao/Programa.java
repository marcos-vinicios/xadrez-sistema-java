package aplicacao;

import java.util.InputMismatchException;
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
		
		while(true) {
			try {
			UI.limparTela();
			UI.printTabuleiro(partidaXadrez.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);
			
			PecaXadrez capturePeca = partidaXadrez.execultarXadrezMove(origem, origem);
		}
		catch(ExeculsaoXadrez e){
			System.out.println(e.getMessage());
			sc.nextLine();
		}catch(InputMismatchException e) {
			System.out.println(e.getMessage());
			sc.nextLine();
			
			}	
		}
	}
}
