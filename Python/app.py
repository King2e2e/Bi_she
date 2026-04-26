from flask import Flask, request, jsonify
from flask_cors import CORS
from ultralytics import YOLO
import os
import tempfile
import time
from datetime import datetime

app = Flask(__name__)
CORS(app)

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(BASE_DIR, "best.pt")

# 你的模型类别：0=smoke, 1=drink
CLASS_MAP = {
    0: "smoke",
    1: "drink",
}

# 违规弹窗冷却时间：同一类别 60 秒内只报一次
ALERT_COOLDOWN = 60
last_alert_time = {}
model = None


def load_model():
    global model
    if model is None:
        if not os.path.exists(MODEL_PATH):
            raise FileNotFoundError(f"Model file not found: {MODEL_PATH}")
        model = YOLO(MODEL_PATH)
    return model


def is_violation(class_name: str, confidence: float, threshold: float = 0.5) -> bool:
    return class_name in {"smoke", "drink"} and confidence >= threshold


def build_alerts(detections):
    global last_alert_time
    alerts = []
    now = time.time()

    for item in detections:
        label = item["label"]
        confidence = item["confidence"]
        if not is_violation(label, confidence):
            continue

        last_time = last_alert_time.get(label)
        if last_time is not None and (now - last_time) < ALERT_COOLDOWN:
            continue

        last_alert_time[label] = now
        alerts.append({
            "label": label,
            "confidence": round(confidence, 4),
            "time": datetime.now().strftime("%H:%M:%S"),
            "message": f"检测到违规行为：{label}",
        })

    return alerts


@app.route("/health", methods=["GET"])
def health():
    return jsonify({
        "code": 200,
        "msg": "ok",
        "data": {
            "model_loaded": model is not None,
            "model_path": MODEL_PATH,
            "classes": CLASS_MAP,
            "alert_cooldown": ALERT_COOLDOWN,
        }
    })


@app.route("/infer", methods=["POST"])
def infer():
    try:
        current_model = load_model()

        if "image" not in request.files:
            return jsonify({"code": 400, "msg": "missing image file", "data": None}), 400

        image_file = request.files["image"]
        suffix = os.path.splitext(image_file.filename or "frame.jpg")[1] or ".jpg"
        conf = float(request.form.get("conf", 0.5))

        with tempfile.NamedTemporaryFile(delete=False, suffix=suffix) as tmp:
            image_file.save(tmp.name)
            temp_path = tmp.name

        try:
            results = current_model.predict(source=temp_path, conf=conf)
            detections = []

            for result in results:
                names = result.names
                if result.boxes is None:
                    continue
                for box in result.boxes:
                    cls_id = int(box.cls[0])
                    raw_label = names.get(cls_id, str(cls_id))
                    # 兼容模型输出为 0/1 或 smoke/drink 名称
                    if str(raw_label).isdigit():
                        label = CLASS_MAP.get(int(raw_label), str(raw_label))
                    else:
                        label = str(raw_label)
                    confidence = float(box.conf[0])
                    xyxy = box.xyxy[0].tolist()
                    detections.append({
                        "label": label,
                        "class_id": cls_id,
                        "confidence": round(confidence, 4),
                        "box": {
                            "x1": round(xyxy[0], 2),
                            "y1": round(xyxy[1], 2),
                            "x2": round(xyxy[2], 2),
                            "y2": round(xyxy[3], 2),
                        }
                    })

            alerts = build_alerts(detections)

            return jsonify({
                "code": 200,
                "msg": "success",
                "data": {
                    "count": len(detections),
                    "detections": detections,
                    "alerts": alerts,
                    "has_violation": len(alerts) > 0,
                    "alert_count": len(alerts),
                    "classes": CLASS_MAP,
                }
            })
        finally:
            if os.path.exists(temp_path):
                os.remove(temp_path)

    except Exception as exc:
        return jsonify({"code": 500, "msg": str(exc), "data": None}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
