package com.wangyousong.practice.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher events;

    /**
     * 下单：保存订单并在同一事务中发布事件到 event_publication 表。
     */
    @Transactional
    public Order placeOrder(String productCode, int quantity) {
        Order order = new Order(productCode, quantity);
        Order saved = orderRepository.save(order);
        // 事件会在同一事务中持久化到 event_publication 表
        events.publishEvent(new OrderPlacedEvent(saved.getId(), saved.getProductCode(), saved.getQuantity()));
        return saved;
    }
}
