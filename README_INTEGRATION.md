# Hướng Dẫn Kết Nối Frontend và Backend - Bikestore

## 🚀 Tổng Quan

Dự án Bikestore đã được kết nối hoàn chỉnh giữa Frontend (HTML/CSS/JS) và Backend (Spring Boot + MongoDB). Tài liệu này hướng dẫn cách chạy và sử dụng hệ thống.

## 📁 Cấu Trúc Dự Án

```
bikestore/
├── fontend/                    # Frontend (HTML/CSS/JS)
│   ├── html/                  # Các trang HTML
│   ├── css/                   # Stylesheets
│   ├── js/                    # JavaScript files
│   │   └── api.js            # API Service chung
│   └── img/                   # Hình ảnh
├── src/main/java/             # Backend (Spring Boot)
│   └── com/example/bikestore/
│       ├── Controllers/       # REST Controllers
│       ├── Models/           # Data Models
│       ├── Services/         # Business Logic
│       └── Repositories/     # Data Access
└── pom.xml                   # Maven Configuration
```

## 🛠️ Yêu Cầu Hệ Thống

- **Java 21+**
- **Maven 3.6+**
- **MongoDB 4.4+**
- **Web Browser** (Chrome, Firefox, Safari, Edge)

## 🚀 Cách Chạy Dự Án

### 1. Khởi động MongoDB

```bash
# Windows
mongod

# macOS/Linux
sudo systemctl start mongod
# hoặc
mongod --dbpath /path/to/your/db
```

### 2. Chạy Backend (Spring Boot)

```bash
cd bikestore
mvn spring-boot:run
```

Backend sẽ chạy tại: `http://localhost:8080`

### 3. Chạy Frontend

Mở file `bikestore/fontend/html/Trang Chủ.html` trong trình duyệt web.

**Hoặc sử dụng Live Server (khuyến nghị):**

```bash
# Cài đặt Live Server
npm install -g live-server

# Chạy từ thư mục frontend
cd bikestore/fontend
live-server
```

Frontend sẽ chạy tại: `http://localhost:8080` (nếu dùng Live Server)

## 🔧 Cấu Hình

### Backend Configuration

File: `src/main/resources/application.properties`

```properties
spring.application.name=bikestore
spring.data.mongodb.uri=mongodb://localhost:27017/bike_store
spring.data.mongodb.database=bike_store
server.port=8080
```

### Frontend Configuration

File: `fontend/js/api.js`

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## 📋 Chức Năng Đã Kết Nối

### ✅ Frontend Pages
- **Trang Chủ** - Hiển thị sản phẩm nổi bật từ API
- **Danh Sách Sản Phẩm** - Tìm kiếm, lọc sản phẩm
- **Chi Tiết Sản Phẩm** - Thông tin chi tiết từ API
- **Giỏ Hàng** - Quản lý giỏ hàng local + API
- **Thanh Toán** - Tạo đơn hàng qua API
- **Đăng Nhập/Đăng Ký** - Xác thực qua API
- **Theo Dõi Đơn Hàng** - Xem lịch sử đơn hàng
- **Liên Hệ** - Form liên hệ
- **Chính Sách** - Các chính sách công ty

### ✅ Backend APIs
- **Product APIs** - CRUD sản phẩm
- **User APIs** - Đăng ký, đăng nhập, quản lý user
- **Order APIs** - Tạo, quản lý đơn hàng
- **Dashboard APIs** - Thống kê, báo cáo

## 🔌 API Endpoints

### Products
- `GET /api/products` - Lấy tất cả sản phẩm
- `GET /api/products/{id}` - Lấy sản phẩm theo ID
- `GET /api/products/search?name={name}` - Tìm kiếm sản phẩm
- `GET /api/products/category/{category}` - Lọc theo danh mục
- `GET /api/products/brand/{brand}` - Lọc theo thương hiệu
- `GET /api/products/price?minPrice={min}&maxPrice={max}` - Lọc theo giá

### Users
- `POST /api/users/register` - Đăng ký
- `POST /api/users/login` - Đăng nhập
- `GET /api/users/{id}` - Lấy thông tin user
- `PUT /api/users/{id}` - Cập nhật user

### Orders
- `POST /api/orders` - Tạo đơn hàng
- `GET /api/orders/user/{userId}` - Lấy đơn hàng của user
- `GET /api/orders/{id}` - Lấy đơn hàng theo ID
- `PUT /api/orders/{id}/status?status={status}` - Cập nhật trạng thái

### Dashboard
- `GET /api/dashboard/orders` - Thống kê đơn hàng
- `GET /api/dashboard/customers` - Thống kê khách hàng
- `GET /api/dashboard/revenue` - Thống kê doanh thu
- `GET /api/dashboard/sales-performance` - Hiệu suất bán hàng

## 🧪 Test API

### Sử dụng Postman hoặc curl:

```bash
# Lấy tất cả sản phẩm
curl http://localhost:8080/api/products

# Đăng ký user mới
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"123456"}'

# Đăng nhập
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456"}'
```

## 🐛 Troubleshooting

### Lỗi thường gặp:

1. **CORS Error**
   - Kiểm tra backend có chạy không
   - Đảm bảo `@CrossOrigin(origins = "*")` trong Controllers

2. **MongoDB Connection Error**
   - Kiểm tra MongoDB có chạy không
   - Kiểm tra connection string trong `application.properties`

3. **API không hoạt động**
   - Kiểm tra log backend
   - Đảm bảo port 8080 không bị chiếm dụng

4. **Frontend không load được**
   - Sử dụng Live Server thay vì mở file trực tiếp
   - Kiểm tra console browser để xem lỗi

## 📊 Dữ Liệu Mẫu

Hệ thống sẽ tự động tạo dữ liệu mẫu khi khởi động lần đầu. Bạn có thể:

1. Đăng ký tài khoản mới
2. Thêm sản phẩm vào giỏ hàng
3. Tạo đơn hàng
4. Xem thống kê trong dashboard

## 🔄 Workflow Hoàn Chỉnh

1. **User đăng ký/đăng nhập** → API User
2. **Xem sản phẩm** → API Products
3. **Thêm vào giỏ hàng** → LocalStorage
4. **Thanh toán** → API Orders
5. **Theo dõi đơn hàng** → API Orders
6. **Admin quản lý** → Dashboard APIs

## 🚀 Tính Năng Nâng Cao

- **Offline Support** - Fallback về localStorage khi mất kết nối
- **Real-time Notifications** - Thông báo trạng thái
- **Error Handling** - Xử lý lỗi toàn diện
- **Loading States** - Hiển thị trạng thái loading
- **Responsive Design** - Tối ưu cho mobile

## 📝 Ghi Chú

- Tất cả API calls đều có error handling
- Frontend có fallback về localStorage khi backend không khả dụng
- Hệ thống hỗ trợ cả user đã đăng ký và khách
- Dữ liệu được validate ở cả frontend và backend

## 🎯 Kết Luận

Hệ thống Bikestore đã được kết nối hoàn chỉnh với:
- ✅ Frontend responsive và user-friendly
- ✅ Backend API đầy đủ chức năng
- ✅ Database MongoDB linh hoạt
- ✅ Error handling toàn diện
- ✅ Offline support
- ✅ Security cơ bản

Bạn có thể chạy ngay hệ thống và bắt đầu sử dụng!
