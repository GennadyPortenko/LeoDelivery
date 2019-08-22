package com.cmdelivery.service;

import com.cmdelivery.model.Product;
import com.cmdelivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductService {
    private final ProductRepository productRepository;

    public Product registerNewProduct(Product product) {
        return productRepository.save(product);
    }
}
