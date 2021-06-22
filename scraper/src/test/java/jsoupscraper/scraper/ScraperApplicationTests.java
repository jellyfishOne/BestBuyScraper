package jsoupscraper.scraper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jsoupscraper.scraper.web.model.PriceDetails;
import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.services.ScraperServices;

@SpringBootTest
class ScraperApplicationTests {
	
	@Autowired
	private ScraperServices scraperService;
	
	private String productUrl = "https://www.bestbuy.com/site/apple-airpods-pro-white/5706659.p?skuId=5706659";

	@Test
	void testSaveNewProduct() {
		Product savedProduct = scraperService.saveNewProduct(productUrl);
		assertNotNull(savedProduct);
		
	}
	
	@Test
	void parseNameAndSkuFromUrl() {
		Product product = scraperService.parseNameAndSkuFromUrl(productUrl);
		assertNotNull(product);
	}
	
	@Test
	void testDeleteProduct() {
		Product savedProduct = scraperService.saveNewProduct(productUrl);
		scraperService.deleteProduct(savedProduct.getProductSKU());
	}
	
	@Test
	void testScrapProductPriceDetails() {
		Product product = Product.builder().productUrl(productUrl).build(); 
		PriceDetails priceDetails = scraperService.scrapeProductPriceDetails(product);
		assertNotNull(priceDetails);
	}
	@Test
	void testGetAllProducts() {
		List<Product> productList = scraperService.getAllProducts();
		assertNotNull(productList);
	}
	
	@Test
	void testGetProductsOnSale() {
		List<Product> productList = scraperService.getProductsOnSale();
		assertNotNull(productList);
	}
	@Test
	void testGetProductById() {
		Product savedProduct = scraperService.saveNewProduct(productUrl);
		Optional<Product> returnedProduct = scraperService.getProductById(savedProduct.getId());
		assertNotNull(returnedProduct);
	}
	
	@Test
	void testCheckLowestPrice() {
		Product savedProduct = scraperService.saveNewProduct(productUrl);
		scraperService.checkLowestPrice(savedProduct.getPriceDetails());
	}
}
