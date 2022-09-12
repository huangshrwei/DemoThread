package com.thread.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.thread.dto.ProductsDto;
import com.thread.entity.Products;
import com.thread.service.ProductsService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.thread.api.CommonResult;


@Slf4j
@SpringBootTest
@EnableAutoConfiguration
@EnableJpaAuditing
class ProductsServiceImplTest {

    @Autowired
    ProductsService productsService;
	
	List<ProductsDto> productsList = new ArrayList<>();
	
	@BeforeEach
	void setUp() throws Exception {		
		for (int i = 0; i <300000; i++) {
			ProductsDto ProductsDto = new ProductsDto();
			ProductsDto.setProductId(Long.valueOf(i));
			ProductsDto.setCategory("vegetables");
			ProductsDto.setImage("https://res.cloudinary.com/sivadass/image/upload/v1493620046/dummy-products/broccoli.jpg");
			ProductsDto.setName("Brocolli");
			ProductsDto.setCreationBy(1);
			ProductsDto.setPrice(Double.valueOf(120));
			productsList.add(ProductsDto);			
		}
		log.info("productsList: "+productsList.size());
		
	}

	@AfterEach
	void tearDown() throws Exception {
		productsList.clear();
	}
	
	@Test
	void testSave() {
		
		ProductsDto ProductsDto = new ProductsDto();
		ProductsDto.setCategory("vegetables");
		ProductsDto.setImage("https://res.cloudinary.com/sivadass/image/upload/v1493620046/dummy-products/broccoli.jpg");
		ProductsDto.setName("Brocolli");
		ProductsDto.setCreationBy(1);
		ProductsDto.setPrice(Double.valueOf(120));
		productsService.createProducts(ProductsDto);
		
		List<Products> products =  productsService.listAllProducts();
		log.info("List<Products>: "+products.toString());
		
	}	
	

	@Test
	void testMultiSave() throws InterruptedException, ExecutionException {
		
		List<ProductsDto> productsRetry = new ArrayList<>();
		
        long timeStart, timeEnd;
		
        timeStart = System.currentTimeMillis();
		
		for (int i =0; i<productsList.size(); i++) {
			CompletableFuture<CommonResult> resultFuture = productsService.multiSave(productsList.get(i));
			
			CommonResult<?> result = resultFuture.get();
			
			if (result.getCode()==500) {
				productsRetry.add((ProductsDto)result.getData());
			}
			
		}
		
		timeEnd = System.currentTimeMillis();
		
		log.info("Execute Time: "+ (timeEnd-timeStart));
		
		if (productsRetry.size()>0) {
			log.info("productsRetry: "+productsRetry.toString());
		}else {
			List<Products> products =  productsService.listAllProducts();
			log.info("List<Products>: "+products.size());
		}
	}

	@Test
	void testSaveAll(){
		
        long timeStart, timeEnd;
		
        timeStart = System.currentTimeMillis();
		
		for (int i =0; i<productsList.size(); i++) {
			int returnCode = productsService.createProducts(productsList.get(i));
			
		}		
		timeEnd = System.currentTimeMillis();
		
		log.info("Execute Time: "+ (timeEnd-timeStart));
		
		List<Products> products =  productsService.listAllProducts();
		log.info("List<Products>: "+products.size());
		
	}	
	
	@Test	
	void testMultiSaveAll() throws InterruptedException{
		
        long timeStart, timeEnd;
		
        timeStart = System.currentTimeMillis();
		
		for (int i =0; i<productsList.size(); i++) {
			productsService.multiSaveAll(productsList.get(i));			
		}		
		timeEnd = System.currentTimeMillis();
		
		log.info("Execute Time: "+ (timeEnd-timeStart));
		
		List<Products> products =  productsService.listAllProducts();
		log.info("List<Products>: "+products.size());
		
	}	
	
	
}
