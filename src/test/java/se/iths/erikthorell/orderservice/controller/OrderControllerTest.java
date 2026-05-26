package se.iths.erikthorell.orderservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.erikthorell.orderservice.dto.OrderResponse;
import se.iths.erikthorell.orderservice.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Starta ordercontroller
@WebMvcTest(
        controllers = OrderController.class,
        excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class
)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrderShouldReturnCreatedOrder() throws Exception {
        OrderResponse response = new OrderResponse(
                1L,
                LocalDateTime.now(),
                "ada@example.com",
                List.of(),
                BigDecimal.valueOf(299)
        );

        when(orderService.createOrder(
                any(),
                eq("ada@example.com"),
                eq("Bearer fake-token")
        )).thenReturn(response);

        String json = """
                {
                  "items": [
                    {
                      "productId": 1,
                      "quantity": 2
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/orders")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .subject("ada@example.com")
                                        .tokenValue("fake-token"))
                                .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("ada@example.com"))
                .andExpect(jsonPath("$.totalPrice").value(299));
    }
}