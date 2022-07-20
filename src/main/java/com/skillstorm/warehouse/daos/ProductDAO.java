/**
 * 
 */
package com.skillstorm.warehouse.daos;

import java.util.List;
import com.skillstorm.warehouse.models.*;

/**
 * @author eugenesumaryev
 *
 */
public interface ProductDAO {

	// CRUD is Create, Read, Update, Delete

	public List<Product> findAll();

	public Product findById(int id);

	public Product findByName(String name);

	// I like to return the Artist since we can get it's key (if auto incremeneted)
	// You can also use the returned artist to test if it's in the table
	public Product save(Product product);

	public void updateName(Product product); // contains the id and updates as needed

	public void delete(Product product);

	public void delete(int id);

	public void deleteMany(int[] ids);

}
