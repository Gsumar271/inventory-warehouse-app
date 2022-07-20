/**
 * 
 */
package com.skillstorm.warehouse.daos;

import java.util.List;

import com.skillstorm.warehouse.models.Product;

/**
 * @author eugenesumaryev
 *
 */
public interface InventoryDAO<T> {
	
	// CRUD is Create, Read, Update, Delete
	
	public List<T> findAll();
	public T findById(int id);
	public T findByName(String name);
	// You can also use the returned artist to test if it's in the table
	public T save(T object);
	public void update(T object); // contains the id and updates as needed
	public void delete(T object);
	public void delete(int id);
	public void deleteMany(int[] ids);
	
	

}
