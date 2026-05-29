package com.wangyousong.practice.modulith.order;

import java.util.UUID;

/**
 * 订单已创建事件，用于通知其他模块（如库存模块）进行后续处理。
 */
public record OrderPlacedEvent(UUID orderId, String productCode, int quantity) {
}
