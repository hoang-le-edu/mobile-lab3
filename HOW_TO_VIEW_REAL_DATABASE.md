# 📱 Cách Xem Database THẬT từ Device

## 🎯 Vấn đề

Khi bạn INSERT data trong app, data được lưu vào database **TRONG DEVICE/EMULATOR**:
```
/data/data/com.example.lab3/databases/qlsv.db
```

Nhưng nếu bạn mở file `qlsv.db` trên Desktop hoặc trong assets, bạn đang xem **FILE KHÁC**!

---

## ✅ CÁCH 1: Database Inspector (KHUYÊN DÙNG)

### Bước 1: Mở Database Inspector
```
View → Tool Windows → App Inspection
```

### Bước 2: Chọn tab "Database Inspector"

### Bước 3: Đảm bảo app đang chạy
- App phải đang chạy trên emulator/device
- Nếu không thấy database, stop và run lại app

### Bước 4: Chọn database "qlsv.db"
- Click vào `qlsv.db` để expand
- Click vào bảng `sinhvien`
- Bạn sẽ thấy **DATA THẬT** trong app!

### Bước 5: Test real-time
1. INSERT sinh viên trong app
2. Click nút **Refresh** 🔄 trong Database Inspector
3. Thấy data mới xuất hiện ngay

**Screenshot vị trí:**
```
Android Studio (Bottom tabs):
[Run] [Logcat] [App Inspection] ← Click đây
              ↓
  [Database Inspector] ← Tab này
  [Background Task Inspector]
  [Network Inspector]
```

---

## ✅ CÁCH 2: Pull Database File về máy

### Sử dụng Device File Explorer

#### Bước 1: Mở Device File Explorer
```
View → Tool Windows → Device File Explorer
```

#### Bước 2: Navigate đến database
```
data/
  └── data/
      └── com.example.lab3/
          └── databases/
              └── qlsv.db  ← File này!
```

#### Bước 3: Download về máy
1. Click phải vào `qlsv.db`
2. Chọn **Save As...**
3. Lưu với tên: `qlsv_from_device.db`

#### Bước 4: Mở bằng DB Browser for SQLite
1. Mở DB Browser for SQLite
2. File → Open Database
3. Chọn file `qlsv_from_device.db`
4. Browse Data → Xem bảng `sinhvien`
5. Thấy data thật từ app!

---

## ✅ CÁCH 3: Dùng ADB Command

### Pull database về máy
```bash
# Chạy trong Terminal của Android Studio
adb exec-out run-as com.example.lab3 cat databases/qlsv.db > qlsv_real.db
```

### Mở file qlsv_real.db
- Dùng DB Browser for SQLite
- Thấy data thật từ app

---

## 🔍 SO SÁNH 3 DATABASE FILES

### 1. File trong assets (Template)
```
Đường dẫn: Lab3/app/src/main/assets/qlsv.db
Mục đích: Template để copy vào device lần đầu
Data: Dữ liệu mẫu ban đầu (nếu có)
Không thay đổi: Luôn giữ nguyên
```

### 2. File trên Desktop (Backup/Development)
```
Đường dẫn: Desktop/qlsv.db (hoặc nơi bạn tạo)
Mục đích: Để tạo template hoặc backup
Data: Dữ liệu mẫu
Không liên quan: Không ảnh hưởng đến app đang chạy
```

### 3. File THẬT trong device (Runtime)
```
Đường dẫn: /data/data/com.example.lab3/databases/qlsv.db
Mục đích: Database app đang sử dụng
Data: Dữ liệu thật khi INSERT/UPDATE/DELETE
CẦN XEM FILE NÀY: Để kiểm tra app hoạt động đúng
```

---

## 📊 Workflow Đúng

### Khi phát triển app:

#### 1. Tạo template (Optional)
```
Desktop/qlsv.db 
→ Tạo bảng + data mẫu
→ Copy vào: Lab3/app/src/main/assets/qlsv.db
```

#### 2. App chạy lần đầu
```
assets/qlsv.db 
→ COPY vào: /data/data/com.example.lab3/databases/qlsv.db
→ App sử dụng file này từ bây giờ
```

#### 3. Khi INSERT/UPDATE/DELETE
```
App thao tác trên: /data/data/com.example.lab3/databases/qlsv.db
KHÔNG ảnh hưởng: assets/qlsv.db và Desktop/qlsv.db
```

#### 4. Khi kiểm tra data
```
XEM FILE: /data/data/com.example.lab3/databases/qlsv.db
DÙNG: Database Inspector hoặc pull về
```

---

## 🎯 Test Flow Đúng

### Step 1: Run app
```
App copy database từ assets (nếu chưa có)
Hoặc tạo database mới
```

### Step 2: INSERT data trong app
```
Data được lưu vào: /data/data/.../qlsv.db
Log: Insert result: 2 for MSSV: xxx
```

### Step 3: Xem data THẬT
```
Database Inspector → Refresh
Thấy data vừa INSERT
```

### Step 4: Pull về để backup (Optional)
```
Device File Explorer → Save As
Hoặc: adb exec-out run-as ... > backup.db
```

---

## ⚠️ LƯU Ý QUAN TRỌNG

### ❌ SAI: Mở file Desktop/qlsv.db sau khi INSERT
```
→ Không thấy data mới
→ Vì app KHÔNG ghi vào file này!
```

### ✅ ĐÚNG: Dùng Database Inspector
```
→ Xem database ĐANG CHẠY trong device
→ Thấy data real-time
```

### ❌ SAI: Edit file assets/qlsv.db và mong app update
```
→ App đã copy file rồi, không đọc lại
→ Muốn reset: Uninstall app rồi run lại
```

### ✅ ĐÚNG: Muốn reset database
```
Uninstall app → Run lại
App sẽ copy lại từ assets (nếu có)
```

---

## 🛠️ Quick Commands

### Pull database từ running app:
```bash
adb exec-out run-as com.example.lab3 cat databases/qlsv.db > current_db.db
```

### Xem database info:
```bash
adb shell "run-as com.example.lab3 ls -la databases/"
```

### Xem số records:
```bash
adb shell "run-as com.example.lab3 sqlite3 databases/qlsv.db 'SELECT COUNT(*) FROM sinhvien;'"
```

### Xem tất cả records:
```bash
adb shell "run-as com.example.lab3 sqlite3 databases/qlsv.db 'SELECT * FROM sinhvien;'"
```

---

## 📱 Sử dụng Database Inspector (Recommended)

### Ưu điểm:
- ✅ Xem real-time (đang chạy)
- ✅ Không cần pull file
- ✅ Có thể query trực tiếp
- ✅ Refresh để xem changes
- ✅ UI đẹp, dễ dùng

### Cách dùng:
1. Run app trên emulator
2. View → Tool Windows → App Inspection
3. Tab: Database Inspector
4. Chọn qlsv.db → sinhvien table
5. Thấy tất cả data thật!

### Test INSERT:
1. INSERT sinh viên trong app
2. Click Refresh 🔄 trong Database Inspector
3. Thấy record mới xuất hiện
4. Perfect! ✅

---

## 🎓 Kết luận

**Luôn nhớ:**
- App sử dụng database trong `/data/data/.../databases/`
- Đó là file KHÁC với file trên Desktop/assets
- Dùng Database Inspector để xem data thật
- Pull về nếu muốn backup hoặc xem offline

**Không bao giờ:**
- Mở file Desktop/qlsv.db và mong thấy data từ app
- Edit file assets/qlsv.db và mong app tự update
- Quên refresh Database Inspector sau khi thay đổi data

