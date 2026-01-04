# 热部署配置指南

## 项目热部署配置已完成

项目中已经添加了Spring Boot DevTools依赖，并且配置了相关的application.yaml设置。要启用热部署，请按以下步骤在IntelliJ IDEA中进行配置：

## 1. 启用自动编译

### 步骤1：启用Build project automatically
1. 打开 `File` -> `Settings` (Windows/Linux) 或 `IntelliJ IDEA` -> `Preferences` (Mac)
2. 导航到 `Build, Execution, Deployment` -> `Compiler`
3. 勾选 `Build project automatically` 选项

### 步骤2：启用运行时自动编译
1. 使用快捷键 `Ctrl+Shift+A` (Windows) 或 `Cmd+Shift+A` (Mac) 打开Action搜索
2. 搜索 `Registry`
3. 在Registry中找到 `compiler.automake.allow.when.app.running` 并勾选它

## 2. 启动应用

1. 运行 `YfApplication` 类启动应用
2. 确保控制台显示与devtools相关的日志信息

## 3. 测试热部署

1. 应用启动后，修改任意Java源代码（如Controller、Service等）
2. 保存文件 (Ctrl+S)
3. IDEA会自动编译更改的文件
4. 应用会在几秒内重新加载更改，无需重启整个应用

## 配置说明

- `spring.devtools.restart.enabled: true` - 启用重启功能
- `spring.devtools.restart.additional-paths: src/main/java` - 监听src/main/java目录下的变更
- `spring.devtools.livereload.enabled: true` - 启用LiveReload功能

## 注意事项

1. 热部署只在开发环境中使用，生产环境会自动禁用
2. 某些大的结构性变更（如添加依赖、修改配置类）可能仍需要重启应用
3. 静态资源文件（HTML, CSS, JS等）的修改通常会立即生效
4. 实体类的修改可能需要重启应用才能完全生效

## 验证热部署是否工作

1. 启动应用后，控制台应该显示类似信息：
   ```
   Restarting due to 1 class path change (0 additions, 0 deletions, 1 modification)
   ```

2. 修改任意Controller中的返回值或消息，保存后观察是否自动重新加载

这样配置后，您就可以享受热部署带来的开发效率提升，无需频繁重启应用即可看到代码修改的效果。