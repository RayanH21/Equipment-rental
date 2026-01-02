package be.rayanhaddou.equipmentrental.controller;

import be.rayanhaddou.equipmentrental.dto.ProductDto;
import be.rayanhaddou.equipmentrental.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Voorbeeld:
    // GET /api/products
    // GET /api/products?category=Kabels
    @GetMapping
    public List<ProductDto> getProducts(@RequestParam(required = false) String category) {
        return productService.getCatalog(category);
    }
}