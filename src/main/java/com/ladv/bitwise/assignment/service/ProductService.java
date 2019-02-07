package com.ladv.bitwise.assignment.service;

import com.ladv.bitwise.assignment.domain.Product;
import com.ladv.bitwise.assignment.domain.User;
import com.ladv.bitwise.assignment.repository.ProductRepository;
import com.ladv.bitwise.assignment.repository.UserRepository;
import com.ladv.bitwise.assignment.service.dto.ProductDTO;
import com.ladv.bitwise.assignment.service.mapper.ProductMapper;
import com.ladv.bitwise.assignment.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getAllUsers() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDTO getUserById(Long id) {
        return productMapper
                .toDto(productRepository.findById(id).orElseThrow(() -> new NoSuchElementException()));
    }

    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        return productMapper.toDto(productRepository.save(product));
    }
}
