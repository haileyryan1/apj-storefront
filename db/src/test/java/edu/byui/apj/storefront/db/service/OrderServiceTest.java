package edu.byui.apj.storefront.db.service;

import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrder() {
        Cart cart = new Cart();
        cart.setId("cart123");

        CardOrder order = new CardOrder();
        order.setCart(cart);

        when(cartService.getCart("cart123")).thenReturn(cart);
        when(orderRepository.save(order)).thenReturn(order);

        CardOrder savedOrder = orderService.saveOrder(order);

        assertThat(savedOrder.getCart()).isEqualTo(cart);
        verify(orderRepository).save(order);
        verify(cartService).getCart("cart123");
    }

    @Test
    void testGetOrder() {
        CardOrder order = new CardOrder();
        order.setId(42L);

        when(orderRepository.findById(42L)).thenReturn(Optional.of(order));

        Optional<CardOrder> result = orderService.getOrder(42L);

        assertThat(result).isPresent().contains(order);
        verify(orderRepository).findById(42L);
    }
}
