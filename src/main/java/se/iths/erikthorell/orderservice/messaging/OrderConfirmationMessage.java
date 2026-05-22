package se.iths.erikthorell.orderservice.messaging;

import java.math.BigDecimal;
import java.util.List;

//Här skapas meddelandet till email-service
//Hur JSON-filen kommer se ut:
// {
//        "orderId": 3,
//        "customerEmail": "erik@gmail.com",
//        "items": [
//          {
//          "name": "T-shirt",
//          "price": 199,
//          "quantity": 2,
//          "lineTotal": 398
//          }
//        ],
//        "totalPrice": 398
//        }

public record OrderConfirmationMessage(
        Long orderId,
        String customerEmail,
        List<OrderConfirmationItem> items,
        BigDecimal totalPrice
) {
    public record OrderConfirmationItem(
            String name,
            BigDecimal price,
            int quantity,
            BigDecimal lineTotal
    ) {
    }
}