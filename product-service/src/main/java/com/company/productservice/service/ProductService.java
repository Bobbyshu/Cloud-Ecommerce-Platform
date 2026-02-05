package com.company.productservice.service;

import com.company.productservice.entity.Product;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    Product updateProduct(Product product);
    void deleteProduct(Long id);
}
