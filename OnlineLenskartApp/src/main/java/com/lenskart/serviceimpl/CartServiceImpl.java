package com.lenskart.serviceimpl;
//

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenskart.dto.CartDTO;
import com.lenskart.dto.ProductDTO;
import com.lenskart.dto.UserDTO;
import com.lenskart.entity.Cart;
import com.lenskart.entity.CustomerEntity;
import com.lenskart.entity.Product;
import com.lenskart.exception.AddToCartNotFoundException;
import com.lenskart.exception.CustomerNotFoundException;
import com.lenskart.repository.CartRepository;
import com.lenskart.repository.CustomerRepository;
import com.lenskart.repository.ProductRepository;
import com.lenskart.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	public CartDTO addToCart(int customerId, int productId) {
		double total;
		int quantity;
		CartDTO cartDTO = new CartDTO();
		CustomerEntity customer;
		try {
			customer = customerRepository.findById(customerId).orElseThrow(() -> new AddToCartNotFoundException());

			Product product = productRepository.findById(productId).orElseThrow(() -> new AddToCartNotFoundException());

			System.out.println(product);

			Cart cart = customer.getCart();

			if (cart == null) {
				cart = new Cart();
				customer.setCart(cart);

				cart.setCustomer(customer);

			}
			total = cart.getTotalPrice();

			quantity = cart.getTotalQuantity();

			quantity = quantity + 1;
			total = product.getProductPrice() + total;
			cart.setTotalPrice(total);
			cart.setTotalQuantity(quantity);
			cart.getProducts().add(product);
			System.out.println(cart.getProducts());

			cartRepository.save(cart);

			UserDTO userDTO = new UserDTO();

			userDTO.setAddress(customer.getAddress());
			userDTO.setEmail(customer.getEmail());
			userDTO.setName(customer.getName());
			userDTO.setNumber(customer.getNumber());
			userDTO.setPassword(customer.getPassword());
			userDTO.setRole(customer.getRole());
			userDTO.setId(customer.getId());
			userDTO.setUsername(customer.getUsername());

			cartDTO.setCustomer(userDTO);

			List<ProductDTO> productIds = new ArrayList<>();
			for (Product product_i : cart.getProducts()) {
				ProductDTO productDTO = new ProductDTO();
				productDTO.setBrand(product_i.getBrand());
				productDTO.setCategory(product_i.getCategory());
				productDTO.setProductId(product_i.getProductId());
				productDTO.setProductImage(product_i.getProductImage());
				productDTO.setProductName(product_i.getProductName());
				productDTO.setProductPrice(product_i.getProductPrice());
				productDTO.setQuantity(product_i.getQuantity());

				productIds.add(productDTO);
			}
			cartDTO.setProducts(productIds);
			cartDTO.setTotalPrice(total);
			cartDTO.setTotalQuantity(quantity);

		} catch (AddToCartNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return cartDTO;

	}

	public String deleteProduct(int custid, int prodid) {

		CustomerEntity customer;
		try {
			customer = customerRepository.findById(custid).orElseThrow(() -> new CustomerNotFoundException());

			Cart cart = customer.getCart();

			List<Product> products = cart.getProducts();
			int quantity;
			double totalprice;
			quantity = cart.getTotalQuantity();
			totalprice = cart.getTotalPrice();
			for (Product product : products) {
				if (product.getProductId() == prodid) {
					quantity = quantity - 1;
					totalprice = totalprice - product.getProductPrice();
					cartRepository.deleteByCartProduct(prodid);
				}
			}
			cart.setTotalPrice(totalprice);
			cart.setTotalQuantity(quantity);
			cartRepository.save(cart);

			return "deleted Successfully";
		} catch (CustomerNotFoundException e) {
			System.out.println(e);
			return "Customer not Found";
		}

	}
}