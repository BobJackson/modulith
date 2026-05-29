# Spring Modulith 实践项目

基于 **Spring Modulith** 的模块化单体应用示例，演示如何在单体架构中实现模块边界隔离与事件驱动的模块间通信。

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring Modulith | 2.0.6 |
| Hibernate | 7.2.12.Final |
| PostgreSQL | — |
| GraalVM Native Build Tools | 0.11.5 |
| Lombok | — |

## 项目结构

```
src/main/java/com/wangyousong/practice/modulith/
├── ModulithApplication.java          # 应用入口
├── order/                            # 订单模块
│   ├── Order.java                    # 订单实体
│   ├── OrderRepository.java          # 订单仓储
│   ├── OrderService.java             # 订单业务逻辑
│   ├── OrderController.java          # 订单 REST 接口
│   └── OrderPlacedEvent.java         # 订单创建事件（模块间通信载体）
└── inventory/                        # 库存模块
    └── InventoryListener.java        # 库存事件监听器（消费订单事件）
```

## 核心功能

### 模块间事件驱动通信

1. **订单模块** 接收下单请求，保存订单后发布 `OrderPlacedEvent` 到 `event_publication` 表（与订单事务一致）
2. **库存模块** 通过 `@ApplicationModuleListener` 异步监听订单事件，在独立事务中执行库存扣减
3. 若库存扣减失败，Spring Modulith 自动重试，直到处理成功

### 架构守护测试

`ArchitectureGuardTest` 无需启动 Spring 容器，快速验证：
- 模块间无循环依赖
- 模块只能访问其他模块公开的 API
- 自动生成 PlantUML 模块依赖图

## 环境要求

- JDK 25+
- PostgreSQL（运行在 `localhost:5432`，数据库 `test`，用户 `postgres`）

数据库配置见 `src/main/resources/application.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/test
    username: postgres
    password: postgres
```

## 快速开始

### 1. 启动 PostgreSQL

```bash
# 使用 Docker 快速启动
docker run -d \
  --name postgres \
  -e POSTGRES_DB=test \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:latest
```

### 2. 运行应用

```bash
./gradlew bootRun
```

### 3. 调用下单接口

```bash
curl -X POST "http://localhost:8080/orders?productCode=SKU001&quantity=5"
```

### 4. 运行测试

```bash
# 运行所有测试（含架构守护测试）
./gradlew test
```

## 关键依赖说明

| 依赖 | 作用 |
|------|------|
| `spring-modulith-starter-core` | 模块定义、边界校验、事件发布基础设施 |
| `spring-modulith-starter-jpa` | 事件持久化（`event_publication` 表），保证事件与业务数据事务一致性 |
| `spring-modulith-starter-test` | 模块结构验证测试支持 |
