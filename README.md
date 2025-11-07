# Hệ thống Quản lý Tour Du lịch
Đây là một ứng dụng console (dòng lệnh) được xây dựng bằng ngôn ngữ Java để quản lý các hoạt động của một công ty du lịch nhỏ. Ứng dụng cho phép quản lý thông tin về các gói tour, khách hàng, xử lý việc đặt tour và tạo các báo cáo thống kê đơn giản.

Dữ liệu của ứng dụng được lưu trữ bền vững giữa các phiên làm việc bằng cách sử dụng cơ chế Java Serialization để ghi và đọc đối tượng từ file.

## Tính năng Chính
### Quản lý Tour:
- Thêm một gói tour du lịch mới (tên, lịch trình, giá, số chỗ).
- Sửa thông tin của một tour đã có.
- Xóa một tour khỏi hệ thống.
- Hiển thị danh sách tất cả các tour.
### Quản lý Khách hàng:
- Thêm thông tin khách hàng mới (tên, email, SĐT).
- Hiển thị danh sách tất cả khách hàng.
### Đặt Tour:
- Cho phép một khách hàng đặt một tour du lịch.
- Hệ thống tự động kiểm tra xem tour có còn chỗ trống hay không trước khi xác nhận.
### Báo cáo và Thống kê:
- Hiển thị danh sách tất cả khách hàng đã đặt một tour cụ thể.
- Liệt kê các tour sắp khởi hành (so với ngày hiện tại).
- Tính toán và hiển thị tổng doanh thu từ một tour cụ thể.
### Lưu trữ Dữ liệu:
- Toàn bộ dữ liệu về tour, khách hàng và các lượt đặt tour sẽ được tự động lưu vào các file .dat khi thoát chương trình và được tải lên khi khởi động.

## Kiến trúc Dự án
Dự án được xây dựng theo kiến trúc 3-Lớp (3-Tier Architecture) để đảm bảo sự tách biệt rõ ràng giữa các thành phần, giúp mã nguồn dễ bảo trì, dễ mở rộng và phù hợp cho việc làm việc nhóm.

- UI Layer (ui): Lớp giao diện người dùng, chịu trách nhiệm hiển thị menu và nhận dữ liệu đầu vào.
- Service Layer (service): Lớp xử lý logic nghiệp vụ, là bộ não của ứng dụng.
- Repository Layer (repository): Lớp truy cập dữ liệu, chịu trách nhiệm đọc và ghi dữ liệu từ file.
```text
/QLTourDL
      ├── exception/  (Chứa các lớp ngoại lệ tùy chỉnh)
      ├── model/      (Các lớp định nghĩa đối tượng: Tour, Customer, Booking)
      ├── repository/ (Lớp đọc/ghi file dữ liệu)
      ├── service/    (Các lớp xử lý logic nghiệp vụ)
      ├── ui/         (Các lớp xử lý giao diện dòng lệnh)
      └── Main.java   (Điểm khởi đầu của chương trình)
```
## Công nghệ sử dụng
- Ngôn ngữ: Java 11+ 
- Lưu trữ: File I/O với Java Serialization 
- Kiến trúc: 3-Lớp (UI, Service, Repository)
- Nguyên tắc thiết kế: Lập trình Hướng đối tượng (OOP), SOLID (đặc biệt là Nguyên tắc Đơn trách nhiệm)

## Hướng dẫn Cài đặt và Chạy
Yêu cầu:
- JDK (Java Development Kit) phiên bản 11 trở lên.
- Git.

Các bước thực hiện:
1. Clone repository về máy của bạn:
```text
git clone <URL_repository_cua_ban>
cd <ten_thu_muc_du_an>
```
2. Biên dịch dự án:
Mở Terminal hoặc Command Prompt và chạy lệnh sau:
```text
javac src/*.java src/exception/*.java src/model/*.java src/repository/*.java src/service/*.java src/ui/*.java
```
3. Chạy chương trình:
Sau khi biên dịch thành công, vẫn đứng từ thư mục gốc QLTourDL, chạy lệnh java sau:
```text
java -cp src Main
```
Chương trình sẽ khởi chạy và hiển thị menu chính trên giao diện dòng lệnh.

Lưu ý: Các file dữ liệu (tours.dat, customers.dat, bookings.dat) sẽ được tự động tạo ở thư mục gốc của dự án (ngang hàng với thư mục src).

## Mô tả Cấu trúc Mã nguồn
- model: Chứa các lớp POJO (Plain Old Java Object) định nghĩa cấu trúc dữ liệu.
  + TourPackage.java: Định nghĩa đối tượng Gói Tour.
  + Customer.java: Định nghĩa đối tượng Khách hàng.
  + Booking.java: Định nghĩa đối tượng Đặt tour, liên kết giữa Tour và Customer.

- repository: Chịu trách nhiệm lưu và tải dữ liệu.
  + GenericRepository.java: Lớp dùng chung có khả năng đọc/ghi bất kỳ danh sách đối tượng Serializable nào vào file, giúp tránh lặp code.

- service: Chứa toàn bộ logic nghiệp vụ của ứng dụng.
  + TourService.java: Xử lý các chức năng liên quan đến quản lý Gói Tour (thêm, sửa, xóa, tìm kiếm).
  + CustomerService.java: Quản lý thông tin Khách hàng và các thao tác CRUD tương ứng.
  + BookingService.java: Xử lý logic phức tạp như kiểm tra điều kiện và tạo một lượt đặt tour mới.
  + ReportService.java: Tổng hợp và thống kê dữ liệu, ví dụ như báo cáo doanh thu, số lượt đặt tour, hoặc danh sách khách hàng theo tour.

- ui: Chịu trách nhiệm về giao diện người dùng.
  + UserInputHandler.java: Lớp tiện ích để xử lý và xác thực mọi loại input từ người dùng một cách an toàn.
  + TourUI.java, CustomerUI.java, BookingUI.java, ReportUI.java: Mỗi lớp chịu trách nhiệm cho một menu chức năng riêng biệt, giúp lớp Main trở nên gọn gàng.

- Main.java: Điểm vào của ứng dụng.
  + Nhiệm vụ chính là khởi tạo các đối tượng Service và UI, sau đó điều hướng người dùng đến các menu chức năng tương ứng.

## Tác giả
Dự án được phát triển bởi:
- Đỗ Gia Nam
- Bùi Đình Hiếu
- Nguyễn Thế Hiệp