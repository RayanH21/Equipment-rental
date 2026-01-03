package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.dto.AddToCartRequest;
import be.rayanhaddou.equipmentrental.model.CartItem;
import be.rayanhaddou.equipmentrental.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_KEY = "CART";

    private final ReservationService reservationService;
    private final ProductRepository productRepository;

    public CartService(ReservationService reservationService, ProductRepository productRepository) {
        this.reservationService = reservationService;
        this.productRepository = productRepository;
    }

    // ➕ item toevoegen aan cart
    public void addToCart(AddToCartRequest request, HttpSession session) {
        List<CartItem> cart = getCart(session);

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is not available");
        }

        if (request.getQuantity() > product.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock");
        }

        CartItem item = new CartItem(
                request.getProductId(),
                product.getName(), // ✅ nu gevuld
                request.getQuantity(),
                request.getFromDate(),
                request.getToDate()
        );

        cart.add(item);
        session.setAttribute(CART_KEY, cart);
    }

    // 👀 cart bekijken
    public List<CartItem> viewCart(HttpSession session) {
        return getCart(session);
    }

    // ✅ checkout: reservations maken + stock verminderen
    public void checkout(HttpSession session) {
        List<CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            return;
        }

        reservationService.checkout(cart);

        // 🧹 cart leegmaken
        session.setAttribute(CART_KEY, new ArrayList<CartItem>());
    }

    // 🔒 helper om cart uit session te halen
    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        Object obj = session.getAttribute(CART_KEY);
        if (obj == null) {
            List<CartItem> cart = new ArrayList<>();
            session.setAttribute(CART_KEY, cart);
            return cart;
        }
        return (List<CartItem>) obj;
    }
}