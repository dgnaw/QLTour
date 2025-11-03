// Đặt tại: src/ui/BookingUI.java
package ui;

import service.BookingService;
import service.TourService;
import service.CustomerService;
import exception.*;
import model.TourPackage;

public class BookingUI {

    private final BookingService bookingService;
    private final TourService tourService;
    private final CustomerService customerService;
    private final CustomerUI customerUI; // Dùng để hiển thị danh sách KH
    private final TourUI tourUI;       // Dùng để hiển thị danh sách Tour

    // Constructor: Nhận tất cả các Service và UI liên quan
    public BookingUI(BookingService bookingService, TourService tourService, CustomerService customerService, CustomerUI customerUI, TourUI tourUI) {
        this.bookingService = bookingService;
        this.tourService = tourService;
        this.customerService = customerService;
        this.customerUI = customerUI;
        this.tourUI = tourUI;
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n--- Quản lý Đặt Tour ---");
            System.out.println("1. Tạo Đặt Tour Mới");
            System.out.println("2. Hủy Đặt Tour");
            System.out.println("3. Xem danh sách Đặt Tour");
            System.out.println("0. Quay lại Menu chính");
            choice = UserInputHandler.getIntInput("Lựa chọn: ");
            switch (choice) {
                case 1: handleCreateBooking(); break;
                case 2: handleCancelBooking(); break;
                case 3: handleShowAllBookings(); break;
            }
        } while (choice != 0);
    }

    private void handleCreateBooking() {
        System.out.println("\n------Tạo Đặt Tour Mới------");

        try {
            // 1. Thu thập thông tin từ KH
            customerUI.handleListCustomers();
            int customerId = UserInputHandler.getIntInput("Nhập ID của khách hàng (người đặt): ");
            customerService.findCustomerById(customerId); // Kiểm tra tồn tại KH

            tourUI.handleShowTours();
            int tourId = UserInputHandler.getIntInput("Nhập ID của tour muốn đặt: ");

            // Lấy thông tin Tour để tính toán
            TourPackage tour = tourService.findTourById(tourId);

            int numberOfPax = UserInputHandler.getIntInput("Nhập số lượng người đi: ");

            // 2. Tính toán và hiển thị giá
            double pricePerPax = tour.getPrice();
            double totalPrice = pricePerPax * numberOfPax;

            System.out.println("-> Tổng tiền phải trả: " + String.format("%,.0f VND", totalPrice));
            double deposit = UserInputHandler.getDoubleInput("Nhập số tiền đặt cọc: ");

            // 3. Gọi Service để xử lý nghiệp vụ (kiểm tra chỗ, cập nhật tour, tạo booking)
            bookingService.createBooking(tourId, customerId, deposit, numberOfPax);

            System.out.println("Đặt tour thành công!");
        } catch (CustomerNotFoundException e) {
            System.err.println("Lỗi: Khách hàng không tồn tại. Vui lòng thêm khách hàng trước.");
        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: Tour không tồn tại. " + e.getMessage());
        } catch (TourFullException e) {
            System.err.println("Lỗi đặt tour: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    private void handleCancelBooking() {
        handleShowAllBookings();
        int bookingId = UserInputHandler.getIntInput("Nhập ID booking cần hủy: ");
        try {
            bookingService.cancelBooking(bookingId);
            System.out.println("Hủy booking thành công! Số chỗ đã được hoàn lại.");
        } catch (BookingNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy booking cần hủy.");
        } catch (TourNotFoundException e) {
            // Lỗi này xảy ra khi tourService không tìm thấy tour để trả lại chỗ
            System.err.println("Lỗi nghiêm trọng: Không tìm thấy tour liên kết. " + e.getMessage());
        }
    }

    private void handleShowAllBookings() {
        System.out.println("\n--- Danh sách tất cả Booking ---");
        if (bookingService.getAllBookings().isEmpty()) {
            System.out.println("Chưa có lượt đặt tour nào.");
            return;
        }
        bookingService.getAllBookings().forEach(System.out::println);
        System.out.println("--------------------------------");
    }
}