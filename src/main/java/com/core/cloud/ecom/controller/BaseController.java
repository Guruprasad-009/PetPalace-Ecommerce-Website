package com.core.cloud.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.core.cloud.ecom.entity.Product;
import com.core.cloud.ecom.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import com.core.cloud.ecom.entity.User;

@Controller
public class BaseController {

	@Autowired
	private ProductRepository repo;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@GetMapping
	public String homePage() {
		return "index";
	}

	@GetMapping("/about")
	public String aboutPage() {
		return "about";
	}

	@GetMapping("/contact")
	public String contactPage() {
		return "contact";
	}
	
	private boolean isAdmin(HttpSession session) {

	    User user = (User) session.getAttribute("loggedInUser");

	    return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
	}

	@GetMapping("/add-product")
	public String openForm(Model m, HttpSession session) {

	    if (!isAdmin(session)) {
	        return "redirect:/user-dashboard";
	    }

	    Product p = new Product();
	    m.addAttribute("product", p);
	    return "form";
	}

	@PostMapping("/save")
	public String saveProduct(@ModelAttribute Product p,
	                          @RequestParam("imageFile") MultipartFile file,
	                          HttpSession session) throws IOException {

	    if (!isAdmin(session)) {
	        return "redirect:/user-dashboard";
	    }

		if (!file.isEmpty()) {
			String originalName = file.getOriginalFilename();
			String extension = originalName.substring(originalName.lastIndexOf("."));
			String filename = UUID.randomUUID() + extension;

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

			p.setImage(filename);
		}

		repo.save(p);

		return "redirect:/view-products";
	}

	@GetMapping("/view-products")
	public String viewProducts(Model m, HttpSession session) {

	    if (!isAdmin(session)) {
	        return "redirect:/user-dashboard"; // or return "redirect:/login";
	    }

	    List<Product> list = repo.findAll();
	    m.addAttribute("products", list);

	    return "view-products";
	}

	@GetMapping("/edit-product/{id}")
	public String editProduct(@PathVariable int id,
	                          Model m,
	                          HttpSession session) {

	    if (!isAdmin(session)) {
	        return "redirect:/user-dashboard";
	    }

	    Product p = repo.findById(id).orElse(null);
	    m.addAttribute("product", p);
	    return "edit-product";
	
	}

	@PostMapping("/update-product")
	public String updateProduct(@ModelAttribute Product p,
	                            @RequestParam("imageFile") MultipartFile file,
	                            HttpSession session) throws IOException {

	    if (!isAdmin(session)) {
	        return "redirect:/user-dashboard";
	    }

		if (!file.isEmpty()) {
			if (p.getImage() != null && !p.getImage().isEmpty()) {
				Path oldImagePath = Paths.get(uploadDir, p.getImage());
				Files.deleteIfExists(oldImagePath);
			}

			String originalName = file.getOriginalFilename();
			String extension = originalName.substring(originalName.lastIndexOf("."));
			String newFilename = UUID.randomUUID() + extension;

			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Files.copy(file.getInputStream(), uploadPath.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);
			p.setImage(newFilename);

		}
		repo.save(p);
		return "redirect:/view-products";
	}

	@GetMapping("/delete-product/{id}")
	public String deleteProduct(@PathVariable int id,
	                            HttpSession session) {

	    if (!isAdmin(session)) {
	        return "redirect:/user-dashboard";
	    }

	    repo.deleteById(id);
	    return "redirect:/view-products";
	}

}
