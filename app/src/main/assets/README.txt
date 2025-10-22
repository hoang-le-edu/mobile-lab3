================================================================================
HƯỚNG DẪN: Đặt file qlsv.db vào thư mục này
================================================================================

📁 Thư mục này dùng để chứa file database qlsv.db

📝 CÁCH TẠO FILE qlsv.db:

1. Tải DB Browser for SQLite:
   https://sqlitebrowser.org/dl/

2. Tạo database mới tên: qlsv.db

3. Tạo bảng "sinhvien" với cấu trúc:
   - MSSV (TEXT, PRIMARY KEY)
   - Hoten (TEXT)
   - Lop (TEXT)

4. Thêm dữ liệu mẫu (optional):
   INSERT INTO sinhvien VALUES ('22020311', 'Hoàng An', 'K67-CNTT');
   INSERT INTO sinhvien VALUES ('22020723', 'Cao Bình', 'K67-CNTT');

5. Copy file qlsv.db vào thư mục này:
   Lab3/app/src/main/assets/qlsv.db

📚 Chi tiết xem file: HUONG_DAN_TAO_DATABASE_QLSV.md trong thư mục gốc project

⚠️  LƯU Ý:
- Nếu không có file qlsv.db, app vẫn chạy được (sẽ tạo database rỗng)
- Để reset database: Uninstall app rồi cài lại

================================================================================

