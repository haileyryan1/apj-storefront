package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.CardOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testFindCartsWithoutOrders() {
        Cart cart1 = new Cart();
        cart1.setId("c1");
        cart1.setPersonId("p1");

        Cart cart2 = new Cart();
        cart2.setId("c2");
        cart2.setPersonId("p2");

        cartRepository.save(cart1);
        cartRepository.save(cart2);

        // Add an order for cart1
        CardOrder order = new CardOrder();
        order.setCart(cart1);
        orderRepository.save(order);

        List<Cart> result = cartRepository.findCartsWithoutOrders();

        assertThat(result).containsExactly(cart2);
    }
}

