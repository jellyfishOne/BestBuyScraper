package jsoupscraper.scraper.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jsoupscraper.scraper.web.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{
	List<Product> findByProductOnSaleIsTrue();
	Optional<Product> findById(String id);
	Product findByProductSKU(String sku);
}
