package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbExcepetion;
import model.dao.VendedorDao;
import modelo.entidades.Departamento;
import modelo.entidades.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {

	private Connection conn;

	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Vendedor obj) {

		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO seller ( " + "Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());

			int linhasAlteradas = st.executeUpdate();

			if (linhasAlteradas > 0) {
				ResultSet result = st.getGeneratedKeys();
				if (result.next()) {
					int id = result.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(result);
			} else {
				throw new DbExcepetion("Algo ocorreu de forma inesperada!! Nehuma linha alterada!!");
			}
		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void atualizar(Vendedor obj) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?");
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
			
		} 
		catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void apagarPorId(Integer id) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
			
		} 
		catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Vendedor buscarPorId(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				Vendedor vendedor = instaciarVendedor(rs, dep);
				return vendedor;
			}
			return null;
		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> buscaPorDepartamento(Departamento dep) {

		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? ORDER BY Name");

			st.setInt(1, dep.getId());
			rs = st.executeQuery();
			List<Vendedor> listaVendedores = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento depart = map.get(rs.getInt("DepartmentID"));
				System.out.println("Depart: .. " + depart);
				if (depart == null) {
					depart = instanciarDepartamento(rs);
					System.out.println("Depart2:.. " + depart);
					map.put(rs.getInt("DepartmentID"), depart);
				}
				listaVendedores.add(instaciarVendedor(rs, depart));
			}
			return listaVendedores;
		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Vendedor> buscarTodos() {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rs = st.executeQuery();
			List<Vendedor> listaVendedores = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {
				Departamento depart = map.get(rs.getInt("DepartmentID"));
				if (depart == null) {
					depart = instanciarDepartamento(rs);
					map.put(rs.getInt("DepartmentID"), depart);
				}
				listaVendedores.add(instaciarVendedor(rs, depart));
			}
			return listaVendedores;
		} catch (SQLException e) {
			throw new DbExcepetion(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Vendedor instaciarVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor vendedor = new Vendedor();
		vendedor.setId(rs.getInt("Id"));
		vendedor.setNome(rs.getString("Name"));
		vendedor.setEmail(rs.getString("Email"));
		vendedor.setDataNasc(rs.getDate("BirthDate"));
		vendedor.setSalarioBase(rs.getDouble("BaseSalary"));
		vendedor.setDepartamento(dep);
		return vendedor;
	}

	private Departamento instanciarDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setNome(rs.getString("DepName"));
		return dep;
	}
}
