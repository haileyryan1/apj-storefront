package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveAndFindOrder() {
        Cart cart = new Cart();
        cart.setId("cart1");
        cart.setPersonId("person1");

        Address address = new Address();
        address.setAddressLine1("123 Main St");
        address.setCity("Cityville");
        address.setState("ST");
        address.setZipCode("12345");
        address.setCountry("USA");

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("1234567890");

        CardOrder order = new CardOrder();
        order.setCart(cart);
        order.setShippingAddress(address);
        order.setCustomer(customer);
        order.setOrderDate(new Date());
        order.setSubtotal(100.0);
        order.setTax(10.0);
        order.setTotal(110.0);
        order.setShipMethod("Standard");
        order.setOrderNotes("Leave at door");
        order.setConfirmationSent(true);

        CardOrder savedOrder = orderRepository.save(order);
        Optional<CardOrder> retrieved = orderRepository.findById(savedOrder.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTotal()).isEqualTo(110.0);
    }
}
