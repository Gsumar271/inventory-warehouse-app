/**
 * 
 */
package com.skillstorm.warehouse.models;

import java.time.LocalDate;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author eugenesumaryev
 *
 */
public class Order {
	
	private int id;
	private String type; //Purchase or Sales 
	private String productName; //reference to a purchased product
	private String supplierOrCustomer; //Supplier or Customer name 
	private int orderQuantity; //size of the order 
//	private Date date;
	private double itemPrice; // price oer Item
	private String note;
	
public Order() {
		
	}
	
	public String getProductName() {
	return productName;
}

public void setProductName(String productName) {
	this.productName = productName;
}

public String getSupplierOrCustomer() {
	return supplierOrCustomer;
}

public void setSupplierOrCustomer(String supplierOrCustomer) {
	this.supplierOrCustomer = supplierOrCustomer;
}

public double getItemPrice() {
	return itemPrice;
}

public void setItemPrice(double itemPrice) {
	this.itemPrice = itemPrice;
}

	public Order(int id, String type, String productName, String supplierOrCustomer, int orderQuantity,
			double itemPrice, String note) {
		super();
		this.id = id;
		this.type = type;
		this.productName = productName;
		this.supplierOrCustomer = supplierOrCustomer;
		this.orderQuantity = orderQuantity;
	//	this.date = date;
		this.itemPrice = itemPrice;
		this.note = note;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
/*
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	*/


}
