package be.rayanhaddou.equipmentrental.controller;

import be.rayanhaddou.equipmentrental.dto.AddToCartRequest;
import be.rayanhaddou.equipmentrental.model.CartItem;
import be.rayanhaddou.equipmentrental.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public void addToCart(@Valid @RequestBody AddToCartRequest request,
                          HttpSession session) {
        cartService.addToCart(request, session);
    }

    @GetMapping
    public List<CartItem> viewCart(HttpSession session) {
        return cartService.viewCart(session);
    }

    @PostMapping("/checkout")
    public void checkout(HttpSession session) {
        cartService.checkout(session);
    }
}