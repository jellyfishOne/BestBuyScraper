package jsoupscraper.scraper.web.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.model.PriceDetails;
import jsoupscraper.scraper.web.repository.ProductRepository;
import jsoupscraper.scraper.web.services.ScraperServices;


@Component
@EnableScheduling
public class ScheduledTasks {
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	ScraperServices scraperService;
	
	@Scheduled(cron = "0 5 * * * *")
	public void checkPrice() throws InterruptedException {
		System.out.println("Starting Scheduled Task...");
		List<Product> allProducts = productRepo.findAll();
		
		for(Product product : allProducts) {
			TimeUnit.SECONDS.sleep(1);
			PriceDetails priceDetails = scraperService.scrapeProductPriceDetails(product);
			scraperService.checkLowestPrice(priceDetails);
			product.setPriceDetails(priceDetails);
			productRepo.save(product);
			
			System.out.println("*********************");
			System.out.println(product.getProductName());
			System.out.println(product.getLastUpdate());
		}
		System.out.println("Finished Scheduled Task...");
	}	
}
