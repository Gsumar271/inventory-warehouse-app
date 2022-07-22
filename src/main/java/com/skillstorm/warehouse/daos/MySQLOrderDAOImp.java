/**
 * 
 */
package com.skillstorm.warehouse.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.warehouse.conf.WarehouseDbCreds;
import com.skillstorm.warehouse.models.Customer;
import com.skillstorm.warehouse.models.Order;
import com.skillstorm.warehouse.models.Supplier;

/**
 * @author eugenesumaryev
 *
 */
public class MySQLOrderDAOImp implements InventoryDAO<Order> {

	@Override
	public List<Order> findAll() {
		String sql = "SELECT Order_id, Order_type, Product_name, Supplier_Customer, Order_quantity, Order_itemPrice, Order_note FROM Order_table";

		// Connection will auto close in the event of a failure. Due to Autocloseable
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			// Create a Statement using the Connection object
			Statement stmt = conn.createStatement();

			// Executing the query returns a ResultSet which contains all of the values
			// returned
			ResultSet rs = stmt.executeQuery(sql);
			LinkedList<Order> orders = new LinkedList<>();

			// next() returns a boolean on whether the iterator is done yet
			// You need to advance the cursor with it so that you can parse all of the
			// results
			while (rs.next()) {
				// Looping over individual rows of the result set
				Order order = new Order(rs.getInt("Order_id"), rs.getString("Order_type"),
						rs.getString("Product_name"), rs.getString("Supplier_Customer"),
						rs.getInt("Order_quantity"), rs.getDouble("Order_itemPrice"),
						rs.getString("Order_note"));

				orders.add(order);
			}

			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Order findById(int id) {
		// find supplier by Id
		String sql = "SELECT Order_id, Order_type, Product_name, Supplier_Customer, Order_quantity, Order_itemPrice, Order_note FROM Order_table WHERE Order_id = "
				+ id;
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return new Order(rs.getInt("Order_id"), rs.getString("Order_type"),
						rs.getString("Product_name"), rs.getString("Supplier_Customer"),
						rs.getInt("Order_quantity"), rs.getDouble("Order_itemPrice"),
						rs.getString("Order_note"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Order findByName(String name) {
		// Use parameterized queries
		//returns the type of order (purchase order or sales order)
		String sql = "SELECT Order_id, Order_type, Product_name, Supplier_Customer, Order_quantity, Order_itemPrice, Order_note FROM Order_table WHERE Order_type = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, name); // This auto escapes any malicious characters
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // Make sure there was at least one item there
				return new Order(rs.getInt("Order_id"), rs.getString("Order_type"),
						rs.getString("Product_name"), rs.getString("Supplier_Customer"),
						rs.getInt("Order_quantity"), rs.getDouble("Order_itemPrice"),
						rs.getString("Order_note"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Order save(Order order) {
		
		Date date;
		// 
		String sql = "INSERT INTO Order_table (Order_type, Product_name, Supplier_Customer, Order_quantity, Order_itemPrice, Order_note) VALUES (?,?,?,?,?,?)";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Start a transaction
			conn.setAutoCommit(false); // Prevents each query from immediately altering the database

			// Obtain auto incremented values like so
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, order.getType());
			ps.setString(2, order.getProductName());
			ps.setString(3, order.getSupplierOrCustomer());
			ps.setInt(4, order.getOrderQuantity());
		//	ps.setDate(5, order.getDate());
			ps.setDouble(5, order.getItemPrice());
			ps.setString(6, order.getNote());
			//convert date to sql date 
			//date = Date.valueOf(order.getDate());
			//ps.setDate(5, date);
			//ps.setDate(5, new java.sql.Date(order.getDate().getDate()));
			

			int rowsAffected = ps.executeUpdate(); // If 0 is returned, my data didn't save
			if (rowsAffected != 0) {
				// If I want my keys do this code
				ResultSet keys = ps.getGeneratedKeys();
				// List a of all generated keys
				if (keys.next()) {
					int key = keys.getInt(1); // Give me the auto generated key
					order.setId(key);
				
				}
				conn.commit(); // Executes ALL queries in a given transaction. Green button
				return order;
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	} //Order_type, Product_name, Supplier_Customer, Order_quantity, Order_date, Order_itemPrice, Order_note

	@Override
	public void update(Order order) {
		String sql = "UPDATE Order_table SET Order_type = ?, Product_name = ?, Supplier_Customer = ?, Order_quantity = ?, Order_itemPrice = ?, Order_note = ? WHERE Order_id = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			Date date;

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, order.getType());
			ps.setString(2, order.getProductName());
			ps.setString(3, order.getSupplierOrCustomer());
			ps.setInt(4, order.getOrderQuantity());
		//	ps.setDate(5, order.getDate());
			ps.setDouble(5, order.getItemPrice());
			ps.setString(6, order.getNote());
			ps.setInt(7, order.getId());
			//convert date to sql date 
			//date = Date.valueOf(order.getDate());
			
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(Order order) {
		String sql = "DELETE FROM Order_table WHERE Order_type = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, order.getType()); // This auto escapes any malicious characters
			int row = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM Order_table WHERE Order_id = ?";
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
		String sql = "DELETE FROM Order WHERE Order_id = ?";
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
