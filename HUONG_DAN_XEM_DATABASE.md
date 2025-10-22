# 📱 Hướng Dẫn Xem SQLite Database

## 🎯 Có 3 cách xem database SQLite trong Android Studio

---

## ✅ **CÁCH 1: Database Inspector (Khuyên dùng - Dễ nhất)**

### Bước 1: Chạy ứng dụng trên thiết bị/emulator
- Nhấn Run (Shift + F10)
- Đảm bảo app đang chạy trên device

### Bước 2: Mở Database Inspector
```
View → Tool Windows → App Inspection
```
Hoặc click vào tab **App Inspection** ở góc dưới Android Studio

### Bước 3: Chọn Database
- Trong tab **Database Inspector**, bạn sẽ thấy:
  - `Database_Demo` (database của users)
  - `contactsManager` (database của contacts)

### Bước 4: Xem dữ liệu
- Click vào `contactsManager` → mở rộng → chọn bảng `contacts`
- Bạn sẽ thấy tất cả records với columns: `id`, `name`, `phone_number`
- Có thể query trực tiếp bằng SQL

### Bước 5: Test real-time
- Long click xóa 1 contact trong app
- Click nút **Refresh** trong Database Inspector
- Thấy contact đã bị xóa khỏi database

**🎥 Screenshot vị trí:**
```
Android Studio
├── Toolbar: View → Tool Windows → App Inspection
└── Tab dưới cùng: App Inspection → Database Inspector
```

---

## ✅ **CÁCH 2: Device File Explorer + Export Database**

### Bước 1: Mở Device File Explorer
```
View → Tool Windows → Device File Explorer
```

### Bước 2: Navigate đến database
```
data/
  └── data/
      └── com.example.lab3/
          └── databases/
              ├── contactsManager
              ├── contactsManager-shm
              ├── contactsManager-wal
              ├── Database_Demo
              ├── Database_Demo-shm
              └── Database_Demo-wal
```

### Bước 3: Export database về máy
- Click phải vào file `contactsManager`
- Chọn **Save As...**
- Lưu với tên: `contactsManager.db`

### Bước 4: Mở bằng DB Browser for SQLite
- Tải DB Browser: https://sqlitebrowser.org/dl/
- Cài đặt xong, mở file `contactsManager.db`
- Xem bảng `contacts` với tất cả dữ liệu

---

## ✅ **CÁCH 3: ADB Command Line**

### Bước 1: Mở Terminal trong Android Studio
```
View → Tool Windows → Terminal
```

### Bước 2: Kiểm tra device đã kết nối
```bash
adb devices
```

### Bước 3: Truy cập vào SQLite shell
```bash
adb shell
cd /data/data/com.example.lab3/databases/
ls -la
```

### Bước 4: Query database trực tiếp
```bash
sqlite3 contactsManager

# Trong SQLite shell:
.tables                          # Xem tất cả bảng
SELECT * FROM contacts;          # Xem tất cả contacts
.quit                            # Thoát
```

### Bước 5: Pull database về máy (nếu cần)
```bash
# Thoát khỏi adb shell
exit

# Pull database về thư mục hiện tại
adb pull /data/data/com.example.lab3/databases/contactsManager contactsManager.db
```

---

## 📊 So sánh 3 cách

| Cách | Độ dễ | Real-time | Cần cài thêm | Khuyến nghị |
|------|-------|-----------|--------------|-------------|
| **Database Inspector** | ⭐⭐⭐⭐⭐ | ✅ | ❌ | **Tốt nhất** |
| **Device File Explorer** | ⭐⭐⭐⭐ | ❌ | DB Browser | Tốt |
| **ADB Command** | ⭐⭐⭐ | ✅ | ❌ | Nâng cao |

---

## 🧪 Test Flow hoàn chỉnh

### 1. Chạy app lần đầu
```
App tạo 4 contacts: Ravi, Srinivas, Tommy, Karthik
```

### 2. Xem trong Database Inspector
```
contacts table: 4 records
```

### 3. Long click xóa "Ravi"
```
Toast: "Đã xóa: Ravi"
```

### 4. Refresh Database Inspector
```
contacts table: 3 records (còn Srinivas, Tommy, Karthik)
```

### 5. Tiếp tục xóa và verify
```
Mỗi lần xóa → Refresh → Thấy giảm dần trong database
```

---

## 🔍 Queries hữu ích

### Trong Database Inspector hoặc SQLite shell:

```sql
-- Xem tất cả contacts
SELECT * FROM contacts;

-- Đếm số lượng contacts
SELECT COUNT(*) FROM contacts;

-- Xem contacts có tên cụ thể
SELECT * FROM contacts WHERE name = 'Ravi';

-- Xem contacts theo ID
SELECT * FROM contacts WHERE id > 5;

-- Xóa contact (test)
DELETE FROM contacts WHERE id = 1;
```

---

## 💡 Tips

1. **Database Inspector** chỉ hoạt động khi app đang chạy
2. Nhấn **Live Updates** để tự động refresh khi có thay đổi
3. Nếu không thấy database, thử:
   - Stop app
   - Uninstall app
   - Run lại
4. File `.db-shm` và `.db-wal` là temporary files của SQLite
5. Có thể copy SQL query từ Database Inspector để test

---

## 🎯 Vị trí chính xác trong Android Studio

### Database Inspector:
```
Android Studio Window
├── Menu Bar: View → Tool Windows → App Inspection
├── Tab Bar (Bottom): [Run] [Logcat] [App Inspection] [Build] [TODO]
└── App Inspection Panel
    ├── [Database Inspector] ← Click tab này
    ├── [Background Task Inspector]
    └── [Network Inspector]
```

### Device File Explorer:
```
Android Studio Window
├── Menu Bar: View → Tool Windows → Device File Explorer  
├── Right Sidebar: Click Device File Explorer icon
└── Navigate: data/data/com.example.lab3/databases/
```

---

## ❓ Troubleshooting

### Không thấy Database Inspector?
- Update Android Studio lên version 4.1+
- `File → Settings → Plugins → Check "Database Inspector"`

### Database rỗng?
- Đảm bảo app đang chạy
- Thử restart app
- Check Logcat xem có lỗi khi insert không

### Permission denied khi dùng ADB?
- Chỉ work với **debug build**
- Không work với **release build** (bị bảo vệ)

---

## 🚀 Quick Access (Phím tắt)

```
Alt + 6           → Logcat
Alt + 7           → Device File Explorer  
Ctrl + Shift + A  → Find Action → Gõ "Database Inspector"
```

**Khuyến nghị:** Dùng **Database Inspector** vì:
- ✅ Không cần setup gì
- ✅ Xem real-time
- ✅ Query trực tiếp
- ✅ UI đẹp, dễ dùng

