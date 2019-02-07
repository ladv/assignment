package com.ladv.bitwise.assignment.service.mapper;

import com.ladv.bitwise.assignment.domain.Product;
import com.ladv.bitwise.assignment.service.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "user.id", source = "userId")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "userId", source = "user.id")
    ProductDTO toDto(Product product);
}
