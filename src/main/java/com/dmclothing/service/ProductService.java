package com.dmclothing.service;

import com.dmclothing.model.Product;
import com.dmclothing.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        Product product = getProductById(productId);

        product.setProductName(updatedProduct.getProductName());
        product.setCategory(updatedProduct.getCategory());
        product.setSize(updatedProduct.getSize());
        product.setColors(updatedProduct.getColors());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setImage(updatedProduct.getImage());

        return productRepository.save(product);
    }

    public String deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);

        return "Product deleted successfully";
    }
    public List<Product> getLowStockProducts() {
    return productRepository.findByStockLessThanEqual(5);
}
}