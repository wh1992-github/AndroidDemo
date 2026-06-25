## 数据格式基线

行情文件位于 `app/src/main/assets/`，文件名为「股票名.txt」，每行一条记录，格式为 `YYYY-MM-DD,星期------收盘价`，例如：

```text
2016-01-04,一------20.78
```

- 解析方式：以 `------` 后的 6 个字符作 `Float.parseFloat` 得到收盘价；行首字符取月份与日期。
- 当前内置 7 个文件：三特索道、晶方科技、曲江文旅、浪潮信息、通富微电、长城证券、长白山。
- 文件不含涨跌幅、振幅、成交量字段。
- `Constants.getData()` 提供另一份硬编码数据（华天酒店），不来自文件。

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
- **WHEN** 单次调用 `getMonthStats` 统计一个指定月份
- **THEN** 系统 SHALL 只打开并读取行情文件一次（而非重构前的三次）
- **AND** 计算结果 SHALL 与重构前完全一致

> 说明：`printMonth` 因需分别取得"买入月最低价"与"次月最高价"，每月会调用两次 `getMonthStats`，即打开两次文件；"只读一次"针对每次 `getMonthStats` 调用而言。
