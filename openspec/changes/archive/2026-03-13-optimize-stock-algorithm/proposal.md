## Why

JDK.java 中的股票买卖算法存在大量重复代码，可读性差，维护困难。`printTest` 和 `printMoney` 方法中有重复的"寻找局部极值"逻辑，文件读取方法（`getMonthAverage`、`getMonthMin`、`getMonthMax`）每次都重新打开文件流，效率低下。代码缺乏清晰的结构划分，算法意图不明确。

## What Changes

- 提取重复的"滑动窗口寻找局部极值 + 买卖信号判断"逻辑为独立方法
- 合并 `getMonthAverage`、`getMonthMin`、`getMonthMax` 三个文件读取方法，一次读取获取所有统计数据
- 改善变量命名，用有意义的名称替代 `temp`、`flag`、`b`、`s` 等
- 减少 `printMoney` 和 `printTest` 中超长的日志拼接行，提升可读性
- 移除未使用的方法 `getAverage5`（如确认未使用）

## Capabilities

### New Capabilities
- `stock-trading-engine`: 核心买卖策略算法的提取与优化，包括信号检测、收益计算
- `stock-data-reader`: 股票数据文件的读取与按月分组统计

### Modified Capabilities

## Impact

- 主要影响文件：`app/src/main/java/com/example/helloworld/JDK.java`
- `AlarmActivity.java` 调用 JDK 的公共方法签名保持不变，无需修改
- 无外部依赖变化
