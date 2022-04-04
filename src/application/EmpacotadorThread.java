package application;

import static application.Main.mutexLog;

import java.util.LinkedHashMap;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class EmpacotadorThread extends Thread {

	private final Pane node;
	private final TextArea taLog;
	private final int id;
	private final String nome;
	private final int te;
	private Label lb;
	private ProgressBar pb;
	private ImageView iv;
	private LinkedHashMap<String, Image> images = new LinkedHashMap<>();
	private String log;
	private Deposito deposito;
	
	private final String[] urls = {
		"/application/images/emp0.png",
		"/application/images/emp1.png",
		"/application/images/emp2.png",
		"/application/images/emp3.png",
		"/application/images/emp4.png",
		"/application/images/emp5.png",
		"/application/images/emp6.png",
		"/application/images/emp7.png",
		"/application/images/emp8.png",
		
		"/application/images/ind0.png",
		"/application/images/ind1.png",
		
		"/application/images/vol0.png",
		"/application/images/vol1.png",
		
		"/application/images/emp-dormindo.png",
	};
	
	public EmpacotadorThread(int id, int te, String nome, Pane node, TextArea ta, Deposito dep) {
		super("Emp." + String.valueOf(id));

		node.setVisible(true);

		this.node = node;
		this.id = id;
		this.nome = nome;
		this.taLog = ta;

		// transforma o tempo inserido de segundos em milissegundos
		this.te = te * 1000;
		
		lb = (Label) node.getChildren().get(0);
		pb = (ProgressBar) node.getChildren().get(1);
		iv = (ImageView) node.getChildren().get(2);
		
		deposito = dep;
		
		for (String url : urls) {
			images.put(url, new Image(url, 54, 114, false, false));
		}
	}

	@Override
	public void run() {
		

		while (true) {
			empacotar();
			
			// ir para o deposito
			caminhar(74, "/application/images/ind", "/application/images/ind1.png");
			
			// depositar caixa (REGIAO CRITICA)
			colocarCaixaNoDeposito();
			
			// voltar do deposito
			caminhar(0, "/application/images/vol", "/application/images/vol1.png");
			
//			if (Semaforo.posVazias.availablePermits() == 0) {
//				System.out.println(this.nome + "(id." + this.id + ") dormiu!");
//			}
//			try {
//				Semaforo.posVazias.acquire();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			try {
//				Semaforo.mutex.acquire();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			carregarPacote();
//			Semaforo.mutex.release();
//			Semaforo.posCheias.release();
		}
	}

	public void empacotar() {
		long it = System.nanoTime();
		String prefix = "/application/images/emp";

		Platform.runLater(() -> {
			lb.setText("Empacotando");
			lb.setVisible(true);
			pb.setVisible(true);
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
		lb.setVisible(false);
		updateLog("Empacotador " + (id + 1) + " terminou de empacotar", taLog);
	}
	
	private void caminhar(int y, String prefix, String url) {
		long it = System.nanoTime();
		long ct = System.nanoTime();
		long pt = 0;
		int imageId = 0;
		Image sprite = images.get(url);
		iv.setImage(sprite);
		
		Platform.runLater(() -> {
			lb.setText("Caminhando");
			lb.setVisible(true);
		});
		
		translateAnimation(0, y);
		
		while (true) {
			ct = (long) ((ct - it) / 1000000.0);

			if (ct >= 2000) {
				break;
			}
			
			if ((ct - pt) > 200) {
				url = prefix + imageId + ".png";
				sprite = images.get(url);
				iv.setImage(sprite);

				imageId = (imageId + 1) % 2;
				pt = ct;
			}

			ct = System.nanoTime();
		}
		
		lb.setVisible(false);
	}

	private void colocarCaixaNoDeposito() {
		if (Semaforo.posVazias.availablePermits() == 0) {
			iv.setImage(images.get("/application/images/emp-dormindo.png"));
			updateLog("Empacotador " + (id + 1) + " dormiu", taLog);
		}
		
		try {
			Semaforo.posVazias.acquire();
			Semaforo.mutex.acquire();
			
			deposito.depositar();
			updateLog("Empacotador " + (id + 1) + " depositou uma caixa", taLog);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			Semaforo.mutex.release();
			Semaforo.posCheias.release();
		}
//		try {
//			Semaforo.mutex.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Semaforo.mutex.release();
//		Semaforo.posCheias.release();
	}
	
	private void translateAnimation(int x, int y) {
		TranslateTransition tt = new TranslateTransition(Duration.seconds(2), this.node);
		tt.setToX(x);
		tt.setToY(y);

		tt.play();
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
				ta.appendText("");
			});

			mutexLog.release();
		}
	}
}
