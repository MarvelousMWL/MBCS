# MBCS 每日复盘自动化

## 概述

本项目每日复盘自动化系统会在每天 **8:00** 自动执行以下流程：

1. **启动服务** - 启动 bank-server 后端和 Vite 前端
2. **页面巡检** - 检测关键 API 和前端页面的可用性
3. **缺陷修复** - 自动修复端口冲突和数据库连接等常见问题
4. **生成日报** - 输出详细的中文复盘日报

## 文件说明

| 文件 | 说明 |
|------|------|
| `daily_review.ps1` | 主执行脚本，完成启动→巡检→修复→日报全流程 |
| `install_scheduler.ps1` | 注册 Windows 计划任务（每日 8:00） |
| `logs/` | 运行日志、服务日志、Bug 记录 |

## 使用方式

### 1. 安装计划任务（每日自动执行）
```powershell
# 以管理员身份运行
powershell -ExecutionPolicy Bypass -File daily_review\install_scheduler.ps1
```

### 2. 手动立即执行
```powershell
powershell -ExecutionPolicy Bypass -File daily_review\daily_review.ps1
```

### 3. 仅巡检不启动服务
```powershell
powershell -ExecutionPolicy Bypass -File daily_review\daily_review.ps1 -SkipStart
```

## 日报输出

日报输出到 `daily_review/日报_YYYY-MM-DD.md`，包含：
- 服务状态概览
- 页面巡检结果（前台页面 + 后端 API）
- 安全审计
- 性能分析
- Bug 与缺陷统计
- 3条可改进建议（含优先级、位置、实施步骤、验证方式）
- 2个建议新增功能（含作用说明、优先级、设计位置、实施步骤、验证方式）
- 今日推荐执行计划

## 注意事项

- 需要 **Java 17+**、**Node.js**、**Maven** 环境
- 需要 **MySQL** 服务运行且 `bank_core` 数据库已初始化
- 计划任务推荐以管理员身份安装
