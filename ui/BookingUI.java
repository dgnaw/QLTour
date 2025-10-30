// Đặt tại: src/ui/BookingUI.java
package ui;

import service.BookingService;
import service.TourService;
import service.CustomerService;
import exception.BookingNotFoundException;
import exception.TourNotFoundException;
import exception.TourFullException;
import exception.CustomerNotFoundException;

public class BookingUI {

    private final BookingService bookingService;
    private final TourService tourService;
    private final CustomerService customerService;
    private final CustomerUI customerUI;
    private final TourUI tourUI;

    // Constructor: Nhận tất cả các Service và UI liên quan
    public BookingUI(BookingService bookingService, TourService tourService, CustomerService customerService, CustomerUI customerUI, TourUI tourUI) {
        this.bookingService = bookingService;
        this.tourService = tourService;
        this.customerService = customerService;
        this.customerUI = customerUI;
        this.tourUI = tourUI;
    }

    public void showBookingMenu() {
        int choice;
        do {
            System.out.println("\n--- Quản lý Đặt Tour ---");
            System.out.println("1. Tạo Đặt Tour Mới");
            System.out.println("2. Hủy Đặt Tour");
            System.out.println("3. Xem danh sách Đặt Tour");
            System.out.println("0. Quay lại Menu chính");
            choice = UserInputHandler.getIntInput("Lựa chọn: ");
            switch (choice) {
                case 1: createBooking(); break;
                case 2: cancelBooking(); break;
                case 3: listAllBookings(); break;
            }
        } while (choice != 0);
    }

    private void createBooking() {
        System.out.println("\n--- Tạo Đặt Tour Mới ---");

        // Hiển thị danh sách để người dùng chọn
        customerUI.listAllCustomers();
        int customerId = UserInputHandler.getIntInput("Nhập ID của khách hàng (người đứng ra đặt): ");

        tourUI.listAllTours();
        int tourId = UserInputHandler.getIntInput("Nhập ID của tour muốn đặt: ");

        int numberOfPax = UserInputHandler.getIntInput("Nhập số lượng người đi: ");
        double deposit = UserInputHandler.getDoubleInput("Nhập số tiền đặt cọc: ");

        try {
            // Kiểm tra khách hàng có tồn tại không (qua CustomerService)
            customerService.getCustomerById(customerId);

            // Gọi BookingService để xử lý logic chính
            bookingService.createBooking(tourId, customerId, deposit, numberOfPax);

            System.out.println("Đặt tour thành công!");
        } catch (CustomerNotFoundException | TourNotFoundException | TourFullException e) {
            System.out.println("Lỗi đặt tour: " + e.getMessage());
        }
    }

    private void cancelBooking() {
        listAllBookings();
        int bookingId = UserInputHandler.getIntInput("Nhập ID booking cần hủy: ");
        try {
            bookingService.cancelBooking(bookingId);
            System.out.println("Hủy booking thành công! Số chỗ đã được hoàn lại.");
        } catch (BookingNotFoundException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (TourNotFoundException e) {
            System.out.println("Lỗi nghiêm trọng: " + e.getMessage());
        }
    }

    private void listAllBookings() {
        System.out.println("\n--- Danh sách tất cả Booking ---");
        bookingService.getAllBookings().forEach(System.out::println);
        System.out.println("--------------------------------");
    }
}