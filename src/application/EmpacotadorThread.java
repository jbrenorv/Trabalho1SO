package application;

import java.util.LinkedHashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class EmpacotadorThread extends Thread {

	private final Pane node;
	private final int id;
	private final int te;
	private LinkedHashMap<String, Image> images = new LinkedHashMap<>();

	public EmpacotadorThread(int id, Pane node, int te) {
		super("Emp." + String.valueOf(id));

		node.setVisible(true);

		this.node = node;
		this.id = id;
		this.te = te * 1000;
	}

	@Override
	public void run() {
		empacotar();
	}

	public void empacotar() {
		long it = System.nanoTime();
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

				double progress = Math.min(ct / (te * 1.0), 1.0);
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

		}.start();

		// TODO: adicionar codigo para executar enquanto a animacao ocorre
	}

}
