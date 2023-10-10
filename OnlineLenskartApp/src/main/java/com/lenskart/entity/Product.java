package com.lenskart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	@Column
	@NotNull
	@NotEmpty(message="username cannot be empty")
	private String productName;
	@OneToOne
	@JoinColumn(name = "category_id")
	private Category category;
	@Column
	private double productPrice;
	@Column
	private String productImage;
	@Column
	private int quantity;
	@Column
	private String brand;

}
