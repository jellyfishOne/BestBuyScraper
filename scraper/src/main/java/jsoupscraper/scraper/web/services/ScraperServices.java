package jsoupscraper.scraper.web.services;

import java.util.List;
import java.util.Optional;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.model.PriceDetails;

public interface ScraperServices {
	Product saveNewProduct(Product product);
	void parseNameAndSkuFromUrl(Product product);
	PriceDetails scrapeProductPriceDetails(Product product);
	void deleteProduct(String productId);
	List<Product>  getAllProducts();
	List<Product> getProductsOnSale();
	Optional<Product> getProductById(String productId);
	void checkLowestPrice(PriceDetails priceDetails);


}
