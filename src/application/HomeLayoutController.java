package application;

import static application.Main.getId;
import static application.Main.ids;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class HomeLayoutController implements Initializable {

	@FXML
	private Button btCriarEmpacotador;

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
		// TODO Auto-generated method stub

	}

	@FXML
	void criarEmpacotador() {
		int id = getId();

		//////// tratar errors
		int te = Integer.parseInt(tfTempoDeEmpacotamento.getText());
		//////////////////////

		switch (id) {
		case 0:
			EmpacotadorThread emp0 = new EmpacotadorThread(id, vbEmp0, te);
			emp0.start();
			break;
		case 1:
			EmpacotadorThread emp1 = new EmpacotadorThread(id, vbEmp1, te);
			emp1.start();
			break;
		case 2:
			EmpacotadorThread emp2 = new EmpacotadorThread(id, vbEmp2, te);
			emp2.start();
			break;
		case 3:
			EmpacotadorThread emp3 = new EmpacotadorThread(id, vbEmp3, te);
			emp3.start();
			break;
		case 4:
			EmpacotadorThread emp4 = new EmpacotadorThread(id, vbEmp4, te);
			emp4.start();
			break;
		case 5:
			EmpacotadorThread emp5 = new EmpacotadorThread(id, vbEmp5, te);
			emp5.start();
			break;
		case 6:
			EmpacotadorThread emp6 = new EmpacotadorThread(id, vbEmp6, te);
			emp6.start();
			break;
		case 7:
			EmpacotadorThread emp7 = new EmpacotadorThread(id, vbEmp7, te);
			emp7.start();
			break;
		case 8:
			EmpacotadorThread emp8 = new EmpacotadorThread(id, vbEmp8, te);
			emp8.start();
			break;
		default:
			EmpacotadorThread emp9 = new EmpacotadorThread(id, vbEmp9, te);
			emp9.start();
			break;
		}

		if (ids.isEmpty()) {
			btCriarEmpacotador.setDisable(true);
		}
	}

}
