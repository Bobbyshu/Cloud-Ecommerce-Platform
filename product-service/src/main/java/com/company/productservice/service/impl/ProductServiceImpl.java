package com.company.productservice.service.impl;

import com.company.productservice.dao.ProductRepository;
import com.company.productservice.entity.Product;
import com.company.productservice.exception.InsufficientStockException;
import com.company.productservice.exception.ProductNotFoundException;
import com.company.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;
    @Override
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void reduceStock(Long id, Integer quantity) {
        Product product = productRepo.findByIdWithLock(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (product.getStock() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product id: " + id);
        }

        product.setStock(product.getStock() - quantity);
        productRepo.save(product);
    }
}
