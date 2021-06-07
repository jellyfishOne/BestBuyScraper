package jsoupscraper.scraper.web.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.repository.ProductRepository;

@Service
public class ScraperServicesImpl implements ScraperServices {
	
	@Autowired
	ProductRepository productRepo;

	@Override
	public Product saveNewProduct(Product product) {
		parseProductUrl(product);
		
		//Check if SKU is already in database
		Product checkIfProductIsSaved = productRepo.findByProductSKU(product.getProductSKU());
		if(checkIfProductIsSaved != null) {
			return checkIfProductIsSaved;
		}else {
			Product updatedProduct = getProductPrice(product);
			productRepo.save(updatedProduct);
			return updatedProduct;
		}
	}
	
	@Override
	public void deleteProduct(String productId) {
		productRepo.deleteById(productId);
	}
	

	@Override
	public List<Product> getAllProducts() {
		List<Product> allProducts = productRepo.findAll();
		return allProducts;
	}
	
	@Override
	public List<Product> getProductsOnSale(){
		return productRepo.findByProductOnSaleIsTrue();
	}
	
	@Override
	public Optional<Product> getProductById(String productId) {
		return productRepo.findById(productId);
	}
	
	@Override
	public Product getProductPrice(Product product) {
		String currentPrice = "";
		String savings = "";
		String regularPrice = "";
		
		try {
			Document document = Jsoup.connect(product.getProductUrl()).get();
			Elements body = document.select("div.priceView-price");
			
			for(Element step: body){
				currentPrice = step.select("div.priceView-customer-price span[aria-hidden]").text();
				savings = step.select("div.pricing-price__savings").text();
				regularPrice = step.select("span div.pricing-price__regular-price[aria-hidden]").text();	
			}
			
		} catch(IOException e) {
			e.getMessage();
		}
		
		Product updatedProduct = parsePrice(product, regularPrice, savings, currentPrice);
		updatedProduct.setLastUpdate(LocalDateTime.now());
		
		return updatedProduct;
	}
	
	// Helper Methods //
		
	public Product parsePrice(Product product, String regularPrice, String savings, String currentPrice) {		
		if(savings.isEmpty()) {
			String newCurrentPrice = currentPrice.replaceAll("[$,]", "");
			product.setProductSavings("0");
			product.setProductCurrentPrice(newCurrentPrice);
			product.setProductRegularPrice(newCurrentPrice);
			product.setProductOnSale(false);
		} else {
			String newCurrentPrice = currentPrice.replaceAll("[$,]", "");
			String[] regularPriceArray = regularPrice.split(" ");
			String[] savingsArray = savings.split(" ");
			String newRegularPrice = regularPriceArray[1].replaceAll("[$,]", "");
			String newSavings = savingsArray[1].replace("$", "");
			
			product.setProductCurrentPrice(newCurrentPrice);
			product.setProductSavings(newSavings);
			product.setProductRegularPrice(newRegularPrice);
			product.setProductOnSale(true);
		}
		//Check if current price is lowest price
		checkLowestPrice(product);
		return product;
	}
	
	/*TODO convert string to BigDecimal so that in the future I can calculate historical savings amount
	 * 
	 * Converting String to BigDecimal to calculate savings
	 * 	System.out.println(new BigDecimal(135.69)); //135.68999999999999772626324556767940521240234375
	 * 	System.out.println(new BigDecimal("135.69")); // 135.69
	 * 	System.out.println(BigDecimal.valueOf(135.69)); // 135.69	
	 */
	public void checkLowestPrice(Product product) {
		if(product.getProductPreviousLowestPrice() == null) {
			product.setProductLowestPrice(product.getProductCurrentPrice());
			product.setProductPreviousLowestPrice("No previous lowest price");;
		} else {
			BigDecimal cPrice = new BigDecimal(product.getProductCurrentPrice());
			BigDecimal rPrice = new BigDecimal(product.getProductRegularPrice());
			BigDecimal lPrice = new BigDecimal(product.getProductLowestPrice());
			BigDecimal diff = rPrice.subtract(cPrice);
			
			// Returns: -1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
			if(diff.compareTo(lPrice)  == -1 ) {
				product.setProductPreviousLowestPrice(product.getProductLowestPrice());
				product.setProductLowestPrice(product.getProductCurrentPrice());
			}	
		}
	}
	
	public void parseProductUrl(Product product) {
		//TODO validate for: site/combo/
		//TODO validate its a bestbuy URL
		String productUrl = product.getProductUrl();
		
		int lengthOfSite = "site/".length();
		int lastIndexOfSite = productUrl.indexOf("site/");
		int lastIndexOfForwardSlash = productUrl.lastIndexOf("/");
		int dot = productUrl.lastIndexOf(".");
		
		product.setProductName(productUrl.substring(lastIndexOfSite +lengthOfSite , lastIndexOfForwardSlash));
		product.setProductSKU(productUrl.substring(lastIndexOfForwardSlash + 1, dot));	
	}

}
