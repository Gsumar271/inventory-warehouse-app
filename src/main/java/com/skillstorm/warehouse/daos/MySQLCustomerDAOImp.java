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
import com.skillstorm.warehouse.models.Customer;
import com.skillstorm.warehouse.models.Supplier;

/**
 * @author eugenesumaryev
 *
 */
public class MySQLCustomerDAOImp implements InventoryDAO<Customer> {

	@Override
	public List<Customer> findAll() {
		String sql = "SELECT Customer_id, Customer_firstname, Customer_lastname, Customer_address, Customer_phonenumber, Customer_email FROM Customer";

		// Connection will auto close in the event of a failure. Due to Autocloseable
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			// Create a Statement using the Connection object
			Statement stmt = conn.createStatement();

			// Executing the query returns a ResultSet which contains all of the values
			// returned
			ResultSet rs = stmt.executeQuery(sql);
			LinkedList<Customer> customers = new LinkedList<>();

			// next() returns a boolean on whether the iterator is done yet
			// You need to advance the cursor with it so that you can parse all of the
			// results
			while (rs.next()) {
				// Looping over individual rows of the result set
				Customer customer = new Customer(rs.getInt("Customer_id"), rs.getString("Customer_firstname"),
						rs.getString("Customer_lastname"), rs.getString("Customer_address"),
						rs.getString("Customer_phonenumber"), rs.getString("Customer_email"));

				customers.add(customer);
			}

			return customers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Customer findById(int id) {
		// find supplier by Id
		String sql = "SELECT Customer_id, Customer_firstname, Customer_lastname, Customer_address, Customer_phonenumber, Customer_email FROM Customer WHERE Customer_id = "
				+ id;
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return new Customer(rs.getInt("Customer_id"), rs.getString("Customer_firstname"),
						rs.getString("Customer_lastname"), rs.getString("Customer_address"),
						rs.getString("Customer_phonenumber"), rs.getString("Customer_email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Customer findByName(String name) {
		// Use parameterized queries
		String sql = "SELECT Customer_id, Customer_firstname, Customer_lastname, Customer_address, Customer_phonenumber, Customer_email FROM Customer WHERE Customer_name = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, name); // This auto escapes any malicious characters
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // Make sure there was at least one item there
				return new Customer(rs.getInt("Customer_id"), rs.getString("Customer_firstname"),
						rs.getString("Customer_lastname"), rs.getString("Customer_address"),
						rs.getString("Customer_phonenumber"), rs.getString("Customer_email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Customer save(Customer customer) {
		// 
		String sql = "INSERT INTO Customer (Customer_firstname, Customer_lastname, Customer_address, Customer_phonenumber, Customer_email) VALUES (?,?,?,?,?)";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Start a transaction
			conn.setAutoCommit(false); // Prevents each query from immediately altering the database

			// Obtain auto incremented values like so
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getAddress());
			ps.setString(4, customer.getPhoneNumber());
			ps.setString(5, customer.getEmail());

			int rowsAffected = ps.executeUpdate(); // If 0 is returned, my data didn't save
			if (rowsAffected != 0) {
				// If I want my keys do this code
				ResultSet keys = ps.getGeneratedKeys();
				// List a of all generated keys
				if (keys.next()) {
					int key = keys.getInt(1); // Give me the auto generated key
					customer.setId(key);
					
				}
				conn.commit(); // Executes ALL queries in a given transaction. Green button
				return customer;
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(Customer customer) {
		String sql = "UPDATE Customer SET Customer_firstname = ?, Customer_lastname = ?, Customer_address = ?, Customer_phonenumber = ?, Customer_email = ? WHERE Customer_id = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			//Get all the customer info from the customer object
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getAddress());
			ps.setString(4, customer.getPhoneNumber());
			ps.setString(5, customer.getEmail());
			ps.setInt(6, customer.getId());
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(Customer customer) {
		String sql = "DELETE FROM Customer WHERE Customer_firstname = ? AND Customer_lastname = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName()); // This auto escapes any malicious characters
			
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM Customer WHERE Customer_id = ?";
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
		String sql = "DELETE FROM Customer WHERE Customer_id = ?";
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
