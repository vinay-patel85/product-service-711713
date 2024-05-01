package com.jbk.service.impl;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbk.dao.ProductDao;
import com.jbk.dao.impl.ProductDaoImpl;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.CustomIllegalArgumentException;
import com.jbk.exception.ResourceAlreadyExistsException;
import com.jbk.exception.ResourceNotExistsException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.CategoryModel;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.SupplierModel;
import com.jbk.model.ProductModel;
import com.jbk.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductDao dao;

	@Autowired
	ModelMapper mapper;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Override
	public boolean addProduct(ProductModel productModel) {
		
		
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		productModel.setProductId(Long.parseLong(timeStamp));
		
		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
		
		boolean isAdded = dao.addProduct(productEntity);
		
		return isAdded;
	}

	@Override
	public ProductModel getProductById(long productId) {
		
		ProductEntity productEntity = dao.getProductById(productId);
		
		if (productEntity!=null) {
			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
			
			return productModel;
		}
		else {
			throw new ResourceNotExistsException("Product Not Exists with Id : "+productId);
		}
	}

	@Override
	public boolean deleteProductById(long productId) {
	    
//	    ProductEntity productEntity = new ProductEntity();
//	    
//	    productEntity.setProductId(productId);
	    
	    
	    return dao.deleteProductById(productId);
	}

	@Override
	public boolean updateProduct(ProductModel productModel) {
		
		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
		
		return dao.updateProduct(productEntity);
	}
	
	@Override
	public List<ProductModel> getAllProducts() {
		
		List<ProductEntity> entityList = dao.getAllProducts();
		
		List<ProductModel> modelList=new ArrayList<>();
		
		if (!entityList.isEmpty()) {
			
			for (ProductEntity productEntity : entityList) {
				
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
			return modelList;
			
		}else {
			throw new ResourceNotExistsException("Product Not Exists");
		}
		
	
	}
	
	@Override
    public List<ProductModel> sortingByproperty(String orderType, String property) {
		
		List<ProductEntity> entityList = dao.sortingByproperty(orderType, property);
		
		List<ProductModel> modelList=new ArrayList<>();
		
		if (!entityList.isEmpty()) {
			
			for (ProductEntity productEntity : entityList) {
				
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
			return modelList;
			
		}else {
			throw new ResourceNotExistsException("Product Not Exists");
		}
        
    }

	
	@Override
	public double getMaxProductPrice() {
		
		double maxPriceProduct = dao.getMaxProductPrice();
		if (maxPriceProduct>0) {
			
			return maxPriceProduct;
		}
		else {
			throw new ResourceNotExistsException("Resource not Exists");
		}
		
	}

	@Override
	public List<ProductModel> maxPriceProduct() {
		List<ProductEntity> entityList = dao.maxPriceProduct();
		
		List<ProductModel> modelList=new ArrayList<>();
		
		for (ProductEntity productEntity : entityList) {
			
			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
			modelList.add(productModel);
		}
		return modelList;
		
	}

	

	@Override
	public ProductModel getProductByName(String productName) {
		
		return mapper.map(dao.getProductByName(productName),ProductModel.class);
		
		
	}

	public List<ProductModel> readExcel(String filePath){
		
		List<ProductModel> list=new ArrayList<>();
		int rowCount=0;
		try {
			
			FileInputStream fis=new FileInputStream(filePath);
			
			Workbook workbook=WorkbookFactory.create(fis);
			
			Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rows = sheet.rowIterator();
			
			// Iterate rows and pony out every row
			
			
			
			while (rows.hasNext()) {
				Row row =  rows.next();
				int rowNum = row.getRowNum();
				if (rowNum ==0) {
					continue;
				}
				// total row count in sheet
				rowCount++;
				
				// for every row will create one productModel object 
				ProductModel productModel=new ProductModel();
				
				String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
				productModel.setProductId(Long.parseLong(timeStamp)+rowNum);
				
				Iterator<Cell> cells = row.cellIterator();
				
				// Iterate every row and point out every cell
				
				while (cells.hasNext()) {
					Cell cell =  cells.next();
					
					int columnIndex = cell.getColumnIndex();
					
					switch (columnIndex ) {
					case 0:
						productModel.setProductName(cell.getStringCellValue());
						break;
						
					case 1:
						double supplierId = cell.getNumericCellValue();
						
						productModel.setSupplierId((long)supplierId);
						break;
						
					case 2:
						double categoryId = cell.getNumericCellValue();
						
						productModel.setCategoryId((long)categoryId);;
						break;
					case 3:
						productModel.setProductQty((int)cell.getNumericCellValue());
						break;
					case 4:
						productModel.setProductPrice(cell.getNumericCellValue());
						break;
					case 5:
						productModel.setDeliveryCharges((int)cell.getNumericCellValue());
						break;
					
					}
					
				}
				
				// Add product into list
				
				list.add(productModel);
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
		
	}
	
	public Map<String, Object> uploadSheet(MultipartFile myfile) {
	    Map<String, Object> response = new HashMap<>();
	    
	    int isAddedCounter = 0;
	    int alreadyExistsCounter = 0;
	    int issueCounter = 0;
	    int totalRows = 0;
	    int totalExcluded = 0;
	    
	    List<Integer> existingRowNumbers = new ArrayList<>();
	    Map<Integer, Map<String, String>> badRecordRowNumber = new HashMap<>();
	    
	    try {
	        String path = "uploaded";
	        String fileName = myfile.getOriginalFilename();
	        FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
	        
	        byte[] data = myfile.getBytes();
	        fos.write(data);
	        
	        List<ProductModel> list = readExcel(path + File.separator + fileName);
	        List<ProductEntity> dbAllProducts = dao.getAllProducts();
	        
	        for (int i = 0; i < list.size(); i++) {
	            ProductModel productModel = list.get(i);
	            boolean alreadyExists = false;
	            boolean isValid = true;
	            Map<String, String> errorMap = new HashMap<>();
	            
	            // Data validation
	            if (!isValidProductName(productModel.getProductName())) {
	                isValid = false;
	                errorMap.put("productName", "Product name must only contain alphabetic characters and spaces.");
	            }
	            if (productModel.getProductQty() <= 0) {
	                isValid = false;
	                errorMap.put("productQty", "Product quantity cannot be negative.");
	            }
	            if (productModel.getProductPrice() <= 0) {
	                isValid = false;
	                errorMap.put("productPrice", "Product price cannot be negative.");
	            }
	            if (productModel.getDeliveryCharges() <= 0) {
	                isValid = false;
	                errorMap.put("deliveryCharges", "Delivery charges cannot be negative.");
	            }
	            
	            
	            if (!isValid) {
	                badRecordRowNumber.put(i + 1, errorMap);
	                continue; // Skip further processing for invalid data
	            }
	            
	            // Proceed with further processing if data is valid
          
	            for (ProductEntity dbProduct : dbAllProducts) {
	                if (isMatch(productModel, dbProduct)) {
	                    alreadyExists = true;
	                    existingRowNumbers.add(i + 1); // Add 1 to convert to 1-based index
	                    break;
	                }
	            }
	            
	            if (!alreadyExists) {
	                ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
	                try {
	                    boolean isAdded = dao.addProduct(productEntity);
	                    if (isAdded) {
	                        isAddedCounter++;
	                    }
	                } catch (SomethingWentWrongException e) {
	                    issueCounter++;
	                }
	            }
	        }
	        
	        totalRows = list.size();
//	        totalExcluded = existingRowNumbers.size() + alreadyExistsCounter;
	        
	        if (!badRecordRowNumber.isEmpty()) {
	            // If bad records are found, increment the totalExcluded count
	            totalExcluded += badRecordRowNumber.size();
	        }
	        
	    } catch (Exception e) {
	        throw new SomethingWentWrongException("Something went wrong during upload sheet");
	    }
	    
	    // Populate response map with the necessary data
	    response.put("Uploaded Records In DB", isAddedCounter);
	    response.put("Total Exists Records In DB", existingRowNumbers.size());
//	    response.put("Total Exists Records In DB", alreadyExistsCounter);
	    response.put("Row Num, Exists Records In DB", existingRowNumbers);
	    response.put("Total Record In Sheet", totalRows);
	    response.put("Total Excluded", totalExcluded);
	    response.put("Bad Record Row Number", badRecordRowNumber);
	    
	    return response;
	}

	private boolean isValidProductName(String productName) {
	    // Regular expression to check if product name meets the specified pattern
	    String pattern = "^(?!\\s|\\d)(?=.*[a-zA-Z]).*$";
	    return productName.matches(pattern);
	}


	private boolean isMatch(ProductModel excelProduct, ProductEntity dbProduct) {
	    // Compare relevant fields to check if they match
	    return excelProduct.getProductName().equals(dbProduct.getProductName());
	          
	}


	
	@Override
	public List<ProductModel> getAllProductRange(double lowPrice, double highPrice) {
	    List<ProductEntity> list = dao.getAllProductRange(lowPrice, highPrice);
	    List<ProductModel> modelList = new ArrayList<>();

	    if (list.isEmpty()) {
	        throw new ResourceNotExistsException("No products found within the specified price range");
	    }

	    for (ProductEntity productEntity : list) {
	        ProductModel productModel = mapper.map(productEntity, ProductModel.class);
	        modelList.add(productModel);
	    }
	    return modelList;
	}
	
	@Override
	public double countOfTotalProduct() {
		
		return dao.countOfTotalProduct();
	}

	@Override
	public Product_Supplier_Category getProductByIdWithSC(long productId) {
		
		Product_Supplier_Category psc=null;
		ProductModel productModel=getProductById(productId);
		
		if (productModel !=null) {
			psc.setProductModel(productModel);
			
			try {
				SupplierModel supplierModel=restTemplate.getForObject("http://localhost:8092/supplier/get-supplier-by-id/"+productModel.getSupplierId(), SupplierModel.class);
				
				if (supplierModel.getSupplierId()<=0) {
					psc.setSupplierModel(null);
				}
				else {
					psc.setSupplierModel(supplierModel);
				}
				
				
			} catch (ResourceAccessException e) {
				psc.setSupplierModel(null);
			}
			try {
				CategoryModel categoryModel=restTemplate.getForObject("http://localhost:8093/category/get-category-by-id/"+productModel.getCategoryId(), CategoryModel.class);
				
				if (categoryModel.getCategoryId()<=0) {
					psc.setCategoryModel(null);
				}
				else {
					psc.setCategoryModel(categoryModel);
				}
				
				
			} catch (ResourceAccessException e) {
				psc.setCategoryModel(null);
			}
			
			
		}else {
			
		}
		
		return psc;
	}
	
	

}
