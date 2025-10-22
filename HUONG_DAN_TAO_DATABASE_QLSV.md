# 📚 Hướng Dẫn Tạo Database qlsv.db

## 🎯 Mục tiêu
Tạo file database **qlsv.db** bên ngoài bằng **DB Browser for SQLite**, sau đó copy vào thư mục **assets** của project Android.

---

## 📥 Bước 1: Tải và cài đặt DB Browser for SQLite

### Option 1: Download trực tiếp (Khuyên dùng)
```
Truy cập: https://sqlitebrowser.org/dl/
Chọn phiên bản phù hợp với Windows
```

### Option 2: Dùng Winget (Windows Package Manager)
```powershell
winget install -e --id DBBrowserForSQLite.DBBrowserForSQLite
```

### Option 3: Download bản portable (không cần cài đặt)
```
https://github.com/sqlitebrowser/sqlitebrowser/releases
Tải file: DB.Browser.for.SQLite-***-win64.zip
Giải nén và chạy DB Browser for SQLite.exe
```

---

## 🗄️ Bước 2: Tạo Database qlsv.db

### 1. Mở DB Browser for SQLite
- Chạy ứng dụng **DB Browser for SQLite**

### 2. Tạo Database mới
```
File → New Database
Chọn vị trí lưu: Desktop (hoặc nơi dễ tìm)
Đặt tên: qlsv.db
Nhấn Save
```

### 3. Tạo bảng sinhvien
Cửa sổ "Edit table definition" sẽ hiện ra:

**Table name:** `sinhvien`

**Columns:**

| Name | Type | PK | NN | Default | Check |
|------|------|----|----|---------|-------|
| MSSV | TEXT | ✅ | ✅ | | |
| Hoten | TEXT | | | | |
| Lop | TEXT | | | | |

**Giải thích:**
- **MSSV**: Mã số sinh viên (PRIMARY KEY)
- **Hoten**: Họ và tên
- **Lop**: Lớp

Nhấn **OK** để tạo bảng.

### 4. Thêm dữ liệu mẫu
Click tab **Browse Data** → Chọn bảng `sinhvien` → Click **New Record**

Thêm một vài sinh viên mẫu:

| MSSV | Hoten | Lop |
|------|-------|-----|
| 22020311 | Hoàng An | K67-CNTT |
| 22020723 | Cao Bình | K67-CNTT |

Để thêm record mới, click vào **Insert a new record** (icon +)

### 5. Lưu database
```
File → Write Changes (hoặc nhấn Ctrl+S)
```

---

## 📁 Bước 3: Tạo thư mục assets trong Android Project

### Trong Android Studio:

#### Cách 1: Qua Project View
```
1. Chuyển sang view "Project" (không phải "Android")
2. Navigate: Lab3 → app → src → main
3. Click phải vào "main" → New → Folder → Assets Folder
4. Nhấn Finish
```

#### Cách 2: Tạo thủ công
```
Mở File Explorer
Navigate đến: D:\University\Semester7\Mobile\LabMobile\Lab3\app\src\main\
Tạo folder mới tên: assets
```

---

## 📂 Bước 4: Copy file qlsv.db vào assets

### 1. Tìm file qlsv.db vừa tạo
- Thường ở Desktop hoặc nơi bạn đã save

### 2. Copy vào thư mục assets
```
Từ: Desktop\qlsv.db
Đến: D:\University\Semester7\Mobile\LabMobile\Lab3\app\src\main\assets\qlsv.db
```

### 3. Verify trong Android Studio
```
Project View → app → src → main → assets → qlsv.db
```

---

## ✅ Bước 5: Kiểm tra cấu trúc project

Sau khi hoàn thành, cấu trúc thư mục sẽ như sau:

```
Lab3/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── qlsv.db ✅ (File database)
│   │   │   ├── java/
│   │   │   │   └── com/example/lab3/
│   │   │   │       ├── StudentActivity.java
│   │   │   │       ├── helper/
│   │   │   │       │   └── StudentDatabaseHelper.java
│   │   │   │       └── ...
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
```

---

## 🧪 Bước 6: Test ứng dụng

### 1. Build và chạy app
```
Build → Clean Project
Build → Rebuild Project
Run (Shift + F10)
```

### 2. Kiểm tra
- Mở app → Click button **"🎓 Quản Lý Sinh Viên"**
- Nếu có dữ liệu trong qlsv.db, sẽ hiển thị ngay trong RecyclerView
- Nếu không có, app vẫn chạy bình thường, chỉ là RecyclerView rỗng

### 3. Test CRUD operations
- **INSERT**: Nhập thông tin → Nhấn INSERT
- **QUERY**: Nhập MSSV → Nhấn QUERY → Xem thông tin
- **UPDATE**: Nhập MSSV, sửa thông tin → Nhấn UPDATE
- **DELETE**: Nhập MSSV → Nhấn DELETE

---

## 🔍 Kiểm tra database trong app

### Dùng Database Inspector
```
View → Tool Windows → App Inspection → Database Inspector
→ Chọn database "qlsv.db"
→ Xem bảng "sinhvien"
```

---

## 💡 Lưu ý quan trọng

### 1. Tên database phải chính xác
- File name: `qlsv.db` (chữ thường)
- Trong code đã define: `DATABASE_NAME = "qlsv.db"`

### 2. Tên bảng và columns phải khớp
```sql
Table: sinhvien
Columns: MSSV, Hoten, Lop
```

### 3. Encoding
- Đảm bảo database sử dụng UTF-8 để hiển thị tiếng Việt đúng

### 4. Database chỉ copy 1 lần
- Code đã check: Nếu database đã tồn tại, không copy lại
- Muốn reset database: Uninstall app rồi cài lại

---

## 🛠️ Troubleshooting

### Lỗi: File not found
```
Nguyên nhân: File qlsv.db không có trong assets
Giải pháp: 
1. Kiểm tra lại đường dẫn: app/src/main/assets/qlsv.db
2. Rebuild project
3. Sync Project with Gradle Files
```

### Lỗi: Table not found
```
Nguyên nhân: Tên bảng hoặc columns không khớp
Giải pháp:
1. Mở qlsv.db bằng DB Browser
2. Kiểm tra tên bảng: "sinhvien"
3. Kiểm tra columns: "MSSV", "Hoten", "Lop"
```

### Database rỗng
```
Nguyên nhân: Chưa thêm dữ liệu mẫu
Giải pháp:
1. Mở qlsv.db bằng DB Browser
2. Browse Data → Insert records
3. Write Changes (Ctrl+S)
4. Copy lại file vào assets
5. Uninstall app rồi cài lại
```

---

## 📊 SQL Script để tạo bảng (Alternative)

Nếu bạn muốn dùng SQL trực tiếp trong DB Browser:

```sql
-- Tạo bảng sinhvien
CREATE TABLE sinhvien (
    MSSV TEXT PRIMARY KEY,
    Hoten TEXT,
    Lop TEXT
);

-- Insert dữ liệu mẫu
INSERT INTO sinhvien (MSSV, Hoten, Lop) VALUES 
('22020311', 'Hoàng An', 'K67-CNTT'),
('22020723', 'Cao Bình', 'K67-CNTT');
```

**Cách dùng:**
1. DB Browser → Tab "Execute SQL"
2. Copy paste code trên
3. Nhấn Execute (F5)
4. Write Changes (Ctrl+S)

---

## 🎯 Checklist hoàn thành

- [ ] Đã tải và cài DB Browser for SQLite
- [ ] Đã tạo file qlsv.db với bảng sinhvien
- [ ] Đã thêm ít nhất 2 sinh viên mẫu
- [ ] Đã tạo thư mục assets trong project
- [ ] Đã copy qlsv.db vào thư mục assets
- [ ] Đã Rebuild project
- [ ] Đã chạy app và thấy button "Quản Lý Sinh Viên"
- [ ] Đã test các chức năng INSERT, DELETE, UPDATE, QUERY

---

## 🚀 Kết quả mong đợi

Sau khi hoàn thành:
- ✅ App có màn hình quản lý sinh viên
- ✅ RecyclerView hiển thị danh sách sinh viên
- ✅ Có thể thêm, xóa, sửa, tìm sinh viên
- ✅ Dữ liệu được lưu vào SQLite database
- ✅ Có thể xem database qua Database Inspector

---

**Nếu gặp vấn đề, hãy kiểm tra lại từng bước một cách cẩn thận!** 🎓

