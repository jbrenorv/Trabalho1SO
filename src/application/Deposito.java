package application;

public class Deposito {
	public static int m; // capacidade m�xima do dep�sito
	public static int qtdAtual = 0; // quantidade atual de pacotes no dep�sito

	public Deposito(int m, int qtdAtual) {
		this.m = m;
		this.qtdAtual = qtdAtual;
	}
}