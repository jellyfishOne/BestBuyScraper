package jsoupscraper.scraper.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PriceDetails {
	private String productRegularPrice;
	private String productSavings;
	private String productCurrentPrice;
	private boolean productOnSale;
	private String productLowestPrice;
	@Builder.Default
	private String productPreviousLowestPrice = null;

}
