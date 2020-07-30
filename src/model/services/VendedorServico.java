package model.services;

import java.util.List;

import model.dao.VendedorDao;
import model.dao.FabricaDao;
import model.entidades.Vendedor;

public class VendedorServico {
	VendedorDao departamentoDao = FabricaDao.CreateVendedorDao();

	public List<Vendedor> buscarTodos() {
		return departamentoDao.buscarTodos();
	}

	public void saveOrUpdate(Vendedor dep) {
		if (dep.getId() == null) {
			departamentoDao.inserir(dep);
		} else {
			departamentoDao.atualizar(dep);
		}
	}
	
	public void removeVendedor(Vendedor obj) {
		departamentoDao.apagarPorId(obj.getId());
	}

}