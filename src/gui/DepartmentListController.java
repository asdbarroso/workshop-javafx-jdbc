package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entidades.Departamento;
import modelo.entidades.DepartamentoServico;

public class DepartmentListController implements Initializable{

	private DepartamentoServico servico;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> tableColumnNome;
	
	@FXML
	private Button btnNovo;
	
	private ObservableList<Departamento> obsLista;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage stage = gui.util.Utils.currentStage(event);
		createDialogForm("/gui/FormularioDepartamento.fxml", stage);
	}
	
	public void setServico(DepartamentoServico servico) {
		this.servico = servico;
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		iniciarNodes();
		
	}

	private void iniciarNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		
		if(servico == null) {
			throw new IllegalStateException("Serviço esta nulo");
		}
		List<Departamento> lista = servico.buscarTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsLista);
	}
	
	private void createDialogForm(String nomeAbsoluto, Stage parentStage) {
		
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
		Pane pane = loader.load();
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Cadastro de Departamento");
		dialogStage.setScene(new Scene(pane));
		dialogStage.setResizable(false);
		dialogStage.initOwner(parentStage);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.showAndWait();
		
		}
		catch (IOException e)  {
			Alertas.showAlerta("Erro IOExcepition", "Erro ao carregar Janela", e.getMessage(), AlertType.ERROR);
		}
		
		
	}

}
