package com.skillstorm.warehouse.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import java.util.List;
import com.skillstorm.warehouse.conf.*;

import com.skillstorm.warehouse.models.Product;

public class MySQLProductDAOImpl implements ProductDAO {

	/**
	 * @return The list of products from the database if successful. Null in the
	 *         event of failure
	 */
	@Override
	public List<Product> findAll() {

		String sql = "SELECT Product_Id, Product_name FROM Product";

		// Connection will auto close in the event of a failure. Due to Autocloseable
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			// Create a Statement using the Connection object
			Statement stmt = conn.createStatement();

			// Executing the query returns a ResultSet which contains all of the values
			// returned
			ResultSet rs = stmt.executeQuery(sql);
			LinkedList<Product> products = new LinkedList<>();

			// next() returns a boolean on whether the iterator is done yet
			// You need to advance the cursor with it so that you can parse all of the
			// results
			while (rs.next()) {
				// Looping over individual rows of the result set
				Product product = new Product(rs.getInt("Product_Id"), rs.getString("Product_name"));
				products.add(product);
			}

			return products;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return The Product with the given id if found, null if the product does not
	 *         exist
	 */
	@Override
	public Product findById(int id) {
		// find product by Id
		String sql = "SELECT Product_Id, Product_name FROM Product WHERE Product_Id = " + id;
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return new Product(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Product findByName(String name) {
		// Use parameterized queries
		String sql = "SELECT Product_Id, Product_name FROM Product WHERE Product_name = ?";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Instead of using Statement, we will use PreparedStatement
			PreparedStatement ps = conn.prepareStatement(sql);
			// Java is going to check our statement ahead of time to make sure it's okay
			ps.setString(1, name); // This auto escapes any malicious characters
			ResultSet rs = ps.executeQuery();
			if (rs.next()) { // Make sure there was at least one item there
				return new Product(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Null if not found
	}

	@Override
	public Product save(Product product) {

		// If this was auto-increment, then the artistid is not needed
		String sql = "INSERT INTO Product (Product_name) VALUES (?)";
		try (Connection conn = WarehouseDbCreds.getInstance().getConnection()) {

			// Start a transaction
			conn.setAutoCommit(false); // Prevents each query from immediately altering the database

			// Obtain auto incremented values like so
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//ps.setInt(1, product.getId());
			ps.setString(1, product.getName());

			int rowsAffected = ps.executeUpdate(); // If 0 is returned, my data didn't save
			if (rowsAffected != 0) {
				// If I want my keys do this code
				ResultSet keys = ps.getGeneratedKeys();
				// List a of all generated keys
				if (keys.next()) {
					int key = keys.getInt(1); // Give me the auto generated key
					product.setId(key);
					return product;
				}
				conn.commit(); // Executes ALL queries in a given transaction. Green button
				return product;
			} else {
				conn.rollback(); // Undoes any of the queries. Database pretends those never happened
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateName(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMany(int[] ids) {
		// TODO Auto-generated method stub

	}

}
