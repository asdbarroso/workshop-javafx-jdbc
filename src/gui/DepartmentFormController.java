package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbExcepetion;
import gui.listeners.DataChangeListener;
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
import model.entidades.Departamento;
import model.exceptions.ValidationException;
import model.services.DepartamentoServico;

public class DepartmentFormController implements Initializable {

	private Departamento departamento;
	private DepartamentoServico servico;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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

	public void subscribeChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {

		if (departamento == null) {
			throw new IllegalStateException("Entidade esta nulo");
		}
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		try {
			departamento = getFormData();
			servico.saveOrUpdate(departamento);
			notifyDataChangeListener();
			Utils.currentStage(event).close();
		} 
		catch(ValidationException e) {
			setErroMensagem(e.getErros());
		}
		catch (DbExcepetion e) {
			Alertas.showAlerta("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Departamento getFormData() {
		Departamento dep = new Departamento();

		ValidationException exception = new ValidationException("Erro na Validação");

		dep.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErros("nome", "O campo não pode ser vazio");
		}
		dep.setNome(txtNome.getText());
		if (exception.getErros().size() > 0) {
			throw exception;
		}
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
		if (departamento == null) {
			throw new IllegalStateException("Entidade é nulo");
		}
		txtId.setText(String.valueOf(departamento.getId()));
		txtNome.setText(departamento.getNome());
	}
	
	private void setErroMensagem(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		if(campos.contains("nome")) {
			lblErro.setText(erros.get("nome"));
		}
	}
}
