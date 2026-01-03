package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.dto.AddToCartRequest;
import be.rayanhaddou.equipmentrental.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_KEY = "CART";

    private final ReservationService reservationService;

    public CartService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ➕ item toevoegen aan cart
    public void addToCart(AddToCartRequest request, HttpSession session) {
        List<CartItem> cart = getCart(session);

        CartItem item = new CartItem(
                request.getProductId(),
                null, // productName vullen we later (of laten we leeg)
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

        // 🔥 HIER gebeurt het echte werk
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