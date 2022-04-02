package application;

import java.util.LinkedHashMap;
import javafx.scene.layout.Pane;


public class Deposito {
	
	private final Pane node;
	public static int m; // capacidade m�xima do dep�sito
	public static int qtdAtual = 0; // quantidade atual de pacotes no dep�sito
	
	public Deposito(Pane node, int m, int qtdAtual) {
		super();

		node.setVisible(true);

		this.node = node;
		this.m = m;
		this.qtdAtual = qtdAtual;
	}
}