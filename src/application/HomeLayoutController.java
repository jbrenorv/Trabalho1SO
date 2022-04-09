package application;

import static application.Main.getId;
import static application.Main.ids;
import static application.Main.setDeposito;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeLayoutController implements Initializable {

	@FXML
	private Button btCriarEmpacotador;

	@FXML
    private Label lbDeposito;
	
	@FXML
    private Label lbTrem;

	@FXML
	private Pane pTrem;

	@FXML
	private TextArea taLog;

	@FXML
	private TextField tfNomeEmpacotador;

	@FXML
	private TextField tfTempoDeEmpacotamento;

	@FXML
	private VBox vbEmp0;

	@FXML
	private VBox vbEmp1;

	@FXML
	private VBox vbEmp2;

	@FXML
	private VBox vbEmp3;

	@FXML
	private VBox vbEmp4;

	@FXML
	private VBox vbEmp5;

	@FXML
	private VBox vbEmp6;

	@FXML
	private VBox vbEmp7;

	@FXML
	private VBox vbEmp8;

	@FXML
	private VBox vbEmp9;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/layouts/SetupDialogLayout.fxml"));
		SetupDialogLayoutController setupController = null;

		try {
			Parent parent = fxmlLoader.load();
			setupController = fxmlLoader.<SetupDialogLayoutController>getController();
			Scene scene = new Scene(parent, 480, 270);
			Stage stage = new Stage();

			stage.initStyle(StageStyle.UTILITY);
			stage.setOnCloseRequest(e -> {
				Platform.exit();
				System.exit(0);
			});
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			int n = setupController.getN();
			int tv = setupController.getTv();
			int m = setupController.getM();

			setDeposito(new Deposito(m, 0, lbDeposito));
			Semaforo.setPosVaziasSemaphore(m);
			TremThread trem = new TremThread(pTrem, tv, n, taLog, lbTrem);
			trem.start();
		}

		taLog.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		    	//this will scroll to the bottom
		    	taLog.setScrollTop(Double.MAX_VALUE);
		        //use Double.MIN_VALUE to scroll to the top
		    }
		});

		tfTempoDeEmpacotamento.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					tfTempoDeEmpacotamento.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

	}

	@FXML
	void criarEmpacotador() {
		int id = getId();
		String nome = tfNomeEmpacotador.getText();
		String teStr = tfTempoDeEmpacotamento.getText();
		
		if (teStr.isEmpty()) {
			return;
		}
		
		int te = Integer.parseInt(teStr);

		switch (id) {
		case 0:
			EmpacotadorThread emp0 = new EmpacotadorThread(id, te, nome, vbEmp0, taLog);
			emp0.start();
			break;
		case 1:
			EmpacotadorThread emp1 = new EmpacotadorThread(id, te, nome, vbEmp1, taLog);
			emp1.start();
			break;
		case 2:
			EmpacotadorThread emp2 = new EmpacotadorThread(id, te, nome, vbEmp2, taLog);
			emp2.start();
			break;
		case 3:
			EmpacotadorThread emp3 = new EmpacotadorThread(id, te, nome, vbEmp3, taLog);
			emp3.start();
			break;
		case 4:
			EmpacotadorThread emp4 = new EmpacotadorThread(id, te, nome, vbEmp4, taLog);
			emp4.start();
			break;
		case 5:
			EmpacotadorThread emp5 = new EmpacotadorThread(id, te, nome, vbEmp5, taLog);
			emp5.start();
			break;
		case 6:
			EmpacotadorThread emp6 = new EmpacotadorThread(id, te, nome, vbEmp6, taLog);
			emp6.start();
			break;
		case 7:
			EmpacotadorThread emp7 = new EmpacotadorThread(id, te, nome, vbEmp7, taLog);
			emp7.start();
			break;
		case 8:
			EmpacotadorThread emp8 = new EmpacotadorThread(id, te, nome, vbEmp8, taLog);
			emp8.start();
			break;
		default:
			EmpacotadorThread emp9 = new EmpacotadorThread(id, te, nome, vbEmp9, taLog);
			emp9.start();
			break;
		}

		if (ids.isEmpty()) {
			btCriarEmpacotador.setDisable(true);
		}
	}
}
