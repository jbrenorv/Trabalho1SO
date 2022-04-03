package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetupDialogLayoutController implements Initializable {
	@FXML
	private Button btnIniciar;

	@FXML
	private Label lblErro;

	@FXML
	private TextField tfCapDeposito;

	@FXML
	private TextField tfCapTrem;

	@FXML
	private TextField tfTempoViagem;

	private int m;
	private int n;
	private int tv;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tfCapDeposito.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					tfCapDeposito.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		tfCapTrem.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					tfCapTrem.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		tfTempoViagem.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					tfTempoViagem.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	@FXML
	void iniciar(ActionEvent event) {
		if (tfCapDeposito.getText().isEmpty() || tfCapTrem.getText().isEmpty() || tfTempoViagem.getText().isEmpty()) {
			lblErro.setText("Preencha todos os campos!");
			return;
		}

		m = Integer.parseInt(tfCapDeposito.getText());
		n = Integer.parseInt(tfCapTrem.getText());
		tv = Integer.parseInt(tfTempoViagem.getText());

		if (n > m) {
			lblErro.setText("A capapacidade do trem deve ser menor ou igual a do depósito!");
			return;
		}

		close(event);
	}

	private void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	public int getN() {
		return n;
	}

	public int getM() {
		return m;
	}

	public int getTv() {
		return tv;
	}

}
