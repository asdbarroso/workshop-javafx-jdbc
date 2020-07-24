package gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alertas {

	public static void showAlerta(String titulo, String cabecalho, String msg, AlertType type) {
		Alert alerta = new Alert(type);
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.setContentText(msg);
		alerta.show();
	}
	
}
