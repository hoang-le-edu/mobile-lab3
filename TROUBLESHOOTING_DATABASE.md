# 🔧 Troubleshooting: Sửa lỗi Database không hoạt động

## ✅ Những thay đổi đã thực hiện:

### 1. **Thêm logging và error handling**
- INSERT, UPDATE, DELETE giờ trả về kết quả
- Hiển thị message chi tiết nếu thất bại
- Log tất cả operations vào Logcat

### 2. **Thêm method debugDatabase()**
- Kiểm tra table có tồn tại không
- Hiển thị cấu trúc columns
- Đếm số records trong database

---

## 🧪 CÁCH KIỂM TRA LỖI:

### Bước 1: Rebuild và chạy lại app
```
Build → Clean Project
Build → Rebuild Project
Run (Shift + F10)
```

### Bước 2: Mở Logcat
```
View → Tool Windows → Logcat
Filter: "StudentDB" hoặc "StudentActivity"
```

### Bước 3: Xem logs khi mở màn hình
Bạn sẽ thấy logs như sau:

```
D/StudentDB: Table 'sinhvien' exists
D/StudentDB: Table structure:
D/StudentDB:   Column: MSSV (TEXT)
D/StudentDB:   Column: Hoten (TEXT)
D/StudentDB:   Column: Lop (TEXT)
D/StudentDB: Total rows in table: 1
D/StudentDB: getAllStudents - Cursor count: 1
D/StudentDB: Loaded: 22520464 - Le Minh Hoang
D/StudentDB: Total students loaded: 1
D/StudentActivity: RecyclerView updated with 1 students
```

### Bước 4: Test INSERT
Nhập:
- MSSV: 22020999
- Họ tên: Nguyễn Văn A
- Lớp: K67

Nhấn INSERT → Xem Logcat:

```
D/StudentDB: Insert result: 1 for MSSV: 22020999
D/StudentActivity: loadStudents() called
D/StudentDB: getAllStudents - Cursor count: 2
D/StudentDB: Loaded: 22020999 - Nguyễn Văn A
D/StudentDB: Loaded: 22520464 - Le Minh Hoang
D/StudentDB: Total students loaded: 2
D/StudentActivity: RecyclerView updated with 2 students
```

✅ **result: 1** = Thành công
❌ **result: -1** = Lỗi (MSSV trùng hoặc constraint violation)

---

## 🚨 CÁC LỖI THƯỜNG GẶP:

### ❌ Lỗi 1: Table không tồn tại
```
E/StudentDB: Table 'sinhvien' does NOT exist!
```

**Nguyên nhân:** Database từ assets có tên bảng khác

**Giải pháp:**
1. Uninstall app hoàn toàn
2. Xóa file qlsv.db trong assets (nếu có)
3. Run lại app → App sẽ tạo database mới

```bash
adb uninstall com.example.lab3
```

---

### ❌ Lỗi 2: Column name không khớp
```
E/AndroidRuntime: java.lang.IllegalArgumentException: column 'MSSV' does not exist
```

**Nguyên nhân:** Database có column name khác (vd: Mssv thay vì MSSV)

**Giải pháp:** Xem log table structure:
```
D/StudentDB: Table structure:
D/StudentDB:   Column: Mssv (TEXT)  ← SAI! Phải là MSSV
```

Nếu column name sai → Uninstall app và run lại

---

### ❌ Lỗi 3: INSERT trả về -1 (MSSV trùng)
```
D/StudentDB: Insert result: -1 for MSSV: 22520464
Toast: "Lỗi: MSSV đã tồn tại hoặc lỗi database!"
```

**Nguyên nhân:** MSSV là PRIMARY KEY, không được trùng

**Giải pháp:** Nhập MSSV khác

---

### ❌ Lỗi 4: RecyclerView không update
```
D/StudentDB: Total students loaded: 2
D/StudentActivity: RecyclerView updated with 0 students  ← Lỗi!
```

**Nguyên nhân:** Adapter không nhận dữ liệu

**Giải pháp:** Kiểm tra lại khởi tạo adapter trong StudentActivity

---

### ❌ Lỗi 5: Database rỗng mặc dù đã có file qlsv.db trong assets
```
D/Database: Database already exists, skipping copy
D/StudentDB: Total rows in table: 0  ← Rỗng!
```

**Nguyên nhân:** 
- Lần đầu app chạy đã tạo database rỗng
- File assets không được copy

**Giải pháp:**
```bash
# Uninstall app
adb uninstall com.example.lab3

# Hoặc trong Android Studio: Run → Stop → Uninstall

# Kiểm tra file qlsv.db có trong assets không
# Đường dẫn: Lab3/app/src/main/assets/qlsv.db

# Run lại app
```

---

## 🛠️ RESET HOÀN TOÀN DATABASE:

### Cách 1: Uninstall app (Dễ nhất)
```
1. Long press app icon → Uninstall
2. Run lại từ Android Studio
```

### Cách 2: Dùng ADB
```bash
adb uninstall com.example.lab3
```

### Cách 3: Clear app data
```
Settings → Apps → Lab3 → Storage → Clear Data
```

### Cách 4: Xóa database trực tiếp (Nâng cao)
```bash
adb shell
run-as com.example.lab3
cd databases
rm qlsv.db
rm qlsv.db-shm
rm qlsv.db-wal
exit
exit
```

---

## 📊 CHECK DATABASE BẰNG DATABASE INSPECTOR:

```
View → Tool Windows → App Inspection → Database Inspector
→ Chọn "qlsv.db"
→ Xem bảng "sinhvien"
```

**Kiểm tra:**
- ✅ Table name: `sinhvien`
- ✅ Columns: `MSSV`, `Hoten`, `Lop`
- ✅ Records: Thấy dữ liệu đã INSERT

**Test real-time:**
1. INSERT sinh viên trong app
2. Click Refresh 🔄 trong Database Inspector
3. Thấy record mới xuất hiện

---

## 🎯 CHECKLIST ĐỂ ĐẢM BẢO HOẠT ĐỘNG:

### Khi mở app lần đầu:
- [ ] Logcat: `Table 'sinhvien' exists`
- [ ] Logcat: Hiển thị đúng 3 columns (MSSV, Hoten, Lop)
- [ ] Logcat: `Total rows in table: X`
- [ ] RecyclerView hiển thị đúng số sinh viên

### Khi INSERT:
- [ ] Toast: "Thêm sinh viên thành công!"
- [ ] Logcat: `Insert result: 1` (không phải -1)
- [ ] RecyclerView tự động update
- [ ] Sinh viên mới xuất hiện trong list

### Khi DELETE:
- [ ] Toast: "Xóa sinh viên thành công!"
- [ ] Logcat: `Delete result: 1 rows`
- [ ] RecyclerView tự động update
- [ ] Sinh viên biến mất khỏi list

### Khi UPDATE:
- [ ] Toast: "Cập nhật sinh viên thành công!"
- [ ] Logcat: `Update result: 1 rows`
- [ ] RecyclerView tự động update
- [ ] Thông tin sinh viên thay đổi trong list

### Khi QUERY:
- [ ] Toast: "Tìm thấy sinh viên!"
- [ ] Form tự động fill thông tin
- [ ] Nếu không tìm thấy: Toast "Không tìm thấy sinh viên!"

---

## 💻 COMMAND DEBUG NHANH:

### Xem tất cả logs của app:
```bash
adb logcat -s StudentDB:D StudentActivity:D
```

### Xem database file:
```bash
adb shell "run-as com.example.lab3 ls -la databases/"
```

### Pull database về xem:
```bash
adb exec-out run-as com.example.lab3 cat databases/qlsv.db > qlsv_debug.db
```

Sau đó mở file `qlsv_debug.db` bằng DB Browser for SQLite

---

## 📝 LOG MẪU KHI HOẠT ĐỘNG ĐÚNG:

```
D/StudentDB: Table 'sinhvien' exists
D/StudentDB: Table structure:
D/StudentDB:   Column: MSSV (TEXT)
D/StudentDB:   Column: Hoten (TEXT)
D/StudentDB:   Column: Lop (TEXT)
D/StudentDB: Total rows in table: 1
D/StudentDB: getAllStudents - Cursor count: 1
D/StudentDB: Loaded: 22520464 - Le Minh Hoang
D/StudentDB: Total students loaded: 1
D/StudentActivity: loadStudents() called
D/StudentActivity: RecyclerView updated with 1 students

--- User INSERT new student ---
D/StudentDB: Insert result: 1 for MSSV: 22020999
D/StudentActivity: loadStudents() called
D/StudentDB: getAllStudents - Cursor count: 2
D/StudentDB: Loaded: 22020999 - Nguyễn Văn A
D/StudentDB: Loaded: 22520464 - Le Minh Hoang
D/StudentDB: Total students loaded: 2
D/StudentActivity: RecyclerView updated with 2 students

--- User DELETE student ---
D/StudentDB: Delete result: 1 rows for MSSV: 22020999
D/StudentActivity: loadStudents() called
D/StudentDB: getAllStudents - Cursor count: 1
D/StudentDB: Loaded: 22520464 - Le Minh Hoang
D/StudentDB: Total students loaded: 1
D/StudentActivity: RecyclerView updated with 1 students
```

---

## ✅ NẾU VẪN KHÔNG HOẠT ĐỘNG:

1. **Copy TOÀN BỘ LOG từ Logcat** khi:
   - Mở màn hình Quản lý sinh viên
   - Thực hiện INSERT
   - Paste log vào đây để tôi phân tích

2. **Kiểm tra Database Inspector:**
   - Screenshot bảng sinhvien
   - Kiểm tra column names có đúng không

3. **Uninstall hoàn toàn và test lại:**
   ```bash
   adb uninstall com.example.lab3
   # Run lại từ Android Studio
   ```

Bây giờ hãy **Rebuild** app và xem **Logcat** để debug! 🚀

