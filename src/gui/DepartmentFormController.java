package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbExcepetion;
import gui.util.Alertas;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.entidades.Departamento;
import modelo.entidades.DepartamentoServico;

public class DepartmentFormController implements Initializable {

	private Departamento departamento;
	private DepartamentoServico servico;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label lblErro;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		
		if(departamento == null) {
			throw new IllegalStateException("Entidade esta nulo");
		}
		if(servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		try {
		departamento = getFormData();
		servico.saveOrUpdate(departamento);
		Utils.currentStage(event).close();
		}
		catch(DbExcepetion e) {
			Alertas.showAlerta("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
		}
		
	}
	
	private Departamento getFormData() {
		
		Departamento dep = new Departamento();
		dep.setId(Utils.tryParseToInt(txtId.getText()));
		dep.setNome(txtNome.getText());
		return dep;
				
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
	}
	
	public void setDepartamento(Departamento dep) {
		departamento = dep;
	}
	
	public void setDepartamentoServico(DepartamentoServico servico) {
		this.servico = servico;
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 35);
	}
	
	public void updateFormData() {
		if(departamento == null) {
			throw new IllegalStateException("Entidade é nulo");
		}
		txtId.setText(String.valueOf(departamento.getId()));
		txtNome.setText(departamento.getNome());
		
	}

	
	
}
