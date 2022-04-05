package application;

import javafx.application.Platform;
import javafx.scene.control.Label;

import static application.Main.mutexHomeLayout;

public class Deposito {
	// capacidade máxima do depósito
	private int m;

	// quantidade atual de pacotes no depósito
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
		
		try {
			mutexHomeLayout.acquire();
			
			Platform.runLater(() -> {
				status.setText(qtdAtual + " / " + m);
				mutexHomeLayout.release();
			});

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
