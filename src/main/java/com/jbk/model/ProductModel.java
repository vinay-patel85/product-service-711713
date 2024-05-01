package com.jbk.model;

import java.util.Comparator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

// {"productId":101,"productName":"Pen","supplierId":1,"categoryId":1,"productQty":100,"productPrice":10,"deliveryCharges":25}

public class ProductModel {
	
	private long productId;
	
	
	@NotBlank(message = "Product name should not be blank")
	@Pattern(regexp = "^(?![\\d ])[\\p{Alnum} ]+$", message = "Product name must only contain alphabetic characters and spaces")
	private String productName;
	
	
	private long supplierId;
	
	
	private long categoryId;
	
	@Min(value = 1)
	private int productQty;
	
	@Min(value = 1)
	private double productPrice;
	
	@Min(value = 0)
	private int deliveryCharges;
	
	public ProductModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductModel(long productId,
			@NotBlank(message = "Product name should not be blank") @Pattern(regexp = "^(?![\\d ])[\\p{Alnum} ]+$", message = "Product name must only contain alphabetic characters and spaces") String productName,
			long supplierId, long categoryId, @Min(1) int productQty, @Min(1) double productPrice,
			@Min(0) int deliveryCharges) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.supplierId = supplierId;
		this.categoryId = categoryId;
		this.productQty = productQty;
		this.productPrice = productPrice;
		this.deliveryCharges = deliveryCharges;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getProductQty() {
		return productQty;
	}

	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public int getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(int deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	@Override
	public String toString() {
		return "ProductModel [productId=" + productId + ", productName=" + productName + ", supplierId=" + supplierId
				+ ", categoryId=" + categoryId + ", productQty=" + productQty + ", productPrice=" + productPrice
				+ ", deliveryCharges=" + deliveryCharges + "]";
	}

	
	
	
}
