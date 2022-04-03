package application;

import java.util.LinkedHashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TremThread extends Thread {

	private final Pane node;
	private final int tv;
	private final int n;
	private LinkedHashMap<String, Image> images = new LinkedHashMap<>();

	public TremThread(Pane node, int tv, int n) {
		super("Trem");

		node.setVisible(true);

		this.node = node;
		this.tv = tv * 1000; // transforma o tempo inserido de segundos em milissegundos
		this.n = n;
	}

	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < this.n; i++) {
				if (Semaforo.posCheias.availablePermits() == 0) {
					System.out.println("Trem dormiu!");
				}
				try {
					Semaforo.posCheias.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Semaforo.mutex.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				descarregarPacote();
				
				Semaforo.mutex.release();
				Semaforo.posVazias.release();
			}
			
			viajar();
		}
		
	}

	public void descarregarPacote() {
		if (Deposito.qtdAtual > 0) {
			Deposito.qtdAtual--;
			System.out.println("Trem recebeu o pacote do depósito.");
		}
		// TODO: adicionar codigo para executar enquanto a animacao ocorre
	}

	public void viajar() {
		long tempoIda = System.currentTimeMillis() + this.tv; // viagem de ida
		System.out.println("Trem iniciou a viagem.");
		while (System.currentTimeMillis() < tempoIda) {};
		long tempoVolta = System.currentTimeMillis() + this.tv; // viagem de ida
		System.out.println("Trem chegou ao destino.");
		while (System.currentTimeMillis() < tempoVolta) {};
		System.out.println("Trem terminou a viagem.");
		
		// codigo que já tinha antes na função do empacotador
		/* long it = System.nanoTime();
		String prefix = "/application/images/emp";

		Label lb = (Label) node.getChildren().get(0);
		ProgressBar pb = (ProgressBar) node.getChildren().get(1);
		ImageView iv = (ImageView) node.getChildren().get(2);

		Platform.runLater(() -> {
			lb.setText("Emp...");
		});

		new AnimationTimer() {
			int imageId = 0;
			long pt = 0;

			@Override
			public void handle(long ct) {
				// tempo decorrido desde o inicio em ms
				ct = (long) ((ct - it) / 1000000.0);

				double progress = Math.min(ct / (tv * 1.0), 1.0);
				pb.setProgress(progress);

				// muda a imagem a cada 200ms
				if ((ct - pt) > 200) {
					Image sprite;
					String url = prefix + imageId + ".png";
					if (!images.containsKey(url))
						images.put(url, new Image(url, 40, 64, false, false));
					sprite = images.get(url);
					iv.setImage(sprite);

					imageId = (imageId + 1) % 3;
					pt = ct;
				}

				if (progress >= 1) {
					this.stop();
				}
			}
		}.start();*/
	}
	// TODO: adicionar codigo para executar enquanto a animacao ocorre
}
