package modelo.entidades;

import java.util.ArrayList;
import java.util.List;

public class DepartamentoServico {

		public List<Departamento> buscarTodos(){

			List<Departamento> lista = new ArrayList<>();
			
			lista.add(new Departamento(1, "Livros"));
			lista.add(new Departamento(2, "Computadores"));
			lista.add(new Departamento(3, "Eletronicos"));
			
			return lista;
		}
		
	}