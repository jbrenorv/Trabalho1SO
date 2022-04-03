package application;

public class Deposito {
	public static int m; // capacidade máxima do depósito
	public static int qtdAtual = 0; // quantidade atual de pacotes no depósito

	public Deposito(int m, int qtdAtual) {
		this.m = m;
		this.qtdAtual = qtdAtual;
	}
}