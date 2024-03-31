package com.easyskillup.productservice.service;

import com.easyskillup.productservice.dto.ProductRequest;
import com.easyskillup.productservice.dto.ProductResponse;
import com.easyskillup.productservice.model.Product;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse mapToProductResponse(Product product);
}
