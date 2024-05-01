package com.jbk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.entity.ProductEntity;
import com.jbk.exception.CustomIllegalArgumentException;
import com.jbk.exception.ProductNotExistsException;
import com.jbk.exception.ResourceAlreadyExistsException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.ProductModel;
import com.jbk.model.ProductModel;
import com.jbk.service.ProductService;
import com.jbk.service.impl.ProductServiceImpl;

@RestController
@RequestMapping("product")
public class ProductController {
	
	@Autowired
	ProductService service;
	
	@PostMapping("add-product")
	public ResponseEntity<String> addProduct(@RequestBody @Valid ProductModel productModel){
		
		boolean isAdded = service.addProduct(productModel);
		
			return ResponseEntity.ok("Product Added !!");
	}
	
	
	@GetMapping("get-product-by-id/{productId}")
	public ResponseEntity<ProductModel> getProductById(@PathVariable long productId){
		
		ProductModel productModel = service.getProductById(productId);
		
		return ResponseEntity.ok(productModel);
		
	}
	@GetMapping("get-product-with-sc/{productId}")
	public ResponseEntity<Product_Supplier_Category> getProductByIdWithSC(@PathVariable long productId){
		
		Product_Supplier_Category psc = service.getProductByIdWithSC(productId);
		
		return ResponseEntity.ok(psc);
		
	}
	
	@DeleteMapping("delete-product-by-id/{productId}")
	public ResponseEntity<String> deleteProductById(@PathVariable long productId) {
	    service.deleteProductById(productId);
	    return ResponseEntity.ok("Product Deleted!!");
	}
	
	
	@PutMapping("update-product")
	public ResponseEntity<String> updateProduct(@RequestBody @Valid ProductModel productModel){
		
		boolean isAdded = service.updateProduct(productModel);
		
			return ResponseEntity.ok("Product Updated !!");
	}
	
	@GetMapping("get-all-products")
	 public ResponseEntity<List<ProductModel>> getAllProducts(){
		 
		 List<ProductModel> allProducts = service.getAllProducts();
		 
		return ResponseEntity.ok(allProducts);
		 
	 }
	
	 @GetMapping("/sorting")
	    public ResponseEntity<List<ProductModel>> sortingByProperty(@RequestParam String orderType, @RequestParam String property) {
		 
		 List<ProductModel> allProducts = service.sortingByproperty(orderType, property);
		 
			return ResponseEntity.ok(allProducts);
	       
	    }
	 
	 
	 @GetMapping("/max-product-price")
	 public ResponseEntity<Double> getMaxProductPrice() {
			
		 double maxPriceProduct = service.getMaxProductPrice();
			
			 return ResponseEntity.ok(maxPriceProduct);
			
		}
	 
	 @GetMapping("/max-price-product")
	    public ResponseEntity<List<ProductModel>> maxPriceProduct() {
		 
		 List<ProductModel> allProducts = service.maxPriceProduct();
		 
			return ResponseEntity.ok(allProducts);
	       
	    }
	 
	 @GetMapping("get-product-by-name/{productName}")
	 public ResponseEntity<ProductModel> getProductByName(@PathVariable String productName){
		 ProductModel productByName = service.getProductByName(productName);
		 
		return ResponseEntity.ok(productByName);
		 
	 }
	 
	 @GetMapping("price-range/{lowPrice}/{highPrice}")
	 public ResponseEntity<List<ProductModel>> getAllProductRange(@PathVariable double lowPrice, @PathVariable double highPrice){
		 
		 List<ProductModel> allProductRange = service.getAllProductRange(lowPrice, highPrice);
		 
		return ResponseEntity.ok(allProductRange);
				
		 
	 }
	 
	 @PostMapping("/upload-sheet")
	 
	 public ResponseEntity<Map<String, Object>> uploadSheet(@RequestParam MultipartFile myfile) {
		 System.out.println(myfile.getOriginalFilename());
	     if (myfile.isEmpty()) {
	         Map<String, Object> errorResponse = new HashMap<>();
	         errorResponse.put("error", "Please select a file to upload.");
	         return ResponseEntity.badRequest().body(errorResponse);
	     }

	     try {
	         Map<String, Object> response = service.uploadSheet(myfile);
	         return ResponseEntity.ok(response);
	     } catch (SomethingWentWrongException e) {
	         Map<String, Object> errorResponse = new HashMap<>();
	         errorResponse.put("error", "Error occurred while uploading the file.");
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	     }
	 }
	 
	 @GetMapping("count-of-total-product")
	 public ResponseEntity<Double> countOfTotalProduct(){
	     double count = service.countOfTotalProduct();
	     return ResponseEntity.ok(count);
	 }
	 
}