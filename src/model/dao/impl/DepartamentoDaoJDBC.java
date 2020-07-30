package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbExcepetion;
import model.dao.DepartamentoDao;
import model.entidades.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {

	private Connection conn = null;

	Departamento dep = new Departamento();

	public DepartamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Departamento obj) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", st.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());

			int linhasAlteradas = st.executeUpdate();

			if (linhasAlteradas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbExcepetion("Algo ocorreu de forma inesperada!! Nehuma linha inserida!!");
			}

		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void atualizar(Departamento obj) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");

			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void apagarPorId(Integer id) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Departamento buscarPorId(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT Id, Name FROM department WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				dep.setId(rs.getInt("Id"));
				dep.setNome(rs.getString("Name"));
			}
			return dep;
		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public List<Departamento> buscaTodos() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM department ORDER BY Name");

			rs = st.executeQuery();

			List<Departamento> lista = new ArrayList<>();

			while (rs.next()) {
				Departamento depto = new Departamento();
				depto.setId(rs.getInt("Id"));
				depto.setNome(rs.getString("Name"));
				lista.add(depto);
			}
			return lista;

		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}
}
