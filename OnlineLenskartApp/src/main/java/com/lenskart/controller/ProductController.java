package com.lenskart.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lenskart.dto.ProductDTO;
import com.lenskart.serviceimpl.ProductServiceImpl;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductServiceImpl productServiceImpl;
 
	@PostMapping("/addProduct")
	public ProductDTO addNewProduct(@Valid @RequestBody ProductDTO productDTO) {
		return productServiceImpl.addProduct(productDTO);
	}

	@GetMapping("/getProductById/{id}")
	public ProductDTO getProductById(@PathVariable(value = "id") int id) {
		return productServiceImpl.getById(id);
	}

	@PutMapping("/updateProduct/{no}")
	public String updateProduct(@Valid @PathVariable(value = "no") int no, @RequestBody ProductDTO product) {

		return productServiceImpl.updateProduct(no, product);
	}

	@DeleteMapping("/deleteProductById/{no}")
	public boolean deleteProduct(@PathVariable(value = "no") int no) {
		productServiceImpl.deleteProduct(no);

		return true;
	}

	@GetMapping("/getProductByBrand/{name}")
	public List<ProductDTO> getProductById(@PathVariable(value = "name") String name) {
		return productServiceImpl.getProductByBrand(name);
	}

	

}
