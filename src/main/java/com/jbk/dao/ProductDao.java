package com.jbk.dao;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.entity.ProductEntity;
import com.jbk.model.ProductModel;

public interface ProductDao {
	
	public boolean addProduct(ProductEntity productEntity) ;

	public ProductEntity getProductById(long productId);

	public boolean deleteProductById(long productId);

	public boolean updateProduct(ProductEntity productEntity);

	public double getMaxProductPrice();
	
	 public List<ProductEntity> maxPriceProduct();
	 
	public List<ProductEntity> sortingByproperty(String oderType ,String property);
	
	public List<ProductEntity> getAllProducts();
	
	public List<ProductEntity> getAllProductRange( double lowPrice,  double highPrice);
	
	public ProductEntity getProductByName( String productName);
	
	public String uploadSheet(MultipartFile myfile);
	
	public double countOfTotalProduct();
}
