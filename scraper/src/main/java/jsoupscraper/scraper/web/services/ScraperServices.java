package jsoupscraper.scraper.web.services;

import java.util.List;
import java.util.Optional;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.model.PriceDetails;

public interface ScraperServices {
	Product saveNewProduct(String productUrl);
	Product parseNameAndSkuFromUrl(String productUrl);
	PriceDetails scrapeProductPriceDetails(Product product);
	void deleteProduct(String productId);
	List<Product>  getAllProducts();
	List<Product> getProductsOnSale();
	Optional<Product> getProductById(String productId);
	Product getProductBySKU(String productSKU);
	void checkLowestPrice(PriceDetails priceDetails);
}
