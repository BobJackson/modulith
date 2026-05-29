package com.wangyousong.practice.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * 架构守护测试 —— 确保模块边界和依赖关系符合 Spring Modulith 规范。
 * <p>
 * 该测试不需要启动 Spring 容器，运行速度快，适合作为 CI 流水线中的快速检查。
 */
class ArchitectureGuardTest {

    ApplicationModules modules = ApplicationModules.of(ModulithApplication.class);

    /**
     * 验证模块结构：
     * - 模块间不允许循环依赖
     * - 模块只能访问其他模块公开的 API（public 类型）
     * - 不允许访问其他模块的内部类型
     */
    @Test
    void verifyModuleStructure() {
        modules.verify();
    }

    /**
     * 打印模块结构信息，便于调试和了解当前模块划分。
     */
    @Test
    void printModuleOverview() {
        modules.forEach(System.out::println);
    }

    @Test
    void writeDocumentation() {
        // 2. 自动生成文档：在 target/modulith-docs 目录下自动生成 C4 架构图和模块依赖文档
        new Documenter(modules).writeModulesAsPlantUml();
    }
}
