package com.lenskart.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDTO {

	private int id;
	private List<ProductDTO> products;
	private UserDTO customer;
	private int totalQuantity;
	private double totalPrice;

}
