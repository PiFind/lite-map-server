# 轻量级地图服务器

## 模块介绍

* **[map-base](map-base/README.md) :** 其他服务引用的基础模块
* **[place-service](place-service/README.md) :** 地点服务
* **[place-service-open](place-service-open) :** 地点开放服务
* **[poi-service](poi-service/README.md) :** POI服务
* **[poi-service](poi-service-open) :** POI开放服务
* **[third-part](third-party/README.md) :** 第三方服务

## 启动顺序

地点服务：
1. place-service-open

POI 服务：
1. poi-service
2. poi-service-open
