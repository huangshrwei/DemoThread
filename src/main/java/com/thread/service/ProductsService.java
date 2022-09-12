package com.thread.service;

import com.thread.api.CommonResult;
import com.thread.dto.ProductsDto;
import com.thread.entity.Products; 

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductsService {
    List<Products> listAllProducts();

    int createProducts(ProductsDto productsDto);

    int updateProducts(Long id, ProductsDto productsDto);

    int deleteProducts(Long id);

    List<Products> listProducts(int pageNum, int pageSize);

    Products getProducts(Long id);
    
    CompletableFuture<CommonResult> multiSave(ProductsDto productsDto) throws InterruptedException;
    
    void multiSaveAll(ProductsDto productsDto) throws InterruptedException;
    
}
