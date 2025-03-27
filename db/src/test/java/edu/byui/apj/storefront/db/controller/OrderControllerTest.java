package edu.byui.apj.storefront.db.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ObjectMapper.class) // Ensure ObjectMapper is present
public class OrderControllerTest {

    private MockMvc mockMvc;

    private OrderService orderService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        OrderController orderController = new OrderController(orderService);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testSaveOrder() throws Exception {
        CardOrder order = new CardOrder();
        Cart cart = new Cart();
        cart.setId("cart1");
        order.setCart(cart);
        order.setTotal(110.0);

        when(orderService.saveOrder(any())).thenReturn(order);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(110.0));
    }

    @Test
    void testGetOrderFound() throws Exception {
        CardOrder order = new CardOrder();
        order.setId(1L);

        when(orderService.getOrder(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetOrderNotFound() throws Exception {
        when(orderService.getOrder(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/order/999"))
                .andExpect(status().isNotFound());
    }
}
