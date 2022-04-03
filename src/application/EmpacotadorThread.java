package application;

import java.util.LinkedHashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import static application.Main.*;

public class EmpacotadorThread extends Thread {

	private final Pane node;
	private final TextArea taLog;
	private final int id;
	private final String nome;
	private final int te;
	private LinkedHashMap<String, Image> images = new LinkedHashMap<>();
	private String log;

	public EmpacotadorThread(int id, Pane node, String nome, int te) {
		super("Emp." + String.valueOf(id));

		node.setVisible(true);
		
//		node.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

		this.node = node;
		this.id = id;
		this.nome = nome;
		this.te = te * 1000; // transforma o tempo inserido de segundos em milissegundos
		this.taLog = ta;
	}

	@Override
	public void run() {
		while (true) {
			empacotar();
			
			if (Semaforo.posVazias.availablePermits() == 0) {
				System.out.println(this.nome + "(id." + this.id + ") dormiu!");
			}
			try {
				Semaforo.posVazias.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Semaforo.mutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			carregarPacote();
			Semaforo.mutex.release();
			Semaforo.posCheias.release();
		}
	}

	public void empacotar() {
		long it = System.nanoTime();
		String prefix = "/application/images/emp";

		Label lb = (Label) node.getChildren().get(0);
		ProgressBar pb = (ProgressBar) node.getChildren().get(1);
		ImageView iv = (ImageView) node.getChildren().get(2);

		Platform.runLater(() -> {
			lb.setText(this.nome);
		});

		new AnimationTimer() {
			int imageId = 0;
			long pt = 0;

			@Override
			public void handle(long ct) {
				// tempo decorrido desde o inicio em ms
				ct = (long) ((ct - it) / 1000000.0);

				double progress = Math.min(ct / (te * 1.0), 1.0);
				pb.setProgress(progress);

				// muda a imagem a cada 200ms
				if ((ct - pt) > 200) {
					Image sprite;
					String url = prefix + imageId + ".png";
					if (!images.containsKey(url))
						images.put(url, new Image(url, 54, 110, false, false));
					sprite = images.get(url);
					iv.setImage(sprite);

					imageId = (imageId + 1) % 9;
					pt = ct;
				}

				if (progress >= 1) {
					this.stop();
				}
			}

		}.start();
    
    delay(it, te);
		pb.setVisible(false);
		updateLog("Empacotador " + (id+1) + " terminou de empacotar", taLog);
    

//		System.out.println(this.nome + "(id." + this.id + ") come�ou a empacotar.");
//		long tempoPacote = System.currentTimeMillis() + this.te;
//		while (System.currentTimeMillis() < tempoPacote) {
//			for(int i=0;i<100;i++) {};
//		};
//		System.out.println(this.nome + "(id." + this.id + ") terminou de empacotar.");
	}
	
	public void carregarPacote() {
		
		if (Deposito.qtdAtual >= 0) {
			Deposito.qtdAtual++;
			System.out.println(this.nome + "(id." + this.id + ") carregou o dep�sito. Total: " + Deposito.qtdAtual + " pacotes carregados.");
		}
		
	}
	
	private void colocarCaixaNoDeposito() {

	}
	
	private void voltar() {
		
	}
	
	private void delay(long initialNanoTime, int durationInMillis) {
		long ct = System.nanoTime();
		while (true) {
			ct = (long) ((ct - initialNanoTime) / 1000000.0);
			
			if (ct >= durationInMillis) {
				break;
			}
			
			ct = System.nanoTime();
		}
	}
	
	private void updateLog(String msg, TextArea ta) {
		try {
			mutexLog.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log = ta.getText();
			if (log == null) {
				log = msg;
			} else {
				log = log + "\n" + msg;
			}
			
			Platform.runLater(() -> {
				ta.setText(log);
			});

			mutexLog.release();
		}
	}
}
