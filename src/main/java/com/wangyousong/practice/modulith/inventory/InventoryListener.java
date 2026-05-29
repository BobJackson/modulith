package com.wangyousong.practice.modulith.inventory;

import com.wangyousong.practice.modulith.order.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * 库存模块监听器：监听订单创建事件，执行库存扣减逻辑。
 * <p>
 * 使用 @ApplicationModuleListener 注解，等同于 @Async + @TransactionalEventListener，
 * 在独立事务中处理事件。如果处理失败，Spring Modulith 会自动重试，
 * 事件记录保留在 event_publication 表中直到处理成功。
 */
@Component
@Slf4j
public class InventoryListener {

    @ApplicationModuleListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("收到订单事件: orderId={}, productCode={}, quantity={}",
                event.orderId(), event.productCode(), event.quantity());

        // TODO: 执行实际的库存扣减逻辑
        deductInventory(event.productCode(), event.quantity());

        log.info("库存扣减完成: productCode={}, quantity={}", event.productCode(), event.quantity());
    }

    private void deductInventory(String productCode, int quantity) {
        // 模拟库存扣减，实际项目中应调用库存 Repository
        log.info("正在扣减库存: productCode={}, quantity={}", productCode, quantity);
    }
}
