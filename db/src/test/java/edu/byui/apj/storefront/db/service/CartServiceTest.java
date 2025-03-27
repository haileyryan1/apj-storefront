package edu.byui.apj.storefront.db.service;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddItemToCart() {
        String cartId = "cart1";
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setItems(new ArrayList<>());

        Item item = new Item();
        item.setId(1L);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart updatedCart = cartService.addItemToCart(cartId, item);

        assertThat(updatedCart.getItems()).hasSize(1);
        verify(cartRepository).save(cart);
    }

    @Test
    public void testRemoveItemFromCart() {
        String cartId = "cart1";
        Long itemId = 1L;

        Item item = new Item();
        item.setId(itemId);
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setItems(new ArrayList<>(List.of(item)));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart updatedCart = cartService.removeItemFromCart(cartId, itemId);

        assertThat(updatedCart.getItems()).isEmpty();
        verify(cartRepository).save(cart);
    }

    @Test
    public void testGetCartThrowsIfNotFound() {
        when(cartRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.getCart("missing"));
    }

    @Test
    public void testGetCartsWithoutOrders() {
        List<Cart> carts = List.of(new Cart(), new Cart());
        when(cartRepository.findCartsWithoutOrders()).thenReturn(carts);

        List<Cart> result = cartService.getCartsWithoutOrders();

        assertThat(result).hasSize(2);
        verify(cartRepository).findCartsWithoutOrders();
    }
}

