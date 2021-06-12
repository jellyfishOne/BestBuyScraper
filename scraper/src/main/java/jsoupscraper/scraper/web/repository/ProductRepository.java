package jsoupscraper.scraper.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import jsoupscraper.scraper.web.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{
	
	@Query("{'priceDetails.productOnSale': ?0}")
	List<Product> findByProductOnSale(final boolean productOnSale);
	
	Optional<Product> findById(String id);
	Product findByProductSKU(String sku);
}
