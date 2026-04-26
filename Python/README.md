# Python 推理服务

这是一个基于 Flask + Ultralytics YOLO 的推理服务，用于接收前端或 Spring Boot 上传的图片并返回检测结果。

## 功能

- 加载 `best.pt`
- 提供 `GET /health` 健康检查
- 提供 `POST /infer` 推理接口
- 支持跨域请求，方便与 Vue / Spring Boot 联调
- 支持违规类别冷却提示：`smoke`、`drink` 在 60 秒内只返回一次告警

## 模型标签

- `0 -> smoke`
- `1 -> drink`

## 目录结构

- `app.py`：Flask 服务入口
- `requirements.txt`：Python 依赖
- `best.pt`：YOLO 模型文件，放在同目录下

## 请求示例

`POST /infer`

表单字段：
- `image`：上传图片文件
- `conf`：可选，置信度阈值，默认 `0.5`

返回示例：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "count": 2,
    "detections": [
      {
        "label": "smoke",
        "class_id": 0,
        "confidence": 0.92,
        "box": {
          "x1": 12.3,
          "y1": 45.6,
          "x2": 210.1,
          "y2": 388.0
        }
      }
    ],
    "alerts": [
      {
        "label": "smoke",
        "confidence": 0.92,
        "time": "15:16:39",
        "message": "检测到违规行为：smoke"
      }
    ],
    "has_violation": true,
    "alert_count": 1,
    "classes": {
      "0": "smoke",
      "1": "drink"
    }
  }
}
```
