
// Problem:
//
// A supermarket sells 3 products listed below: 
//
// Product A = $20 
// Product B = $50 (or 5 for the price of 3) 
// Product C = $30 
//
// Implement the code for a checkout register that calculates the price of a given sequence of items. 
// The input is a product list as a String, e.g "ABBACBBAB" : for which the output should be the integer 240. 
//
// Please consider testability, documentation, and other good coding practices in your solution. 
// As an additional challenge, consider how new pricing rules might be provided programmatically. 


/**
 * Solution:
 * 
 * @author larsnes
 *
 * Implementation of Supermarket class that supports the above requirements.  In addition to the basic
 * requirements, I have implemented a simple API that supports adding and removing new products and
 * pricing rules programmatically.
 * 
 * A simple exception handling mechanism is used to catch unexpected conditions such as missing products
 * and invalid product names/pricing rules.
 * 
 * I have included a simple test program to exercise the code, please see src/larstest/Tester.java for
 * details.  In a real product, a more formal test suite could be implemented using a unit test framework
 * such as TestNG.  I think that is beyond the scope of this coding exercise
 * 
 */

package larstest;

import java.util.Map;
import java.util.HashMap;

public class Supermarket {

    /**
     * Private helper class to represent a product entry in the product "database" 
     */
	private static class Product {
		@SuppressWarnings("unused")
		final String id;		// Product id, e.g., "A", must be single character, not currently used
		final int unitPrice;	// Regular unit price for this item
		final boolean hasBulk;	// Indicates special bulk pricing
		final int bulkCount;	// Minimum quantity to qualify for bulk pricing
		final int bulkRule;		// Pricing to apply for each qualifying bulk group,
								// e.g., bulkCount = 5 and bulkRule = 3 would qualify
								// each group of 5 identical items to be priced as 3
								// (5 for the price of 3)

		/**
	     * Constructor - use this for items with no special bulk pricing
	     * 
	     * @param id - Product id, e.g., "A", must be single character
	     * @param unitPrice - Regular unit price for this item
	     */
		Product(String id,
				int unitPrice) {
			this.id = id;
			this.unitPrice = unitPrice;
			this.bulkCount = 0;
			this.bulkRule = 0;
			hasBulk = false;
		}

		/**
	     * Constructor - use this for items with special bulk pricing
	     * 
	     * @param id - Product id, e.g., "A", must be single character
	     * @param unitPrice - Regular unit price for this item
	     * @param bulkCount - Minimum quantity to qualify for bulk pricing
	     * @param bulkRule - Pricing to apply for each qualifying bulk group (see above for details)
	     */
		Product(String id,
				int unitPrice,
				int bulkCount,
				int bulkRule) {
			this.id = id;
			this.unitPrice = unitPrice;
			this.bulkCount = bulkCount;
			this.bulkRule = bulkRule;
			hasBulk = true;
		}
	}
	
	// Map of available products in the supermarket.
	private Map<String, Product> products = new HashMap<String, Product>();

    /**
     * Computes the price of a collection of equal items 
     * 
     * @param item - String/int combo holding product name and count of items of this type
     * @return price for the collection as an int
     * @throws Exception if product could not be found in "database"
     */
	private int computeProductPrice(Map.Entry<String, Integer> item) 
				throws Exception {
		
		Product p = products.get(item.getKey());
	
		if (p != null) {
			if (p.hasBulk) {
				// Bulk items have special pricing rules where bulkCount is
				// the unit count qualifying for bulk pricing and bulkRule is the
				// bulk pricing in unitPrice per bulk unit.  bulkCount
				// and bulkRule must be greater than 0.
				if (p.bulkCount <= 0) {
					throw new Exception("Product " + item.getKey() 
							+ ", invalid bulkCount: " + p.bulkCount);
				}
				if (p.bulkRule <= 0) {
					throw new Exception("Product " + item.getKey() 
							+ ", invalid bulkRule: " + p.bulkRule);
				}
				// number of bulk groups
				int bulk = item.getValue() / p.bulkCount; 
				// remainder items not qualifying for bulk pricing
				int rem =  item.getValue() % p.bulkCount;
				return (bulk * p.bulkRule + rem) * p.unitPrice;
			}
			else {
				return item.getValue() * p.unitPrice;
			}
		}
		else {
			throw new Exception("Product " + item.getKey() 
					+ " not found!");
		}
	}

    /**
     * Computes the price of a collection of items 
     * 
     * @param String items - String of products, e.g., "ABBACBBAB"
     * @return price as an int for all the items
     */
	public int checkout(String items) 
			throws Exception {
		
		Map<String, Integer> content = new HashMap<String, Integer>();
		int total = 0;
		
		// scan for content, this will produce a list where the
		// key for each entry is the product name, and the value
		// for each entry is the count of that product
		for (int i = 0; i < items.length(); i++) {
			String item = String.valueOf(items.charAt(i));
			if (content.containsKey(item)) {
				content.put(item, content.get(item) + 1);
			}
			else {
				content.put(item, 1);
			}
		}
		
		// compute total value of all the products
		for (Map.Entry<String, Integer> item  : content.entrySet()) {
			total += computeProductPrice(item);
		}
		
		return total;
	}

    /**
     * Add a product to product "database", no special bulk pricing
     * If a product with this id already exists in the product "database", 
     * then this will replace the existing product
     * 
     * @param id - Product id, e.g., "A", must be single character
     * @param unitPrice - Regular unit price for this item
     * @throws Exception if product id is invalid
     */
	public void addProduct(String id, int unitPrice)
		throws Exception {
		
		if (id.trim().length() != 1) {
			throw new Exception("Invalid product id, must be single char: " + id);
		}
		products.put(id, new Product(id.trim(), unitPrice));
	}

    /**
     * Add a product to product "database", with special bulk pricing
     * If a product with this id already exists in the product "database", 
     * then this will replace the existing product
     * 
     * @param id - Product id, e.g., "A", must be single character
     * @param unitPrice - Regular unit price for this item
     * @param bulkCount - Minimum quantity to qualify for bulk pricing
     * @param bulkRule - Pricing to apply for each qualifying bulk group (see above for details)
     * @throws Exception if product spec is invalid
     */
	public void addProduct(String id, int unitPrice, int bulkCount, int bulkRule) 
			throws Exception {
		
		if (id.trim().length() != 1) {
			throw new Exception("Invalid product id, must be single char: " + id);
		}
		if (bulkCount <= 0) {
			throw new Exception("Invalid pricing rule, bulkCount must be greater than 0: " + bulkCount);
		}
		if (bulkRule <= 0) {
			throw new Exception("Invalid pricing rule, bulkRule must be greater than 0: " + bulkRule);
		}
		products.put(id, new Product(id.trim(), unitPrice, bulkCount, bulkRule));
	}

    /**
     * Remove a product from product "database"
     * 
     * @param id - Product id, e.g., "A", must be single character
     * @return Product object that was removed, or null if not found
     */
	public Product removeProduct(String id) {
		return products.remove(id.trim());
	}
}