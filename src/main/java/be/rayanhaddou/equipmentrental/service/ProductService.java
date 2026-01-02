package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.dto.ProductDto;
import be.rayanhaddou.equipmentrental.model.Product;
import be.rayanhaddou.equipmentrental.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DtoMapper dtoMapper;

    public ProductService(ProductRepository productRepository, DtoMapper dtoMapper) {
        this.productRepository = productRepository;
        this.dtoMapper = dtoMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getCatalog(String category) {
        List<Product> products;

        if (category == null || category.isBlank()) {
            products = productRepository.findByActiveTrue();
        } else {
            products = productRepository.findByActiveTrueAndCategory_NameIgnoreCase(category.trim());
        }

        return products.stream()
                .map(dtoMapper::toProductDto)
                .collect(toList());
    }
}