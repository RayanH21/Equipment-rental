package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.dto.AddToCartRequest;
import be.rayanhaddou.equipmentrental.model.CartItem;
import be.rayanhaddou.equipmentrental.model.Product;
import be.rayanhaddou.equipmentrental.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_KEY = "CART";

    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        Object cart = session.getAttribute(CART_KEY);
        if (cart == null) {
            List<CartItem> newCart = new ArrayList<>();
            session.setAttribute(CART_KEY, newCart);
            return newCart;
        }
        return (List<CartItem>) cart;
    }

    public void addToCart(AddToCartRequest request, HttpSession session) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartItem item = new CartItem(
                product.getId(),
                product.getName(),
                request.getQuantity(),
                request.getFromDate(),
                request.getToDate()
        );

        getCart(session).add(item);
    }

    public List<CartItem> viewCart(HttpSession session) {
        return getCart(session);
    }

    public void checkout(HttpSession session) {
        session.removeAttribute(CART_KEY);
    }
}
