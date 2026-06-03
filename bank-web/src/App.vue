<template>
  <router-view />
</template>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app { height: 100%; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif; -webkit-font-smoothing: antialiased; }
:root {
  --sidebar-bg: #ffffff;
  --sidebar-text: #4a5568;
  --sidebar-text-secondary: #a0aec0;
  --sidebar-active: #3182ce;
  --sidebar-active-bg: #ebf4ff;
  --sidebar-hover-bg: #f7fafc;
  --sidebar-border: #edf2f7;
  --header-bg: #ffffff;
  --header-border: #edf2f7;
  --main-bg: #f7fafc;
  --card-shadow: 0 1px 3px rgba(0,0,0,0.06), 0 1px 2px rgba(0,0,0,0.04);
  --card-hover-shadow: 0 10px 25px rgba(0,0,0,0.08);
  --text-primary: #1a202c;
  --text-secondary: #718096;
}
body { background-color: var(--main-bg); }
::-webkit-scrollbar { width: 5px; height: 5px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: #cbd5e0; border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: #a0aec0; }
.el-table { border-radius: 10px; overflow: hidden; box-shadow: var(--card-shadow); }
.el-table th.el-table__cell { background-color: #f7fafc !important; color: var(--text-secondary); font-weight: 600; }
.el-card { border-radius: 10px; border: 1px solid #edf2f7; box-shadow: var(--card-shadow); transition: box-shadow 0.2s, transform 0.2s; }
.el-card:hover { box-shadow: var(--card-hover-shadow); }
.el-dialog { border-radius: 16px; }
.el-dialog .el-dialog__header { padding: 24px 28px 0; }
.el-dialog .el-dialog__body { padding: 20px 28px; }
.el-dialog .el-dialog__footer { padding: 0 28px 24px; }
.el-button--primary { background: #3182ce; border-color: #3182ce; }
.el-button--primary:hover { background: #2b6cb0; border-color: #2b6cb0; }
.el-button--primary:focus { background: #3182ce; border-color: #3182ce; }
.el-button--danger { background: #e53e3e; border-color: #e53e3e; }
.el-button--danger:hover { background: #c53030; border-color: #c53030; }
.el-pagination.is-background .el-pager li:not(.disabled).active { background: #3182ce; }
.el-tag--success { background: #c6f6d5; border-color: #9ae6b4; color: #276749; }
.el-tag--danger { background: #fed7d7; border-color: #feb2b2; color: #9b2c2c; }
.el-tag--warning { background: #fefcbf; border-color: #f6e05e; color: #975a16; }
.el-input__wrapper { border-radius: 8px !important; }
.el-select .el-input__wrapper { border-radius: 8px !important; }
</style>