# 🛒 Hướng Dẫn Sửa Lỗi Giỏ Hàng

## Vấn Đề Đã Được Sửa

Lỗi không thể thêm sản phẩm vào giỏ hàng đã được khắc phục. Vấn đề chính là sự không khớp giữa cấu trúc dữ liệu mà frontend gửi và backend mong đợi.

### Các Thay Đổi Đã Thực Hiện:

1. **Cập nhật Cart Model** (`src/main/java/com/example/bikestore/bikestore/Models/Cart.java`):
   - Thêm các field: `id`, `name`, `price`, `image` vào `CartItem`
   - Giữ nguyên `productId` và `quantity` để tương thích ngược
   - Thêm constructor để hỗ trợ cả frontend và backend

2. **Cải thiện CartService** (`src/main/java/com/example/bikestore/bikestore/Services/CartService.java`):
   - Tự động tạo giỏ hàng trống nếu chưa tồn tại
   - Xử lý việc cập nhật giỏ hàng hiện có thay vì tạo mới
   - Thêm import cần thiết

3. **Thêm CORS Support** (`src/main/java/com/example/bikestore/bikestore/Controllers/CartController.java`):
   - Thêm `@CrossOrigin(origins = "*")` để cho phép frontend kết nối

4. **Cải thiện Frontend API** (`fontend/js/api.js`):
   - Đảm bảo cart items có cả `id` và `productId` để tương thích
   - Tự động mapping giữa các field

## Cách Test Chức Năng

### 1. Khởi Động Backend
```bash
cd "bikestore V4/bikestore (1)/bikestore"
mvn spring-boot:run
```

### 2. Mở File Test
Mở file `test_cart.html` trong trình duyệt để test chức năng giỏ hàng.

### 3. Test Các Chức Năng
- **Test Thêm Sản Phẩm**: Click nút "Test Thêm Sản Phẩm" để thêm sản phẩm test vào giỏ hàng
- **Test Lấy Giỏ Hàng**: Click nút "Test Lấy Giỏ Hàng" để xem nội dung giỏ hàng

### 4. Test Trên Website Thực
1. Mở `fontend/html/Trang ds sp.html`
2. Click nút "Thêm vào giỏ hàng" trên bất kỳ sản phẩm nào
3. Kiểm tra xem sản phẩm có được thêm vào giỏ hàng không
4. Mở `fontend/html/Trang giỏ hàng.html` để xem giỏ hàng

## Cấu Trúc Dữ Liệu Cart

### Frontend Gửi:
```javascript
{
  userId: "user123",
  items: [
    {
      id: "product1",
      name: "Product Name", 
      price: 1000000,
      image: "image.jpg",
      quantity: 1
    }
  ]
}
```

### Backend Nhận và Lưu:
```javascript
{
  id: "cartId",
  userId: "user123", 
  items: [
    {
      id: "product1",           // Tương thích frontend
      productId: "product1",    // Tương thích backend
      name: "Product Name",
      price: 1000000,
      image: "image.jpg", 
      quantity: 1
    }
  ]
}
```

## Lưu Ý Quan Trọng

1. **Backend phải đang chạy** trên port 8080 để frontend có thể kết nối
2. **CORS đã được cấu hình** để cho phép frontend kết nối từ localhost
3. **Dữ liệu cart được lưu trong MongoDB** với collection "carts"
4. **Guest users** cũng có thể sử dụng giỏ hàng với ID tạm thời

## Troubleshooting

Nếu vẫn gặp lỗi:

1. **Kiểm tra Console**: Mở Developer Tools (F12) và xem tab Console để kiểm tra lỗi JavaScript
2. **Kiểm tra Network**: Xem tab Network để kiểm tra các request API
3. **Kiểm tra Backend Logs**: Xem console của Spring Boot để kiểm tra lỗi backend
4. **Kiểm tra MongoDB**: Đảm bảo MongoDB đang chạy và có thể kết nối

## Kết Quả Mong Đợi

Sau khi sửa lỗi:
- ✅ Có thể thêm sản phẩm vào giỏ hàng từ trang danh sách sản phẩm
- ✅ Giỏ hàng hiển thị đúng sản phẩm đã thêm
- ✅ Có thể cập nhật số lượng sản phẩm trong giỏ hàng
- ✅ Có thể xóa sản phẩm khỏi giỏ hàng
- ✅ Số lượng giỏ hàng hiển thị đúng trên header
