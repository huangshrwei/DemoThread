package com.thread.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Id;

import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
@Data
public class ProductsDto {


    private Long productId;

    private String name;

    private Double price;

    private String image;

    private String category;

    private Date creationDate;   

    private Integer creationBy;
    
    private Date updatedDate;     
    
    
}