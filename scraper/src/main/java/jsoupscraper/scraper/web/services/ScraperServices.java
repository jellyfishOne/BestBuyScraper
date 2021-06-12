package jsoupscraper.scraper.web.services;

import java.util.List;
import java.util.Optional;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.model.PriceDetails;

public interface ScraperServices {
	Product saveNewProduct(Product product);
	PriceDetails scrapeProductPriceDetails(Product product);
	void deleteProduct(String productId);
	List<Product>  getAllProducts();
	List<Product> getProductsOnSale();
	Optional<Product> getProductById(String productId);
	//check if product is on sale
	void checkIfProductOnsale(PriceDetails priceDetails);
	
	//check lowest price
	void checkLowestPrice(PriceDetails priceDetails);


}
