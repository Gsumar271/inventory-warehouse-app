package com.skillstorm.warehouse.models;

import java.time.LocalDate;
/**
 * @author yevgsumaryev
 *
 */

public class PurchaseOrder {

	private int id;
	private int productId; //reference to a purchased product
	private int purchaseAmount; //size of the order 
	private String note;
	private LocalDate date; //transaction date

	public PurchaseOrder() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(int purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

}
