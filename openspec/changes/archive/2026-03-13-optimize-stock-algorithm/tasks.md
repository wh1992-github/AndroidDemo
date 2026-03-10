## 1. 数据读取优化

- [x] 1.1 创建内部类 `MonthStats`，包含 average、minPrice、minLine、maxPrice、maxLine 字段
- [x] 1.2 实现 `getMonthStats` 方法，一次文件读取返回完整月度统计
- [x] 1.3 重构 `printMonth` 使用 `getMonthStats` 替代 `getMonthAverage`/`getMonthMin`/`getMonthMax`
- [x] 1.4 删除 `getMonthAverage`、`getMonthMin`、`getMonthMax` 三个旧方法

## 2. 核心算法重构

- [x] 2.1 重命名核心变量：`temp`→`trackedPrice`、`tempIndex`→`trackedIndex`、`flag`→`isHolding`、`base_money`→`capital`
- [x] 2.2 提取滑动窗口极值追踪逻辑为独立方法（如 `updateTrackedPrice`）
- [x] 2.3 在 `printTest` 中使用提取后的方法替代内联逻辑
- [x] 2.4 在 `printMoney` 中使用提取后的方法替代内联逻辑

## 3. 日志格式化

- [x] 3.1 将 `printTest` 中超长的 `stringBuilder.append` 行拆分为多行 `String.format`
- [x] 3.2 将 `printMoney` 中超长的 Log.i 行拆分为多行 `String.format`

## 4. 清理

- [x] 4.1 确认并删除未使用的 `getAverage5` 方法
- [x] 4.2 清理 `printTest` 中未使用的静态变量 `buy_min`/`sell_min`/`buy_max`/`sell_max`（改为局部变量）
