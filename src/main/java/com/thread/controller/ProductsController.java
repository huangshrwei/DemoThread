package com.thread.controller;

import com.thread.api.CommonResult;
import com.thread.entity.Products;
import com.thread.service.ProductsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.thread.dao.ProductsRepository;
import com.thread.dto.ProductsDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;


@RestController
@Api(tags = "ProductsController")
@Tag(name = "ProductsController", description = "商品管理")
@RequestMapping("/products")
@Slf4j
public class ProductsController {
    
    @Autowired
    private ProductsService productsService;    

    
	List<ProductsDto> productsList = new ArrayList<>();

    @PostConstruct
	void setUp() throws Exception {		
		for (int i = 0; i <30000; i++) {
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
    
    
    @ApiOperation(value = "取得商品", notes = "列出所有商品")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/listAll")
    public CommonResult<List<Products>> getAll() {
    	List<Products> productsList = productsService.listAllProducts();
        return CommonResult.success(productsList);
    }  	
	
    @ApiOperation("添加商品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createProducts(@RequestBody ProductsDto productsDto) {
        CommonResult commonResult;
        int count = productsService.createProducts(productsDto);
        if (count == 1) {
            commonResult = CommonResult.success(productsDto);
            log.debug("createProduct success:{}", productsDto);
        } else {
            commonResult = CommonResult.failed("操作失败");
            log.debug("createProduct failed:{}", productsDto);
        }
        return commonResult;
    }

    @ApiOperation("更新指定id品牌信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateProducts(@PathVariable("id") Long id, @RequestBody ProductsDto productsDto, BindingResult result) {
        CommonResult commonResult;
        int count = productsService.updateProducts(id, productsDto);
        if (count == 1) {
            commonResult = CommonResult.success(productsDto);
            log.debug("updateProduct success:{}", productsDto);
        } else {
            commonResult = CommonResult.failed("操作失败");
            log.debug("updateProduct failed:{}", productsDto);
        }
        return commonResult;
    }

    @ApiOperation("删除指定id的品牌")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteProducts(@PathVariable("id") Long id) {
        int count = productsService.deleteProducts(id);
        if (count == 1) {
            log.debug("deleteProduct success :id={}", id);
            return CommonResult.success(null);
        } else {
            log.debug("deleteProduct failed :id={}", id);
            return CommonResult.failed("操作失败");
        }
    }

    @ApiOperation("分页查询商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Products>> listProducts(@RequestParam(value = "pageNum", defaultValue = "1")
                                                        @ApiParam("页码") Integer pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "3")
                                                        @ApiParam("每页数量") Integer pageSize) {
        List<Products> productsList = productsService.listProducts(pageNum, pageSize);
        return CommonResult.success(productsList);
    }

    @ApiOperation("取得指定id商品資料")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Products> products(@PathVariable("id") Long id) {
        return CommonResult.success(productsService.getProducts(id));
    }    
    
    
    @ApiOperation("添加商品ByCompletableFuture")
    @RequestMapping(value = "/mSave", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult multiSaveProducts() throws InterruptedException, ExecutionException {
		
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
		}		
		
		List<Products> products =  productsService.listAllProducts();
		CommonResult commonResult = CommonResult.success(null);
		
		log.info("List<Products>: "+products.size());
		return commonResult;
    }
    
    @ApiOperation("添加商品ByThread")
    @RequestMapping(value = "/mSaveAll", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult multiSaveAllProducts() throws InterruptedException, ExecutionException {
		
		List<ProductsDto> productsRetry = new ArrayList<>();
		
        long timeStart, timeEnd;
        
        timeStart = System.currentTimeMillis();        
		
		for (int i =0; i<productsList.size(); i++) {
			productsService.multiSaveAll(productsList.get(i));			
		}		
		timeEnd = System.currentTimeMillis();
		
		log.info("Execute Time: "+ (timeEnd-timeStart));
		
		List<Products> products =  productsService.listAllProducts();
		
		CommonResult commonResult = CommonResult.success(null);
		
		log.info("List<Products>: "+products.size());
		return commonResult;
    }  
    
    
}
