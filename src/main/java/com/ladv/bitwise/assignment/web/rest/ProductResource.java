package com.ladv.bitwise.assignment.web.rest;

import com.ladv.bitwise.assignment.service.ProductService;
import com.ladv.bitwise.assignment.service.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductResource {
    private Logger log = LoggerFactory.getLogger(ProductResource.class);

    private ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) throws URISyntaxException {
        log.debug("REST request to create product : {}", product);
        ProductDTO createdProduct = productService.save(product);
        return ResponseEntity.created(new URI("/api/products/"+createdProduct.getId()))
                .body(createdProduct);
    }
    
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.debug("REST request to get all products");
        return ResponseEntity.ok(productService.getAllUsers());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        log.debug("REST request to get product by id : {}", id);
        return ResponseEntity.ok(productService.getUserById(id));
    }
    
    
}
