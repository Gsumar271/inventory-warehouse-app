/**
 * 
 */
package com.skillstorm.warehouse.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.warehouse.conf.WarehouseDbCreds;
import com.skillstorm.warehouse.models.Product;
import com.skillstorm.warehouse.models.Supplier;

/**
 * @author eugenesumaryev
 *
 */
public class MySQLSupplierDAOImp implements InventoryDAO<Supplier> {

	@Override
	public List<Supplier> findAll() {
		String sql = "SELECT Supplier_id, Supplier_name, Supplier_address, Supplier_phonenum, Supplier_email FROM Supplier";

		// Connection will auto close in the event of a failure. Due to Autocloseable
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			// Create a Statement using the Connection object
			Statement stmt = conn.createStatement();

			// Executing the query returns a ResultSet which contains all of the values
			// returned
			ResultSet rs = stmt.executeQuery(sql);
			LinkedList<Supplier> suppliers = new LinkedList<>();

			// next() returns a boolean on whether the iterator is done yet
			// You need to advance the cursor with it so that you can parse all of the
			// results
			while (rs.next()) {
				// Looping over individual rows of the result set
				Supplier supplier = new Supplier(rs.getInt("Supplier_id"), rs.getString("Supplier_name"),
						rs.getString("Supplier_address"), rs.getString("Supplier_phonenum"),
						rs.getString("Supplier_email"));

				suppliers.add(supplier);
			}

			return suppliers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Supplier findById(int id) {
		// find supplier by Id
		String sql = "SELECT Supplier_id, Supplier_name, Supplier_address, Supplier_phonenum, Supplier_email FROM Supplier WHERE Supplier_id = "
				+ id;
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return new Supplier(rs.getInt("Supplier_id"), rs.getString("Supplier_name"),
						rs.getString("Supplier_address"), rs.getString("Supplier_phonenum"),
						rs.getString("Supplier_email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Supplier findByName(String name) {
		// Use parameterized queries
		String sql = "SELECT Supplier_id, Supplier_name, Supplier_address, Supplier_phonenum, Supplier_email FROM Supplier WHERE Supplier_name = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, name); // This auto escapes any malicious characters
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // Make sure there was at least one item there
				return new Supplier(rs.getInt("Supplier_id"), rs.getString("Supplier_name"),
						rs.getString("Supplier_address"), rs.getString("Supplier_phonenum"),
						rs.getString("Supplier_email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Supplier save(Supplier supplier) {
		// If this was auto-increment, then the artistid is not needed
		String sql = "INSERT INTO Supplier (Supplier_name, Supplier_address, Supplier_phonenum, Supplier_email) VALUES (?,?,?,?)";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Start a transaction
			conn.setAutoCommit(false); // Prevents each query from immediately altering the database

			// Obtain auto incremented values like so
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, supplier.getName());
			ps.setString(2, supplier.getAddress());
			ps.setString(3, supplier.getPhoneNumber());
			ps.setString(4, supplier.getEmail());

			int rowsAffected = ps.executeUpdate(); // If 0 is returned, my data didn't save
			if (rowsAffected != 0) {
				// If I want my keys do this code
				ResultSet keys = ps.getGeneratedKeys();
				// List a of all generated keys
				if (keys.next()) {
					int key = keys.getInt(1); // Give me the auto generated key
					supplier.setId(key);
					
				}
				conn.commit(); // Executes ALL queries in a given transaction. Green button
				return supplier;
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(Supplier supplier) {
		String sql = "UPDATE Supplier SET Supplier_name = ?, Supplier_address = ?, Supplier_phonenum = ?, Supplier_email = ? WHERE Supplier_id = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, supplier.getName()); // This auto escapes any malicious characters
			ps.setString(2, supplier.getAddress());
			ps.setString(3, supplier.getPhoneNumber());
			ps.setString(4, supplier.getEmail());
			ps.setInt(5, supplier.getId());
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(Supplier supplier) {
		String sql = "DELETE FROM Supplier WHERE Supplier_name = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, supplier.getName()); // This auto escapes any malicious characters
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM Supplier WHERE Supplier_id = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setInt(1, id); // This auto escapes any malicious characters
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteMany(int[] ids) {
		String sql = "DELETE FROM Supplier WHERE Supplier_id = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			
			PreparedStatement ps;

			// Run a loop while deleting rows 
			for (int i = 0; i < ids.length; i++) {
				ps = conn.prepareStatement(sql);
				// Java is going to check our statement ahead of time to make sure it's okay
				ps.setInt(1, ids[i]); // This auto escapes any malicious characters
				int row = ps.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
