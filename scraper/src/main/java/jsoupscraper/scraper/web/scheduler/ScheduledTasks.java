package jsoupscraper.scraper.web.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jsoupscraper.scraper.web.model.Product;
import jsoupscraper.scraper.web.repository.ProductRepository;
import jsoupscraper.scraper.web.services.ScraperServices;


@Component
@EnableScheduling
public class ScheduledTasks {
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	ScraperServices scraperService;
	
	
	//https://dzone.com/articles/running-on-time-with-springs-scheduled-tasks
	//https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
	//@Scheduled(cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week] [Year]")
	//@Scheduled(fixedDelay = 10000)
	@Scheduled(cron = "0 5 * * * *")
	public void checkPrice() throws InterruptedException {
		System.out.println("Starting Scheduled Task...");
		List<Product> allProducts = productRepo.findAll();
		
		for(Product product : allProducts) {
			TimeUnit.SECONDS.sleep(1);
			Product updatedProduct = scraperService.getProductPrice(product);
			productRepo.save(updatedProduct);
			System.out.println("*********************");
			System.out.println(updatedProduct.getProductName());
			System.out.println(updatedProduct.getLastUpdate());
		}
		System.out.println("Finished Scheduled Task...");
	}	
}
