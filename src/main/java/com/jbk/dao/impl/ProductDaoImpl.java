package com.jbk.dao.impl;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.RollbackException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.dao.ProductDao;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistsException;
import com.jbk.exception.ResourceNotExistsException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.ProductModel;

@Repository
public class ProductDaoImpl implements ProductDao {
	

	@Autowired
	private SessionFactory factory;
	
	@Override
	public boolean addProduct(ProductEntity productEntity) {
	    boolean isAdded = false;
	    try {
	        Session session = factory.openSession();
	        Transaction t = session.beginTransaction();

	        // Check if product with the given ID already exists
	        ProductEntity dbEntityById = getProductById(productEntity.getProductId());
	        if (dbEntityById != null) {
	            throw new ResourceAlreadyExistsException("Product is Already Exists with Id: " + productEntity.getProductId());
	        }

	        // Check if product with the given name already exists
	        Query queryByName = session.createQuery("FROM ProductEntity WHERE productName = :name");
	        queryByName.setParameter("name", productEntity.getProductName());
	        ProductEntity dbEntityByName = (ProductEntity) queryByName.uniqueResult();
	        if (dbEntityByName != null) {
	            throw new ResourceAlreadyExistsException("Product is Already Exists with Name: " + productEntity.getProductName());
	        }
	        
	        // If product doesn't exist, save it
	        session.save(productEntity);
	        t.commit();
	        isAdded = true;

	    } 
	    catch (RollbackException e) {
	        e.printStackTrace();
	        throw new SomethingWentWrongException("Something Went Wrong during add product");
	    }
	    return isAdded;
	}




	@Override
	public ProductEntity getProductById(long productId) {
		
		ProductEntity productEntity=null;
		try {
			
			Session session=factory.openSession();
			
			 productEntity = session.get(ProductEntity.class, productId);
			
		} catch (HibernateException e) {
			throw new SomethingWentWrongException("Connection failure ");
		}
		return productEntity;
	}

	@Override
	public boolean deleteProductById(long productId) {
	    boolean isDelete = false;
	    Session session = null;
	    Transaction transaction = null;
	    try {
	        session = factory.openSession();
	        transaction = session.beginTransaction();
	        
//	        Hibernate no longer automatically detects changes made to detached entities. If you modify a detached entity,
//	        you need to explicitly reattach it to a session or merge it with a session to persist those changes back to the database.
	        
//	        ProductEntity dbEntity = getProductById(productId);
	        
	        ProductEntity dbEntity = session.get(ProductEntity.class, productId);

	        
	        System.out.println("dbEntity="+dbEntity);
	        if (dbEntity != null) {
	            session.delete(dbEntity);
	            transaction.commit();
	            isDelete = true;
	        } else {
	            
	            throw new ResourceNotExistsException("Product not found with ID: " + productId);
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	        throw new SomethingWentWrongException("Something Went Wrong during Delete product");
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    return isDelete;
	}

	@Override
	public boolean updateProduct(ProductEntity productEntity) {
		
		boolean isUpdate=false;
		Session session=null;
		try {
			session=factory.openSession();
			Transaction t=session.beginTransaction();
			
			ProductEntity dbProduct= getProductById(productEntity.getProductId());
			
			if (dbProduct != null) {
	            session.update(productEntity);
	            t.commit();
	            isUpdate = true;
	        } else {
	            
	            throw new ResourceNotExistsException("Product not found with ID: " + productEntity.getProductId());
	        }
			
			
		}catch (ResourceNotExistsException e) {
		
			 throw new ResourceNotExistsException("Product not found with ID: " + productEntity.getProductId());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something Went Wrong during Update product");
		}finally {
			
			if (session!=null) {
				session.close();
			}
		}
		
	    return isUpdate;
		
	}

	@Override
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> list=null;
		
		try {
			Session session=factory.openSession();
			
			Criteria criteria = session.createCriteria(ProductEntity.class);
			
			 list = criteria.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something Went wrong during Retrive all Products");
		}
		return list;
	}
	
	
	@Override
	public List<ProductEntity> sortingByproperty(String orderType, String property) {
		List<ProductEntity> list=null;
		
		try {
			Session session=factory.openSession();
			
			Criteria criteria = session.createCriteria(ProductEntity.class);
			
			if (orderType.equalsIgnoreCase("desc")) {
				criteria.addOrder(Order.desc(property));
				 
				
			}else {
				criteria.addOrder(Order.asc(property));
			}
			
			list = criteria.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something Went wrong during Retrive all Products");
		}
		return list;
	}
	
	
	@Override
	public double getMaxProductPrice() {
		double maxPrice=0;
		try {
			Session session=factory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);
			
			Projection projection=Projections.max("productPrice");
			
			 Criteria setProjection = criteria.setProjection(projection);
			 
			 List list = criteria.list();
			 
			 if (!list.isEmpty()) {
				 maxPrice =(double) list.get(0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something Went wrong during get max Price Product"); 
		}
		
		return maxPrice;
		
	}




	@Override
	public List<ProductEntity> maxPriceProduct() {
		
		List<ProductEntity> list=null;
		
		double maxPriceProduct = getMaxProductPrice();
		if (maxPriceProduct>0) {
			Session session=factory.openSession();
			
			Criteria criteria = session.createCriteria(ProductEntity.class);
			
			criteria.add(Restrictions.eqOrIsNull("productPrice", maxPriceProduct));
			
			 list = criteria.list();
			
		}
		else {
			throw new ResourceNotExistsException("Resource not Exists");
		}
		return list;
	}

	@Override
	public ProductEntity getProductByName(String productName) {
		List<ProductEntity> list=null;
		ProductEntity productEntity=null;
		
		try {
			Session session=factory.openSession();
			
			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE productName= :name");
			
			query.setParameter("name", productName);
			
			 list = query.list();
			
			if (!list.isEmpty()) {
				 productEntity = list.get(0);
				
			} else {
				throw new ResourceNotExistsException("product Not Exists");
			}
		}catch (ResourceNotExistsException e) {
			throw new ResourceNotExistsException("product Not Exists");
		} 
		catch (Exception e) {
			throw new SomethingWentWrongException("Something Went Wrong during get product");
		}
		return productEntity;
	}




	@Override
	public String uploadSheet(MultipartFile myfile) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ProductEntity> getAllProductRange(double lowPrice, double highPrice) {
		List<ProductEntity> list=null;
		
		try {
			
			Session session=factory.openSession();
			
			Criteria criteria = session.createCriteria(ProductEntity.class);
			criteria.add(Restrictions.between("productPrice", lowPrice, highPrice));
			 list = criteria.list();
			 
		} catch (Exception e) {
			throw new SomethingWentWrongException("Something Went Wrong During Get Product between Price");
		}
		return list;
	}

	@Override
	public double countOfTotalProduct() {
	    try  {
	        Session session=factory.openSession();
	        Criteria criteria = session.createCriteria(ProductEntity.class);
	        
	        criteria.setProjection(Projections.rowCount());
	        Double result = (Double) criteria.uniqueResult();
	        return result != null ? result : 0.0;
	    } catch (HibernateException e) {
	        // Handle exception appropriately, log it, or rethrow it as needed
	        throw new SomethingWentWrongException("Something went wrong during counting total products");
	    }
	}



	

	
		
	
	

}
