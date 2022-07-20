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
	private String type;
	private int productId; //reference to a purchased product
	private int orderQuantity; //size of the order 
	private String note;
	//private @JsonFormat(pattern = "MM/dd/yyyy")LocalDate date; //transaction date
	private Date date;
	
	public Order() {
		
	}
	

	public Order(int id, String type, int productId, int orderQuantity, String note, Date date) {
		super();
		this.id = id;
		this.type = type;
		this.productId = productId;
		this.orderQuantity = orderQuantity;
		this.note = note;
		this.date = date;
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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


}
