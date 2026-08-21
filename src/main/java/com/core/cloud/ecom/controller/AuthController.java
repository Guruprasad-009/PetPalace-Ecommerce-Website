package com.core.cloud.ecom.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.core.cloud.ecom.entity.Cart;
import com.core.cloud.ecom.entity.Order;
import com.core.cloud.ecom.entity.Product;
import com.core.cloud.ecom.entity.User;
import com.core.cloud.ecom.entity.Wishlist;
import com.core.cloud.ecom.repository.CartRepository;
import com.core.cloud.ecom.repository.OrderRepository;
import com.core.cloud.ecom.repository.ProductRepository;
import com.core.cloud.ecom.repository.UserRepository;
import com.core.cloud.ecom.repository.WishlistRepository;
import com.core.cloud.ecom.utility.CartItem;
import com.core.cloud.ecom.utility.WishlistItem;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private WishlistRepository wishlistRepo;

	@Autowired
	private OrderRepository orderRepo;

	@GetMapping("/user/register")
	public String userRegistrationForm(Model m) {
		User u = new User();
		m.addAttribute("user", u);
		return "register";
	}

	@PostMapping("/user/save")
	public String saveUser(@ModelAttribute User u) {
		System.out.println(u);
		u.setRole("USER");
		userRepo.save(u);
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	@PostMapping("/login-user")
	public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session,
			RedirectAttributes ra) {
		User u = userRepo.findByEmail(email);

		if (u == null) {
			System.out.println("Email not Exists");
			ra.addFlashAttribute("error", "Invalide Email or Password");
			return "redirect:/login";
		}

		if (!u.getPassword().equals(password)) {
			System.out.println("Password not Exists");
			ra.addFlashAttribute("error", "Invalide Email or Password");
			return "redirect:/login";
		}
		System.out.println("Login Success");
		session.setAttribute("loggedInUser", u);
		if ("ADMIN".equals(u.getRole())) {
			return "redirect:/admin-dashboard";
		}
		return "redirect:/user-dashboard";
	}

	@GetMapping("/user-dashboard")
	public String userDashbord(HttpSession session, Model m) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/login";
		}

		List<Product> list = productRepo.findAll();
		m.addAttribute("products", list);

		return "user-dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loggedInUser");
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("/add-to-cart/{productId}")
	public String addToCart(@PathVariable int productId, HttpSession session, RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		int userId = user.getId();

		Optional<Cart> existingCart = cartRepo.findByUserIdAndProductId(userId, productId);

		if (existingCart.isPresent()) {

			Cart cart = existingCart.get();
			cart.setQuantity(cart.getQuantity() + 1);
			cartRepo.save(cart);

			redirectAttributes.addFlashAttribute("success", "Product quantity updated in cart successfully!");

		} else {

			Cart cart = new Cart();
			cart.setUserId(userId);
			cart.setProductId(productId);
			cart.setQuantity(1);

			cartRepo.save(cart);

			redirectAttributes.addFlashAttribute("success", "Product added to cart successfully!");
		}

		return "redirect:/user-dashboard";
	}

	@GetMapping("/my-cart")
	public String addToCart(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		List<Cart> cartList = cartRepo.findByUserId(user.getId());
		ArrayList<CartItem> cartItems = new ArrayList<>();
		double grandTotal = 0;
		for (Cart cart : cartList) {
			Optional<Product> productOptional = productRepo.findById(cart.getProductId());

			if (productOptional.isPresent()) {
				Product product = productOptional.get();

				CartItem item = new CartItem(cart.getId(), product, cart.getQuantity());
				cartItems.add(item);

				grandTotal += item.getTotalPrice();
			}
		}

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("grandTotal", grandTotal);

		return "my-cart";
	}

	@GetMapping("/increase-quantity/{cartId}")
	public String increaseQuantity(@PathVariable int cartId) {

		Optional<Cart> optionalCart = cartRepo.findById(cartId);

		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			cart.setQuantity(cart.getQuantity() + 1);
			cartRepo.save(cart);

		}
		return "redirect:/my-cart";
	}

	@GetMapping("/decrease-quantity/{cartId}")
	public String decreaseQuantity(@PathVariable int cartId) {

		Optional<Cart> optionalCart = cartRepo.findById(cartId);

		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();

			if (cart.getQuantity() > 1) {
				cart.setQuantity(cart.getQuantity() - 1);
				cartRepo.save(cart);

			}
		}

		return "redirect:/my-cart";
	}

	@GetMapping("/remove-item/{cartId}")
	public String remove_item(@PathVariable int cartId) {
		cartRepo.deleteById(cartId);
		return "redirect:/my-cart";

	}

	@GetMapping("/search")
	public String searchProducts(@RequestParam("keyword") String keyword, Model model, HttpSession session) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		List<Product> products = productRepo.findByNameContainingIgnoreCase(keyword);

		model.addAttribute("products", products);
		model.addAttribute("keyword", keyword);

		return "user-dashboard";
	}

	@GetMapping("/search-cart")
	public String searchCart(@RequestParam String keyword, HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		List<Cart> cartList = cartRepo.findByUserId(user.getId());

		ArrayList<CartItem> cartItems = new ArrayList<>();
		double grandTotal = 0;

		for (Cart cart : cartList) {

			Optional<Product> optional = productRepo.findById(cart.getProductId());

			if (optional.isPresent()) {

				Product product = optional.get();

				if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {

					CartItem item = new CartItem(cart.getId(), product, cart.getQuantity());

					cartItems.add(item);

					grandTotal += item.getTotalPrice();
				}
			}
		}

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("grandTotal", grandTotal);
		model.addAttribute("keyword", keyword);

		return "my-cart";
	}

	@GetMapping("/add-to-wishlist/{productId}")
	public String addToWishlist(@PathVariable int productId, HttpSession session,
			RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		Optional<Wishlist> existing = wishlistRepo.findByUserIdAndProductId(user.getId(), productId);

		if (existing.isPresent()) {

			redirectAttributes.addFlashAttribute("warning", "This product is already in your Wishlist!");

			return "redirect:/wishlist";
		}

		Wishlist wishlist = new Wishlist();
		wishlist.setUserId(user.getId());
		wishlist.setProductId(productId);

		wishlistRepo.save(wishlist);

		redirectAttributes.addFlashAttribute("success", "Product added to Wishlist successfully!");

		return "redirect:/wishlist";
	}

	@GetMapping("/wishlist")
	public String wishlist(HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		List<Wishlist> wishlistList = wishlistRepo.findByUserId(user.getId());

		ArrayList<WishlistItem> wishlistItems = new ArrayList<>();

		for (Wishlist wish : wishlistList) {

			Optional<Product> productOptional = productRepo.findById(wish.getProductId());

			if (productOptional.isPresent()) {

				Product product = productOptional.get();

				WishlistItem item = new WishlistItem(wish.getId(), product);

				wishlistItems.add(item);
			}
		}

		model.addAttribute("wishlistItems", wishlistItems);

		return "wishlist";
	}

	@GetMapping("/remove-wishlist/{id}")
	public String removeWishlist(@PathVariable int id) {

		wishlistRepo.deleteById(id);

		return "redirect:/wishlist";
	}

	@GetMapping("/checkout")
	public String checkout(HttpSession session, Model model) {

		session.removeAttribute("buyNowProduct");
		session.removeAttribute("buyNowProductId");

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		List<Cart> cartList = cartRepo.findByUserId(user.getId());
		List<CartItem> cartItems = new ArrayList<>();
		double grandTotal = 0;

		for (Cart cart : cartList) {
			Optional<Product> optionalProduct = productRepo.findById(cart.getProductId());

			if (optionalProduct.isPresent()) {
				Product product = optionalProduct.get();

				CartItem item = new CartItem(cart.getId(), product, cart.getQuantity());
				cartItems.add(item);

				grandTotal += item.getTotalPrice();
			}
		}

		model.addAttribute("user", user);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("grandTotal", grandTotal);

		return "checkout";
	}

	@GetMapping("/buy-now/{id}")
	public String buyNow(@PathVariable int id, HttpSession session) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		Optional<Product> optional = productRepo.findById(id);

		if (optional.isEmpty()) {
			return "redirect:/user-dashboard";
		}

		Product product = optional.get();

		session.setAttribute("buyNowProduct", product);
		session.setAttribute("buyNowProductId", id);

		return "redirect:/payment";
	}

	@GetMapping("/payment")
	public String payment(HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		double grandTotal = 0.0;

		Product buyNowProduct = (Product) session.getAttribute("buyNowProduct");

		if (buyNowProduct != null) {

			grandTotal = buyNowProduct.getPrice();

			model.addAttribute("buyNowProduct", buyNowProduct);

		} else {

			List<Cart> cartList = cartRepo.findByUserId(user.getId());

			ArrayList<CartItem> cartItems = new ArrayList<>();

			for (Cart cart : cartList) {

				Optional<Product> optional = productRepo.findById(cart.getProductId());

				if (optional.isPresent()) {

					Product product = optional.get();

					CartItem item = new CartItem(cart.getId(), product, cart.getQuantity());

					cartItems.add(item);

					grandTotal += item.getTotalPrice();
				}
			}

			model.addAttribute("cartItems", cartItems);
		}

		model.addAttribute("grandTotal", grandTotal);

		return "payment";
	}

	@PostMapping("/place-order")
	public String placeOrder(@RequestParam String paymentMethod, HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		String orderNumber = "ORD-" + System.currentTimeMillis();

		if (session.getAttribute("buyNowProduct") != null) {

			Product product = (Product) session.getAttribute("buyNowProduct");

			Order order = new Order();

			order.setOrderNumber(orderNumber);
			order.setCustomer(user.getFullname());
			order.setProduct(product.getName());
			order.setQuantity(1);
			order.setTotal(product.getPrice());
			order.setPayment(paymentMethod);
			order.setDate(LocalDate.now());

			orderRepo.save(order);

			session.removeAttribute("buyNowProduct");
			session.removeAttribute("buyNowProductId");
		}

		else {

			List<Cart> cartList = cartRepo.findByUserId(user.getId());

			for (Cart cart : cartList) {

				Optional<Product> optional = productRepo.findById(cart.getProductId());

				if (optional.isPresent()) {

					Product product = optional.get();

					Order order = new Order();

					order.setOrderNumber(orderNumber);
					order.setCustomer(user.getFullname());
					order.setProduct(product.getName());
					order.setQuantity(cart.getQuantity());
					order.setTotal(product.getPrice() * cart.getQuantity());
					order.setPayment(paymentMethod);
					order.setDate(LocalDate.now());

					orderRepo.save(order);
				}
			}
		}

		cartRepo.deleteByUserId(user.getId());

		model.addAttribute("user", user);

		return "order-success";
	}

	@GetMapping("/admin-dashboard")
	public String adminDashboard(HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
			return "redirect:/user-dashboard";
		}

		long totalUsers = userRepo.count();
		long totalProducts = productRepo.count();

		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalProducts", totalProducts);
		model.addAttribute("admin", user);

		model.addAttribute("orderCount", orderRepo.count());

		model.addAttribute("outOfStockCount", productRepo.countOutOfStockProducts());

		model.addAttribute("todaySales", orderRepo.countTodaySales());
		model.addAttribute("totalAmountToday", orderRepo.getTotalAmountToday());

		return "admin-dashboard";
	}

	@GetMapping("/view-orders")
	public String viewOrders(Model model) {

		model.addAttribute("orders", orderRepo.findAllByOrderByIdDesc());

		return "view-orders";

	}

	@GetMapping("/view-users")
	public String viewUsers(Model model) {

		model.addAttribute("users", userRepo.findAll());

		return "view-users";
	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("user", user);

		return "user-profile";
	}

	@GetMapping("/edit-profile")
	public String editProfile(HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("user", user);

		return "edit-profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute("user") User user, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return "redirect:/login";
		}

		loggedInUser.setFullname(user.getFullname());
		loggedInUser.setEmail(user.getEmail());
		loggedInUser.setMobile(user.getMobile());
		loggedInUser.setAddress(user.getAddress());

		if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
			loggedInUser.setPassword(user.getPassword());
		}

		userRepo.save(loggedInUser);

		session.setAttribute("loggedInUser", loggedInUser);

		return "redirect:/profile";
	}

	@GetMapping("/my-orders")
	public String myOrders(HttpSession session, Model model) {

		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/login";
		}

		List<Order> orders = orderRepo.findByCustomerOrderByIdDesc(user.getFullname());

		model.addAttribute("orders", orders);

		return "my-orders";
	}
}
