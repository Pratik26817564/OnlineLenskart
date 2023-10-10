package com.lenskart.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lenskart.dto.CartDTO;
import com.lenskart.dto.OrdersDTO;
import com.lenskart.dto.ProductDTO;
import com.lenskart.dto.UserDTO;
import com.lenskart.entity.Cart;
import com.lenskart.entity.CustomerEntity;
import com.lenskart.entity.OrderedCart;
import com.lenskart.entity.Orders;
import com.lenskart.entity.Product;
import com.lenskart.entity.Status;
import com.lenskart.exception.CustomerNotFoundException;
import com.lenskart.repository.CartRepository;
import com.lenskart.repository.OrderedCartRepository;
import com.lenskart.repository.OrdersRepository;
import com.lenskart.service.OrdersService;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private OrderedCartRepository orderedCartRepository;

	public OrdersDTO addOrders(int customerId) {

		Cart cart;
		OrdersDTO ordersDTO = new OrdersDTO();
		try {
			cart = cartRepository.findByCustomerId(customerId).orElseThrow(() -> new CustomerNotFoundException());
		
		if (cart != null) {
			CartDTO cartDTO = mapCartToDTO(cart);

			OrderedCart orderedCart = new OrderedCart();

			List<Product> products = cart.getProducts();

			List<Product> products2 = new ArrayList<Product>();

			for (Product product : products) {

				Product product3 = new Product();

				product3.setBrand(product.getBrand());
				product3.setCategory(product.getCategory());
				product3.setProductId(product.getProductId());
				product3.setProductImage(product.getProductImage());
				product3.setProductName(product.getProductName());
				product3.setProductPrice(product.getProductPrice());
				product3.setQuantity(product.getQuantity());

				products2.add(product3);

			}

			CustomerEntity customer = cart.getCustomer();
			Orders order = new Orders();

			order.setCart(cart);
			order.setDate(LocalDateTime.now());
			order.setStatus(Status.pending);

			ordersRepository.save(order);

			orderedCart.setProducts(products2);
			orderedCart.setCustomer(customer);
			orderedCart.setOrderid(order.getOrderId());
			orderedCart.setTotalPrice(cart.getTotalPrice());
			orderedCart.setTotalQuantity(cart.getTotalQuantity());

			orderedCartRepository.save(orderedCart);

			cart.getProducts().clear();
			cart.setTotalPrice(0);
			cart.setTotalQuantity(0);

			cartRepository.save(cart);

			
			ordersDTO.setOrderId(order.getOrderId());
			ordersDTO.setCart(cartDTO);
			ordersDTO.setDate(LocalDateTime.now());
			ordersDTO.setStatus(Status.pending);

			

		}
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return ordersDTO;
	}

	
	public OrdersDTO getById(int id) {

		Orders order = ordersRepository.findById(id).get();
		OrderedCart cart = orderedCartRepository.findByOrderId(id);

		OrdersDTO orderDTO = new OrdersDTO();

		orderDTO.setCart(mapCartToDTO(cart));

		orderDTO.setDate(order.getDate());
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setStatus(order.getStatus());

		return orderDTO;
	}

	

	public List<OrdersDTO> findAl() {

		List<Orders> orders = ordersRepository.findAll();
		

		List<OrdersDTO> ordersDTOs = new ArrayList<OrdersDTO>();
		List<OrderedCart> carts = orderedCartRepository.findAll();
		for (Orders order : orders) {
			OrdersDTO orderDTO = new OrdersDTO();
			for (OrderedCart cart : carts) {
				if (order.getOrderId() == cart.getOrderid())
					orderDTO.setCart(mapCartToDTO(cart));
			}
			orderDTO.setDate(order.getDate());
			orderDTO.setOrderId(order.getOrderId());
			orderDTO.setStatus(order.getStatus());

			ordersDTOs.add(orderDTO);

		}

		return ordersDTOs;
	}

	public String deleteOrders(int orderId) {

		OrderedCart cart = orderedCartRepository.findByOrderId(orderId);

		cart.getProducts().clear();

		ordersRepository.deleteById(orderId);
		orderedCartRepository.deleteByOrderId(orderId);
		return "Deleted Successfully";
	}

	public List<OrdersDTO> getOrderCustomerId(int customerId) {
		
		List<OrderedCart> carts=orderedCartRepository.findByCustomerId(customerId);
		
		List<Orders> orders=ordersRepository.findAll();
		
		List<OrdersDTO> ordersDTOs=new ArrayList<OrdersDTO>();
		
		for(Orders order:orders)
		{
			for(OrderedCart cart:carts)
			{
				if (order.getOrderId()==cart.getOrderid())
				{
					OrdersDTO orderDTO=new OrdersDTO();
					orderDTO.setDate(order.getDate());
					orderDTO.setOrderId(order.getOrderId());
					orderDTO.setStatus(order.getStatus());
					orderDTO.setCart(mapCartToDTO(cart));
					
					ordersDTOs.add(orderDTO);
					
				}
			}
		}
		return ordersDTOs;

	}
	
	
	
	
	
	private CartDTO mapCartToDTO(Cart cart) {
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(cart.getId());
		cartDTO.setCustomer(mapCustomerToDTO(cart.getCustomer()));
		cartDTO.setProducts(mapProductsToDTO(cart.getProducts()));
		cartDTO.setTotalPrice(cart.getTotalPrice());
		cartDTO.setTotalQuantity(cart.getTotalQuantity());
		return cartDTO;
	}

	private UserDTO mapCustomerToDTO(CustomerEntity customer) {
		UserDTO customerDTO = new UserDTO();
		customerDTO.setId(customer.getId());
		customerDTO.setAddress(customer.getAddress());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setNumber(customer.getNumber());
		customerDTO.setUsername(customer.getUsername());
		customerDTO.setRole(customer.getRole());
		customerDTO.setName(customer.getName());
		return customerDTO;
	}

	private List<ProductDTO> mapProductsToDTO(List<Product> products) {
		return products.stream().map(product -> {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setProductId(product.getProductId());
			productDTO.setProductImage(product.getProductImage());
			productDTO.setProductName(product.getProductName());
			productDTO.setProductPrice(product.getProductPrice());
			productDTO.setQuantity(product.getQuantity());
			return productDTO;
		}).collect(Collectors.toList());
	}
	private CartDTO mapCartToDTO(OrderedCart cart) {
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(cart.getId());
		cartDTO.setCustomer(mapCustomerToDTO(cart.getCustomer()));
		cartDTO.setProducts(mapProductsToDTO(cart.getProducts()));
		cartDTO.setTotalPrice(cart.getTotalPrice());
		cartDTO.setTotalQuantity(cart.getTotalQuantity());
		return cartDTO;
	}


}
