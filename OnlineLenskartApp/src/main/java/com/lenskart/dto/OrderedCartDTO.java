package com.lenskart.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;


import lombok.Data;
@Data
public class OrderedCartDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int orderid;

	@ManyToMany
	@JoinTable(name = "deletedcart_product", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<ProductDTO> products = new ArrayList<ProductDTO>();

	@OneToOne
	@JoinColumn(name = "customer_id")
	private UserDTO customer;	

	private int totalQuantity;
	private double totalPrice;
	
}
