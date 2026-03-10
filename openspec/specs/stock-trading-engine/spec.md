## Requirements

### Requirement: 滑动窗口极值追踪
系统 SHALL 提供独立的滑动窗口极值追踪方法，用于在给定天数窗口内追踪价格的局部最低点或最高点。

#### Scenario: 追踪局部最低点
- **WHEN** 在未持仓状态下遍历价格列表
- **THEN** 系统 SHALL 持续追踪窗口内的最低价格及其索引
- **AND** 当价格偏离超过窗口天数时，系统 SHALL 向前滑动追踪起点

#### Scenario: 追踪局部最高点
- **WHEN** 在持仓状态下遍历价格列表
- **THEN** 系统 SHALL 持续追踪窗口内的最高价格及其索引
- **AND** 当价格偏离超过窗口天数时，系统 SHALL 向前滑动追踪起点

### Requirement: 买卖信号检测复用
`printTest` 和 `printMoney` 中的买卖信号检测逻辑 SHALL 复用同一段极值追踪代码，避免重复实现。

#### Scenario: printTest 使用提取后的方法
- **WHEN** `printTest` 执行策略优化搜索
- **THEN** 系统 SHALL 调用提取后的极值追踪方法来判断买卖时机
- **AND** 计算结果 SHALL 与重构前完全一致

#### Scenario: printMoney 使用提取后的方法
- **WHEN** `printMoney` 执行实际买卖模拟
- **THEN** 系统 SHALL 调用提取后的极值追踪方法来判断买卖时机
- **AND** 计算结果 SHALL 与重构前完全一致

### Requirement: 变量命名清晰化
核心算法中的变量 SHALL 使用有意义的名称，反映其在交易策略中的角色。

#### Scenario: 重命名后代码可读性
- **WHEN** 阅读重构后的代码
- **THEN** `isHolding` SHALL 表示当前是否持仓
- **AND** `trackedPrice` SHALL 表示当前追踪的极值价格
- **AND** `capital` SHALL 表示当前资金

### Requirement: 日志格式化
超长的日志拼接 SHALL 使用 `String.format` 重写，每行控制在合理长度内。

#### Scenario: 日志输出内容不变
- **WHEN** 执行重构后的算法
- **THEN** 日志 SHALL 包含与重构前相同的数值信息（收益、买卖价格、百分比等）
