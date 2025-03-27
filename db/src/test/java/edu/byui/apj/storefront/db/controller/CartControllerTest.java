package edu.byui.apj.storefront.db.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CartControllerTest {

    private MockMvc mockMvc;
    private CartService cartService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        cartService = mock(CartService.class);
        CartController cartController = new CartController();
        cartController.getClass().getDeclaredFields(); // Just to hint at autowired
        // Inject mock manually since we no longer use @Autowired
        cartController = injectCartService(cartController, cartService);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    private CartController injectCartService(CartController controller, CartService service) {
        try {
            var field = CartController.class.getDeclaredField("cartService");
            field.setAccessible(true);
            field.set(controller, service);
            return controller;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetCartById() throws Exception {
        Cart cart = new Cart();
        cart.setId("cart1");

        when(cartService.getCart("cart1")).thenReturn(cart);

        mockMvc.perform(get("/cart/cart1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart1"));
    }

    @Test
    void testSaveCart() throws Exception {
        Cart cart = new Cart();
        cart.setId("cart1");

        when(cartService.saveCart(any())).thenReturn(cart);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart1"));
    }

    @Test
    void testAddItemToCart() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setCardId("card123");

        Cart cart = new Cart();
        cart.setId("cart1");
        cart.setItems(List.of(item));

        when(cartService.addItemToCart(eq("cart1"), any())).thenReturn(cart);

        mockMvc.perform(post("/cart/cart1/item")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart1"));
    }

    @Test
    void testRemoveCart() throws Exception {
        doNothing().when(cartService).removeCart("cart1");

        mockMvc.perform(delete("/cart/cart1"))
                .andExpect(status().isOk());
    }
}

