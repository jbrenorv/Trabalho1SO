package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static List<Integer> ids;
	public static Semaphore mutexLog;

	@Override
	public void start(Stage stage) throws IOException {
		
		mutexLog = new Semaphore(1, true);
		ids = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

		Parent root = FXMLLoader.load(getClass().getResource("/application/layouts/HomeLayout.fxml"));
		Scene scene = new Scene(root, 1280, 720);

		stage.setResizable(false);
		stage.setTitle("Trabalho 1 SO");
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(e -> handleExit());
	}

	public static int getId() {
		Random random = new Random();
		return ids.remove(random.nextInt(ids.size()));
	}

	private void handleExit() {
		Platform.exit();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
