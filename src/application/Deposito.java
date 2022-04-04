package application;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Deposito {
	// capacidade m�xima do dep�sito
	private int m;

	// quantidade atual de pacotes no dep�sito
	private int qtdAtual = 0;
	
	private Label status;

	public Deposito(int qtdMax, int qtdInicial, Label lb) {
		m = qtdMax;
		qtdAtual = qtdInicial;
		status = lb;
	}
	
	public void depositar() {
		qtdAtual++;
		setStatus();
	}
	
	public void retirar() {
		qtdAtual--;
		setStatus();
	}
	
	private void setStatus() {
		Platform.runLater(() -> {
			status.setText(qtdAtual + " / " + m);
		});
	}
}
