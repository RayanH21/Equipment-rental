package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.dto.CategoryDto;
import be.rayanhaddou.equipmentrental.dto.ProductDto;
import be.rayanhaddou.equipmentrental.model.Category;
import be.rayanhaddou.equipmentrental.model.Product;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public CategoryDto toCategoryDto(Category c) {
        return new CategoryDto(c.getId(), c.getName());
    }

    public ProductDto toProductDto(Product p) {
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getQuantity(),
                p.isActive(),
                toCategoryDto(p.getCategory())
        );
    }
}