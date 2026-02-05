package com.company.orderservice.client;

import com.company.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);

    @PutMapping("/products/{id}/reduceStock")
    void reduceStock(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
}