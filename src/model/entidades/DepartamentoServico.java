package model.entidades;

import java.util.List;

import model.dao.DepartamentoDao;
import model.dao.FabricaDao;

public class DepartamentoServico {
	DepartamentoDao departamentoDao = FabricaDao.CreateDepartamentoDao();

	public List<Departamento> buscarTodos() {
		return departamentoDao.buscaTodos();
	}
	
	public void saveOrUpdate(Departamento dep) {
		if(dep.getId() == null) {
			departamentoDao.inserir(dep);
		}
		else {
			departamentoDao.atualizar(dep);
		}
	}
}