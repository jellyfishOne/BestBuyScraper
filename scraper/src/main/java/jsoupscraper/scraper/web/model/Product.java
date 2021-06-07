package jsoupscraper.scraper.web.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection ="products")
public class Product {

	@Id
	private String id;
	
	
	private String productUrl;
	
//	@JsonFormat(pattern= "MM-dd-yyyy")
//	private LocalDate createdDate;
//	
	@JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdate;
	
	private String productName;
	private String productSKU;
	
	private String productRegularPrice;
	private String productSavings;
	private String productCurrentPrice;
	
	private boolean productOnSale;
	private String productLowestPrice;
	
	@Builder.Default
	private String productPreviousLowestPrice = null;
	
}
