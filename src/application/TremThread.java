package application;

import static application.Main.mutexHomeLayout;
import static application.Main.deposito;

import java.io.File;
import java.util.LinkedHashMap;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class TremThread extends Thread {

	private Pane node;
	private int tv;
	private int n;
	private ImageView iv;
	private LinkedHashMap<String, Image> images = new LinkedHashMap<>();
	private String log;
	private TextArea taLog;
	private Label lbTrem;
	private int qtdAtual = 0;

	private final String[] urls = {
		"/application/images/trem-parado.png",
		"/application/images/trem-indo0.png",
		"/application/images/trem-indo1.png",
		"/application/images/trem-indo2.png",
		"/application/images/trem-voltando0.png",
		"/application/images/trem-voltando1.png",
		"/application/images/trem-voltando2.png",
	};

	public TremThread(Pane node, int tv, int n, TextArea ta, Label lb) {
		super("Trem");

		this.node = node;
		this.tv = tv * 1000;
		this.n = n;
		this.taLog = ta;
		this.lbTrem = lb;

		iv = (ImageView) node.getChildren().get(0);

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
			
			lbTrem.setVisible(true);
			updateLog("Trem sendo carregado", taLog);
			for (int i = 0; i < n; i++) {
				if (Semaforo.posCheias.availablePermits() == 0) {
					updateLog("Trem dormiu!", taLog);
				}

				try {
					Semaforo.posCheias.acquire();
					Semaforo.mutex.acquire();

					deposito.retirar();
					qtdAtual++;
					setStatus();

					Semaforo.mutex.release();
					Semaforo.posVazias.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			mediaPlayer.play();

			updateLog("Trem indo para a estacao B", taLog);
			viajar(870, "/application/images/trem-indo", "/application/images/trem-indo0.png");
			
			lbTrem.setVisible(false);
			qtdAtual = 0;
			updateLog("Trem voltando para a estacao A", taLog);
			viajar(0, "/application/images/trem-voltando", "/application/images/trem-voltando0.png");

			mediaPlayer.stop();
			setTrainImage("/application/images/trem-parado.png");
		}

	}

	private void viajar(int x, String prefix, String url) {
		long it = System.nanoTime();
		long ct = System.nanoTime();
		long pt = 0;
		int imageId = 0;
		setTrainImage(url);

		int millis = tv / 2;
		translateAnimation(x, 0, Duration.millis(millis));

		while (true) {
			ct = (long) ((ct - it) / 1000000.0);

			if (ct >= millis) {
				break;
			}

			if ((ct - pt) > 150) {
				url = prefix + imageId + ".png";
				setTrainImage(url);

				imageId = (imageId + 1) % 3;
				pt = ct;
			}

			ct = System.nanoTime();
		}

	}

	private void translateAnimation(int x, int y, Duration d) {
		TranslateTransition tt = new TranslateTransition(d, this.node);
		tt.setToX(x);
		tt.setToY(y);

		tt.play();
	}

	private void updateLog(String msg, TextArea ta) {

		try {
			mutexHomeLayout.acquire();

			Platform.runLater(() -> {
				log = ta.getText();
				if (log == null) {
					log = msg;
				} else {
					log = log + "\n" + msg;
				}

				ta.setText(log);
				ta.appendText("");

				mutexHomeLayout.release();
			});

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setTrainImage(String url) {

			iv.setImage(images.get(url));
	}
	
	private void setStatus() {
			
		try {
			mutexHomeLayout.acquire();
			
			Platform.runLater(() -> {
				lbTrem.setText(qtdAtual + " / " + n);
				mutexHomeLayout.release();
			});

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
