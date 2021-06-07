package jsoupscraper.scraper.web.services;

import java.util.List;
import java.util.Optional;

import jsoupscraper.scraper.web.model.Product;

public interface ScraperServices {
	Product saveNewProduct(Product product);
	Product getProductPrice(Product product);
	void deleteProduct(String productId);
	List<Product>  getAllProducts();
	List<Product> getProductsOnSale();
	Optional<Product> getProductById(String productId);


}
