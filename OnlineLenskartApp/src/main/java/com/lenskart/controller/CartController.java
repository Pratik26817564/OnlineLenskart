package com.lenskart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lenskart.dto.CartDTO;
import com.lenskart.serviceimpl.CartServiceImpl;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CartServiceImpl cartServiceImpl;

	@PostMapping("/add")
	public CartDTO addToCart(@RequestParam int customerId, @RequestParam int productId) {
		return cartServiceImpl.addToCart(customerId, productId);
	}

	@DeleteMapping("/deleteProducts")
	public String deleteProductCart(@RequestParam int customerId, @RequestParam int productId) {
		cartServiceImpl.deleteProduct(customerId, productId);
		return "deleted successfully";
	}
}
