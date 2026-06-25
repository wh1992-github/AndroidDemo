# Note Management Specification

> **状态：规划中 / 未实现**。本规格描述的是规划中的笔记管理功能，当前代码库中（含 `app` 模块）尚无对应的 Activity、数据模型或存储实现。请勿将其理解为已交付功能。

## Requirements

### Requirement: 创建笔记

用户 SHALL 能够创建新的笔记。

#### Scenario: 创建成功

- **GIVEN** 用户在笔记列表页面
- **WHEN** 用户点击新建按钮并输入标题和内容
- **THEN** 系统 SHALL 保存笔记到本地数据库
- **AND** 系统 SHALL 显示创建成功提示
- **AND** 笔记列表 SHALL 刷新显示新笔记

#### Scenario: 标题为空

- **GIVEN** 用户在编辑笔记页面
- **WHEN** 用户未输入标题就点击保存
- **THEN** 系统 SHALL 显示"标题不能为空"提示
- **AND** 系统 SHALL NOT 保存笔记
