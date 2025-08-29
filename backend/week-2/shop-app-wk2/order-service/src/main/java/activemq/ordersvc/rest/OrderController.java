package activemq.ordersvc.rest;

import activemq.ordersvc.domain.OrderEntity;
import activemq.ordersvc.service.OrderService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    public static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    private record CreateOrderRequest(@NotBlank String customerName, @NotNull BigDecimal total){}

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getIndex(){
        return "Index";
    }

    @PostMapping
    public OrderEntity create(@RequestBody CreateOrderRequest rq){
        LOG.info("Received request to create order, {0}, {1}", rq.customerName(), rq.total());
        return orderService.create(rq.customerName(),  rq.total());
    }
}
