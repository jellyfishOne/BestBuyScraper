package jsoupscraper.scraper.web.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.services.ScraperServices;

@RestController
@RequestMapping("api/v1/product-scraper")
public class ScraperController {
	
	private final ScraperServices scraperService;
	
	public ScraperController(ScraperServices scraperService) {
		this.scraperService = scraperService;
	}
	
	
	/*
	 * @The ModelAttribute annotation type binds parameters to Model objects
	 * Create new product for tracking price
	 */
	@PostMapping(value = "/save-product")
	public ResponseEntity saveNewProduct(@Valid @ModelAttribute Product product) {
		Product savedProduct = scraperService.saveNewProduct(product);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
	}
	
	@DeleteMapping(value = "/{productId}")
	public ResponseEntity deleteProduct(@PathVariable String productId){
		scraperService.deleteProduct(productId);
		return ResponseEntity.status(HttpStatus.OK).body("Document was deleted");
		
	}
	
	@GetMapping(value = "all-products")
	public ResponseEntity getProducts() {
		List<Product> products = scraperService.getAllProducts();
		return ResponseEntity.status(HttpStatus.OK).body(products);
		
	}
	
	@GetMapping(value = "/{productId}")
	public ResponseEntity getProduct(@PathVariable String productId) {
		Optional<Product> product = scraperService.getProductById(productId);
		return ResponseEntity.status(HttpStatus.OK).body(product);
		
	}
	@GetMapping(value = "/products-on-sale")
	public ResponseEntity trackProductPrice() {
		List<Product> productsOnSale = scraperService.getProductsOnSale();
		return ResponseEntity.status(HttpStatus.OK).body(productsOnSale);
	}
	

}
