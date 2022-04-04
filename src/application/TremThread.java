package application;

import java.io.File;
import java.util.LinkedHashMap;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class TremThread extends Thread {

	private final Pane node;
	private final int tv;
	private final int n;
	private int qtdAtual = 0;
	private ImageView iv;
	private LinkedHashMap<String, Image> images = new LinkedHashMap<>();
	private Deposito deposito;
	private Label status;
	
	private final String[] urls = {
		"/application/images/trem-parado.png",
		"/application/images/trem-indo0.png",
		"/application/images/trem-indo1.png",
		"/application/images/trem-indo2.png",
		"/application/images/trem-voltando0.png",
		"/application/images/trem-voltando1.png",
		"/application/images/trem-voltando2.png",
	};

	public TremThread(Pane node, int tv, int n, Deposito dep, Label lb) {
		super("Trem");

		this.node = node;
		this.tv = tv * 1000; // transforma o tempo inserido de segundos em milissegundos
		this.n = n;
		
		iv = (ImageView) node.getChildren().get(0);
		deposito = dep;
		status = lb;
		
		for (String url : urls) {
			images.put(url, new Image(url, 400, 150, false, false));
		}
	}

	@Override
	public void run() {
		String path = "src/application/trem-viajando.wav";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.2);
        
		while (true) {
			
			status.setVisible(true);
			
			for (int i = 0; i < n; i++) {
				if (Semaforo.posCheias.availablePermits() == 0) {
					System.out.println("Trem dormiu!");
				}
				
				try {
					Semaforo.posCheias.acquire();
					Semaforo.mutex.acquire();
					
					deposito.retirar();
					qtdAtual++;
					setStatus();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Semaforo.mutex.release();
					Semaforo.posVazias.release();					
				}
			}
			
			status.setVisible(false);
			mediaPlayer.play();
			
			// ir para a estacao B
			viajar(870, "/application/images/trem-indo", "/application/images/trem-indo0.png");
			
			// voltar para a estacao A
			viajar(0, "/application/images/trem-voltando", "/application/images/trem-voltando0.png");
			
			mediaPlayer.stop();
			iv.setImage(images.get("/application/images/trem-parado.png"));
		}

	}
	
	private void viajar(int x, String prefix, String url) {
//		String prefix = "/application/images/trem-viajando";
//		String url = "/application/images/trem-viajando0.png";
		long it = System.nanoTime();
		long ct = System.nanoTime();
		long pt = 0;
		int imageId = 0;
		Image sprite = images.get(url);
		iv.setImage(sprite);
		
//		Platform.runLater(() -> {
//			lb.setText("Caminhando");
//			lb.setVisible(true);
//		});
		
		int millis = tv / 2;
		translateAnimation(x, 0, Duration.millis(millis));
		
		while (true) {
			ct = (long) ((ct - it) / 1000000.0);

			if (ct >= millis) {
				break;
			}
			
			if ((ct - pt) > 200) {
				url = prefix + imageId + ".png";
				sprite = images.get(url);
				iv.setImage(sprite);

				imageId = (imageId + 1) % 3;
				pt = ct;
			}

			ct = System.nanoTime();
		}
		
//		Platform.runLater(() -> {
//			node.setNodeOrientation(noOnFinish);
//		});

//		lb.setVisible(false);
	}
	
	private void translateAnimation(int x, int y, Duration d) {
		TranslateTransition tt = new TranslateTransition(d, this.node);
		tt.setToX(x);
		tt.setToY(y);

		tt.play();
	}
	
	private void setStatus() {
		Platform.runLater(() -> {
			status.setText(qtdAtual + " / " + n);
		});
	}

//	public void descarregarPacote() {
//		if (Deposito.qtdAtual > 0) {
//			Deposito.qtdAtual--;
//			System.out.println("Trem recebeu o pacote do depósito.");
//		}
//		// TODO: adicionar codigo para executar enquanto a animacao ocorre
//	}

//	public void viajar() {
//		long tempoIda = System.currentTimeMillis() + this.tv; // viagem de ida
//		System.out.println("Trem iniciou a viagem.");
//		while (System.currentTimeMillis() < tempoIda) {
//		}
//		long tempoVolta = System.currentTimeMillis() + this.tv; // viagem de ida
//		System.out.println("Trem chegou ao destino.");
//		while (System.currentTimeMillis() < tempoVolta) {
//		}
//		System.out.println("Trem terminou a viagem.");
//
//		// codigo que já tinha antes na função do empacotador
//		/*
//		 * long it = System.nanoTime(); String prefix = "/application/images/emp";
//		 *
//		 * Label lb = (Label) node.getChildren().get(0); ProgressBar pb = (ProgressBar)
//		 * node.getChildren().get(1); ImageView iv = (ImageView)
//		 * node.getChildren().get(2);
//		 *
//		 * Platform.runLater(() -> { lb.setText("Emp..."); });
//		 *
//		 * new AnimationTimer() { int imageId = 0; long pt = 0;
//		 *
//		 * @Override public void handle(long ct) { // tempo decorrido desde o inicio em
//		 * ms ct = (long) ((ct - it) / 1000000.0);
//		 *
//		 * double progress = Math.min(ct / (tv * 1.0), 1.0); pb.setProgress(progress);
//		 *
//		 * // muda a imagem a cada 200ms if ((ct - pt) > 200) { Image sprite; String url
//		 * = prefix + imageId + ".png"; if (!images.containsKey(url)) images.put(url,
//		 * new Image(url, 40, 64, false, false)); sprite = images.get(url);
//		 * iv.setImage(sprite);
//		 *
//		 * imageId = (imageId + 1) % 3; pt = ct; }
//		 *
//		 * if (progress >= 1) { this.stop(); } } }.start();
//		 */
//	}
//	// TODO: adicionar codigo para executar enquanto a animacao ocorre
}
