package com.example.ss6.service;

import com.example.ss6.entity.Product;
import com.example.ss6.entity.ProductPagination;
import com.example.ss6.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    IProductRepository productRepository;

    public ProductPagination getAllProducts(Pageable pageable, String searchName) {
        Page<Product> productPage;
        if (searchName != null && !searchName.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(searchName,pageable);
        } else {
            productPage = productRepository.findAll(pageable);

        }
        ProductPagination productPagination = new ProductPagination();
        productPagination.setProducts(productPage.getContent());
        productPagination.setTotalPages(productPage.getTotalPages());
        productPagination.setPageSize(productPage.getSize());
        productPagination.setCurrentPage(productPage.getNumber());
        return productPagination;
    }
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setStock(product.getStock());
            return productRepository.save(existingProduct);
        }

        return null;
    }


    public void deleteProduct(Long id) {

            productRepository.deleteById(id);

    }

}
