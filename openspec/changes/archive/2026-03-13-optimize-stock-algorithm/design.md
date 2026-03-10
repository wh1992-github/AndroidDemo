## Context

JDK.java 是股票买卖策略的核心算法类，包含约 400 行代码。当前存在以下问题：
- `printTest` 和 `printMoney` 中有重复的"滑动窗口寻找局部极值 + 买卖信号判断"逻辑
- `getMonthAverage`、`getMonthMin`、`getMonthMax` 三个方法每次都独立打开文件流读取，同一个月的数据被读取 3 次
- 变量命名模糊（`temp`、`flag`、`b`、`s`），算法意图不清晰
- 超长的 Log 拼接行（单行超 300 字符），难以阅读和维护

## Goals / Non-Goals

**Goals:**
- 提取公共的"滑动窗口极值追踪"逻辑为独立方法，消除 `printTest` 和 `printMoney` 中的重复代码
- 合并三个文件读取方法为一次读取，返回包含 average/min/max 的统计结果
- 改善核心变量命名，让算法意图更清晰
- 拆分超长日志行为多行拼接

**Non-Goals:**
- 不改变算法本身的策略逻辑和计算结果
- 不修改公共方法签名（`start`、`printTest`、`printMoney`、`printBestBuySell`、`printMonth`）
- 不引入新的依赖或架构模式
- 不修改 AlarmActivity.java 或其他文件

## Decisions

### 1. 提取滑动窗口极值追踪为独立方法
**决策**: 将"在给定天数窗口内追踪局部最低/最高点"的逻辑提取为 `trackExtreme` 或类似方法。
**理由**: `printTest` 和 `printMoney` 中这段逻辑几乎完全相同，只是上下文（寻找买入点 vs 卖出点）不同。提取后可统一维护。

### 2. 创建月度统计数据类
**决策**: 创建内部类 `MonthStats`，包含 `average`、`minPrice`、`minLine`、`maxPrice`、`maxLine` 字段。用一个方法 `getMonthStats` 一次读取文件返回完整统计。
**理由**: 替代 3 个独立方法（`getMonthAverage`、`getMonthMin`、`getMonthMax`），减少 3 次文件 IO 为 1 次。
**备选方案**: 使用 Map 返回多个值 — 类型不安全，放弃。

### 3. 变量重命名策略
**决策**:
- `temp` → `trackedPrice`（追踪的极值价格）
- `tempIndex` → `trackedIndex`
- `flag` → `isHolding`（是否持仓）
- `b` / `s` 循环变量 → `buyThreshold` / `sellThreshold`
- `base_money` → `capital`
**理由**: 直接反映变量在算法中的含义。

### 4. 日志格式化
**决策**: 使用 `String.format` 替代超长的字符串拼接，每行控制在合理长度内。
**理由**: 提升可读性，便于后续修改日志格式。

## Risks / Trade-offs

- [日志输出变化] → 重构后的日志格式可能与之前略有不同（如空格、对齐），但内容和数值不变
- [方法提取过度] → 仅提取确实重复的逻辑，避免过度抽象。保持 `printTest` 和 `printMoney` 作为独立方法
