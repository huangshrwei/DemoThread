package com.thread.service.impl;

import com.thread.api.CommonResult;
import com.thread.dao.ProductsRepository;
import com.thread.dto.ProductsDto;
import com.thread.entity.Products;
import com.thread.service.ProductsService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class ProductsServiceImpl implements ProductsService {
	
    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public List<Products> listAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    @Transactional
    public int createProducts(ProductsDto productsDto) {
    	Products saveProducts = new Products();
    	saveProducts.setCategory(productsDto.getCategory());
    	saveProducts.setImage(productsDto.getImage());
    	saveProducts.setName(productsDto.getName());
    	saveProducts.setPrice(productsDto.getPrice());
    	int i = 0;
    	try {
    		productsRepository.save(saveProducts);    	
    	}catch (Exception e) {
    		log.info("createProducts: " + e.getMessage());
    		i = -1;
    	}    	 	
        return i;
    }

    @Override
    @Transactional    
    public int updateProducts(Long id, ProductsDto productsDto) {
    	Products saveProducts = new Products();
    	saveProducts.setProductId(id);
    	saveProducts.setCategory(productsDto.getCategory());
    	saveProducts.setImage(productsDto.getImage());
    	saveProducts.setName(productsDto.getName());
    	saveProducts.setPrice(productsDto.getPrice());
    	int i = 0;
    	try {
    		productsRepository.save(saveProducts);    	
    	}catch (Exception e) {
    		log.debug("updateProducts: " + e.getMessage());
    		i = -1;
    	}     	
        return i;
    }

    @Override
    public int deleteProducts(Long id) {
    	int i = 0;
    	try {
    		productsRepository.deleteById(id);
    		i = 1;
    	}catch (Exception e) {
    		log.debug("deleteProducts: " + e.getMessage());
    		i = -1;
    	}
        return i;
    }

    @Override
    public List<Products> listProducts(int pageNum, int pageSize) {
    	Pageable pageable = PageRequest.of(pageNum-1, pageSize);    	 
    	Page<Products> page = productsRepository.findAll(pageable);
    	return page.getContent();
    }

    @Override
    public Products getProducts(Long id) {
    	Optional<Products> optProducts = productsRepository.findById(id);
    	if (!optProducts.isEmpty()) {
    		Products products = optProducts.get();
    		return products;
    	}else {
    		return null;
    	}
         
    }
    
    @SuppressWarnings("rawtypes")
    @Async("asyncTaskExecutor")
    @Override
    @Transactional
    public  CompletableFuture<CommonResult> multiSave(ProductsDto productsDto) throws InterruptedException {
        log.info("Starting with thread :"+ Thread.currentThread().getName());
        Thread.sleep(0);
    	Products saveProducts = new Products();
    	saveProducts.setCategory(productsDto.getCategory());
    	saveProducts.setImage(productsDto.getImage());
    	saveProducts.setName(productsDto.getName());
    	saveProducts.setPrice(productsDto.getPrice());
    	int i = 0;
    	try {
    		productsRepository.save(saveProducts);    	
    	}catch (Exception e) {
    		//log.debug("updateProducts: " + e.getMessage());
    		i = -1;
    	}
        //logger.info("Complete: getStockPrice for company {} with thread {}", stockName, Thread.currentThread().getName());    	
    	//CommonResult commonResult;
        if (i == 0) {
        	return CompletableFuture.completedFuture(CommonResult.success(productsDto));
            //log.debug("updateProduct success:{}", productsDto);
        } else {
        	return CompletableFuture.completedFuture( CommonResult.failed("操作失败"));
            //log.debug("updateProduct failed:{}", productsDto);
        }
        //return CompletableFuture.completedFuture(commonResult);
    }    
    
    
    @Async("asyncTaskExecutor")
    @Override
    @Transactional
    public void multiSaveAll(ProductsDto productsDto) throws InterruptedException {
    	Thread.sleep(100);
    	Products saveProducts = new Products();
    	saveProducts.setCategory(productsDto.getCategory());
    	saveProducts.setImage(productsDto.getImage());
    	saveProducts.setName(productsDto.getName());
    	saveProducts.setPrice(productsDto.getPrice());
    	int i = 0;
    	try {
    		productsRepository.save(saveProducts);    	
    	}catch (Exception e) {
    		log.debug("updateProducts: " + e.getMessage());
    		i = -1;
    	}    	
    }    
    
}
