package jsoupscraper.scraper.web.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.model.PriceDetails;
import jsoupscraper.scraper.web.repository.ProductRepository;

@Service
public class ScraperServicesImpl implements ScraperServices {
	
	@Autowired
	ProductRepository productRepo;

	@Override
	public Product saveNewProduct(String productUrl) {
		Product product = parseNameAndSkuFromUrl(productUrl);
		
		//Check if SKU is already in database
		Product checkIfProductIsSaved = productRepo.findByProductSKU(product.getProductSKU());
		if(checkIfProductIsSaved != null) {
			return checkIfProductIsSaved;
		}else {
			//scrape product price 
			PriceDetails priceDetails = scrapeProductPriceDetails(product);
			
			//check lowest price
			checkLowestPrice(priceDetails);
			
			//save product to mongo
			product.setPriceDetails(priceDetails);
			productRepo.save(product);
			return product;
		}
	}
	
	@Override
	public void deleteProduct(String productSKU) {
		productRepo.deleteByProductSKU(productSKU);
	}
	

	@Override
	public List<Product> getAllProducts() {
		List<Product> allProducts = productRepo.findAll();
		return allProducts;
	}
	
	@Override
	public List<Product> getProductsOnSale(){
		return productRepo.findByProductOnSale(true);
	}
	
	@Override
	public Optional<Product> getProductById(String productId) {
		return productRepo.findById(productId);
	}
	
	@Override
	public PriceDetails scrapeProductPriceDetails(Product product) {
		
		PriceDetails priceDetails = new PriceDetails();
		
		try {
			Document document = Jsoup.connect(product.getProductUrl()).get();
			Elements body = document.select("div.priceView-price");
			
			for(Element step: body){
				priceDetails.setProductCurrentPrice(step.select("div.priceView-customer-price span[aria-hidden]").text());
				priceDetails.setProductSavings(step.select("div.pricing-price__savings").text());
				priceDetails.setProductRegularPrice(step.select("span div.pricing-price__regular-price[aria-hidden]").text());	
			}
			
		} catch(IOException e) {
			e.getMessage();
		}
		
		if(priceDetails.getProductSavings().isEmpty()) {
			String newCurrentPrice = priceDetails.getProductCurrentPrice().replaceAll("[$,]", "");
			priceDetails.setProductSavings("0");
			priceDetails.setProductCurrentPrice(newCurrentPrice);
			priceDetails.setProductRegularPrice(newCurrentPrice);
			priceDetails.setProductOnSale(false);

		} else {
			String newCurrentPrice = priceDetails.getProductCurrentPrice().replaceAll("[$,]", "");
			String[] regularPriceArray = priceDetails.getProductRegularPrice().split(" ");
			String[] savingsArray = priceDetails.getProductSavings().split(" ");
			String newRegularPrice = regularPriceArray[1].replaceAll("[$,]", "");
			String newSavings = savingsArray[1].replace("$", "");
			
			priceDetails.setProductCurrentPrice(newCurrentPrice);
			priceDetails.setProductSavings(newSavings);
			priceDetails.setProductRegularPrice(newRegularPrice);
			priceDetails.setProductOnSale(true);
		}
		
		return priceDetails;
	}
	
	@Override
	public void checkLowestPrice( PriceDetails priceDetails) {
		if(priceDetails.getProductPreviousLowestPrice() == null) {
			priceDetails.setProductLowestPrice(priceDetails.getProductCurrentPrice());
			priceDetails.setProductPreviousLowestPrice("No previous lowest price");
		} else {
			BigDecimal cPrice = new BigDecimal(priceDetails.getProductCurrentPrice());
			BigDecimal rPrice = new BigDecimal(priceDetails.getProductRegularPrice());
			BigDecimal lPrice = new BigDecimal(priceDetails.getProductLowestPrice());
			BigDecimal diff = rPrice.subtract(cPrice);
			
			// Returns: -1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
			if(diff.compareTo(lPrice)  == -1 ) {
				priceDetails.setProductPreviousLowestPrice(priceDetails.getProductLowestPrice());
				priceDetails.setProductLowestPrice(priceDetails.getProductCurrentPrice());
			}	
		}
	}
	
	@Override
	public Product parseNameAndSkuFromUrl(String productUrl) {
		Product product = new Product();
		product.setProductUrl(productUrl);
		int lengthOfSite = "site/".length();
		int lastIndexOfSite = productUrl.indexOf("site/");
		int lastIndexOfForwardSlash = productUrl.lastIndexOf("/");
		int dot = productUrl.lastIndexOf(".");
		
		product.setProductName(productUrl.substring(lastIndexOfSite +lengthOfSite , lastIndexOfForwardSlash));
		product.setProductSKU(productUrl.substring(lastIndexOfForwardSlash + 1, dot));	

		return product;
	}

}
