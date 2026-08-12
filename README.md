# 苍穹外卖后端（sky-take-out）

苍穹外卖（Sky Take-out）餐饮外卖管理系统的后端服务，基于 Spring Boot 构建，同时为「管理端」（React）和「小程序用户端」（微信小程序）提供 REST API。

## 技术栈

| 项 | 选型 |
|---|---|
| 框架 | Spring Boot 2.7.3 |
| ORM | MyBatis + PageHelper 分页 |
| 数据库 | MySQL（Druid 连接池） |
| 缓存 | Redis |
| 鉴权 | JWT（jjwt） |
| 接口文档 | Knife4j（Swagger） |
| Excel | Apache POI（运营报表导出） |
| 文件存储 | 阿里云 OSS |
| 实时通信 | WebSocket（来单提醒） |
| 定时任务 | Spring `@Scheduled` |
| 其他 | Lombok / fastjson / commons-lang |

## 模块结构

| 模块 | 说明 |
|---|---|
| `sky-common` | 公共代码：常量、枚举、工具类、异常、JSON、ThreadLocal 上下文 |
| `sky-pojo` | 实体（Entity）、DTO、VO |
| `sky-server` | 启动类 + Controller + Service + Mapper + 配置 |

## 功能

### 管理端接口（`/admin/**`）

- 员工管理：登录、增删改查、启停用
- 分类管理 / 菜品管理 / 套餐管理：CRUD、启停用、批量删除
- 订单管理：条件搜索、接单、拒单、派送、完成、取消
- 店铺营业状态：营业 / 打烊切换
- 数据统计：营业额、用户、订单、销量 Top10
- 工作台：今日营业数据概览（营业额、订单量、菜品/套餐数量）
- 运营报表导出：Excel 导出（POI，基于模板）
- 文件上传（OSS）

### 用户端接口（`/user/**`）

- 微信登录（`wx.login` → code 换 token）
- 分类 / 菜品 / 套餐浏览
- 购物车
- 地址簿
- 下单 / 支付 / 取消 / 催单 / 退款 / 再来一单
- 个人中心（查询 / 修改用户信息）

### 通用能力

- WebSocket 来单提醒：管理端实时收到新订单
- 定时任务：订单超时自动取消、派送中订单处理

## 配置

默认使用 `dev` 环境（`application.yml` 中 `spring.profiles.active: dev`），敏感配置放在 `application-dev.yml`（已加入 `.gitignore`，需自行准备）。

| 项 | 值 |
|---|---|
| 端口 | 8080 |
| MySQL | `localhost:3306 / sky_take_out / root / root` |
| Redis | `127.0.0.1:6379`（无密码） |
| 管理端 JWT | header `token`，密钥 `itcast`，有效期 2 小时 |
| 用户端 JWT | header `authentication`，密钥 `codermcq`，有效期 2 小时 |

> 首次运行前需创建数据库 `sky_take_out` 并导入表结构/数据，同时在 `application-dev.yml` 中填入阿里云 OSS 与微信支付的密钥。

## 运行

```bash
# 1. 启动 MySQL 与 Redis
# 2. 编译打包
mvn clean package -DskipTests

# 3. 运行（以 sky-server 模块为主，自动加载依赖模块）
mvn spring-boot:run -pl sky-server -am
```

启动类：`com.sky.SkyApplication`

接口文档：启动后访问 `http://localhost:8080/doc.html`（Knife4j）
