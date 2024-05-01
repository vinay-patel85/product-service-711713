package com.jbk.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.ProductModel;

public interface ProductService {
	
public boolean addProduct(ProductModel productModel) ;
	
	public ProductModel getProductById( long productId);

	public boolean deleteProductById(long productId);

	public boolean updateProduct( ProductModel productModel);

public double getMaxProductPrice();

public List<ProductModel> maxPriceProduct();

public List<ProductModel> sortingByproperty(String oderType, String property);

public List<ProductModel> getAllProducts();

public List<ProductModel> getAllProductRange( double lowPrice,  double highPrice);

public ProductModel getProductByName( String productName);

public Map<String, Object>  uploadSheet(MultipartFile myfile);

public double countOfTotalProduct();

public Product_Supplier_Category getProductByIdWithSC(long productId);

}
