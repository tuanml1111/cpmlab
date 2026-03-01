# Student Management System
Lê Minh Tuấn - 2252868

---

## Public URL

> Web Service đã được deploy tại:



## Hướng Dẫn Cách Chạy Dự Án

### Yêu cầu

- Java Development Kit (JDK) 17+
- Maven
- PostgreSQL (local hoặc Neon cloud)
- Docker (tuỳ chọn)

### Chạy Local (không Docker)

**1. Cấu hình Database**

Copy file `.env.example` thành `.env` và điền thông tin thực tế:

```bash
cp .env.example .env
```

Nội dung file `.env`:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/student_management
DB_USERNAME=postgres
DB_PASSWORD=your_password_here
```

**2. Tạo database PostgreSQL**

```sql
CREATE DATABASE student_management;
```

**3. Chạy ứng dụng**

```bash
./mvnw spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

---

### Chạy bằng Docker

**1. Build image**

```bash
docker build -t student-management .
```

**2. Run container**

```bash
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://<host>:5432/student_management \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  student-management

## API Endpoints

### REST API


 GET  /api/students :  Lấy danh sách tất cả sinh viên 
 GET /api/students/{id}: Lấy thông tin chi tiết sinh viên theo ID |

### Web UI (Thymeleaf SSR)

GET  /students : Trang danh sách sinh viên (có tìm kiếm) 
GET /students/new:  Form thêm sinh viên mới 
POST /students: Lưu sinh viên mới 
GET /students/{id}: Trang chi tiết sinh viên 
GET /students/{id}/edit: Form chỉnh sửa sinh viên 
POST /students/{id}/edit  Lưu cập nhật sinh viên 
POST /students/{id}/delete Xoá sinh viên 

---

## Câu Trả Lời Lý Thuyết

### Lab 1: Khởi Tạo & Kiến Trúc

**Câu 2 — Ràng buộc Khóa Chính (Primary Key): Tại sao Database lại chặn thao tác INSERT khi `id` trùng?**

> Primary Key (Khóa chính) là ràng buộc đảm bảo mỗi hàng trong bảng có một định danh **duy nhất** và **khác NULL**. Khi cố tình INSERT một bản ghi có `id` đã tồn tại, Database sẽ phát sinh lỗi `UNIQUE constraint failed` và từ chối thao tác đó. Lý do là Primary Key vừa ngầm định ràng buộc `UNIQUE` (không trùng lặp) vừa ràng buộc `NOT NULL`, giúp hệ thống luôn có thể định danh chính xác từng bản ghi. Nếu cho phép trùng `id`, hệ thống sẽ không thể phân biệt được hai sinh viên khác nhau, dẫn tới dữ liệu mơ hồ và các phép JOIN, truy vấn theo `id` sẽ cho kết quả sai.

---

**Câu 3 — Toàn vẹn dữ liệu (Constraints): Điều gì xảy ra khi INSERT sinh viên với `name` để NULL?**

> Trong schema được tạo ở Lab 1, cột `name` được khai báo là `TEXT` mà **không có** ràng buộc `NOT NULL`. Do đó SQLite **sẽ không báo lỗi** và cho phép lưu bản ghi với `name = NULL` thành công.
>
> Hậu quả phía Java: khi Hibernate đọc bản ghi này lên, thuộc tính `name` của đối tượng `Student` sẽ có giá trị `null`. Nếu bất kỳ đoạn code nào gọi `student.getName().toLowerCase()` hoặc thực hiện thao tác chuỗi mà không kiểm tra null trước, ứng dụng sẽ ném `NullPointerException` tại runtime. Đây là rủi ro tiềm ẩn nghiêm trọng — việc thiếu ràng buộc ở tầng Database buộc lập trình viên phải tự xử lý null ở mọi nơi trong code Java, rất dễ bỏ sót. Giải pháp đúng là thêm `NOT NULL` vào schema hoặc dùng annotation `@Column(nullable = false)` trên Entity để Hibernate tự tạo ràng buộc.

---

**Câu 4 — Cấu hình Hibernate: Tại sao mỗi lần chạy lại ứng dụng, dữ liệu cũ bị mất hết?**

> Nguyên nhân nằm ở cấu hình sau trong `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=create
```

> Với giá trị `create`, mỗi khi ứng dụng Spring Boot khởi động, Hibernate sẽ **DROP toàn bộ bảng cũ** rồi **tạo lại từ đầu** dựa trên các Entity class. Điều này có nghĩa là mọi dữ liệu đã tồn tại trong file `student.db` đều bị xóa sạch.

