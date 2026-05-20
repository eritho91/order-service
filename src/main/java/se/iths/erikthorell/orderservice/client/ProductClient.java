package se.iths.erikthorell.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import se.iths.erikthorell.orderservice.dto.ProductInfoResponse;
import se.iths.erikthorell.orderservice.dto.ProductStockRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestClient productRestClient;

    public List<ProductInfoResponse> decreaseStock(
            List<ProductStockRequest> items,
            String bearerToken
    ) {
        try {
            return productRestClient.post()
                    .uri("/products/stock/decrease")
                    .header("Authorization", bearerToken)
                    .body(items)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

        } catch (RestClientResponseException exception) {
            throw new ResponseStatusException(
                    exception.getStatusCode(),
                    "Fel från product-service: " + exception.getResponseBodyAsString()
            );

        } catch (Exception exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Order-service kunde inte kontakta product-service"
            );
        }
    }
}