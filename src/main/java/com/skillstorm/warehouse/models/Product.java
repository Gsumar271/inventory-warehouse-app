package com.skillstorm.warehouse.models;

/**
 * @author yevgsumaryev
 *
 */
	public class Product {


	private int id; // ProductId in the database
	private String name;
	private String desc;
	private int supplierId;
	private double itemPrice;  //original purchase price of item
	private double salesPrice; //sale price of an item 
	private int quantity;
	
	
	public Product() {
		
	}

	// Use this one for auto increment ids
	public Product(String name) {
		this.name = name;
	}
	
	public Product(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Product(int id, String name, String desc, int supplierId, double itemPrice, double salesPrice,
			int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.supplierId = supplierId;
		this.itemPrice = itemPrice;
		this.salesPrice = salesPrice;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplier(int supplierId) {
		this.supplierId = supplierId;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}

}

