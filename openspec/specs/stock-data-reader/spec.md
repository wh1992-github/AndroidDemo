## Requirements

### Requirement: 统一月度数据统计
系统 SHALL 提供单一方法 `getMonthStats`，一次文件读取即可获取指定月份的平均价、最低价、最高价及对应行信息。

#### Scenario: 获取完整月度统计
- **WHEN** 调用 `getMonthStats` 并传入年份、文件名、月份
- **THEN** 系统 SHALL 只打开文件一次
- **AND** 返回结果 SHALL 包含 average、minPrice、minLine、maxPrice、maxLine

#### Scenario: 无匹配数据
- **WHEN** 指定月份在文件中无数据
- **THEN** 系统 SHALL 返回空结果或默认值
- **AND** 系统 SHALL NOT 抛出异常

### Requirement: MonthStats 数据结构
系统 SHALL 使用内部类 `MonthStats` 封装月度统计结果，替代原来三个方法分别返回不同类型的做法。

#### Scenario: 数据结构完整性
- **WHEN** 创建 MonthStats 对象
- **THEN** 对象 SHALL 包含 average(float)、minPrice(float)、minLine(String)、maxPrice(float)、maxLine(String) 字段

### Requirement: printMonth 使用统一读取
`printMonth` 方法 SHALL 使用 `getMonthStats` 替代原来分别调用 `getMonthAverage`、`getMonthMin`、`getMonthMax` 的方式。

#### Scenario: 减少文件 IO
- **WHEN** `printMonth` 统计一个月的数据
- **THEN** 系统 SHALL 只读取文件一次（而非三次）
- **AND** 计算结果 SHALL 与重构前完全一致
