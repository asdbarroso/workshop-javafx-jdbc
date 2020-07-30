package model.dao;

import java.util.List;

import model.entidades.Departamento;

public interface DepartamentoDao {

	void inserir(Departamento obj);
	void atualizar(Departamento obj);
	void apagarPorId(Integer id);
	Departamento buscarPorId(Integer id);
	List<Departamento> buscaTodos();
	
}
