
package ui;

import service.ReportService;
import service.TourService;
import service.BookingService;
import service.CustomerService;
import exception.*;
import model.Customer;
import model.Booking;
import model.TourPackage;

import java.time.LocalDate;
import java.util.List;

public class ReportUI {

    private final ReportService reportService;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final TourService tourService;
    private final TourUI tourUI; // Dùng để hiển thị danh sách Tour

    // Constructor: Nhận tất cả các Service và UI liên quan
    public ReportUI(ReportService reportService,
                    BookingService bookingService,
                    CustomerService customerService,
                    TourService tourService,
                    TourUI tourUI) {
        this.reportService = reportService;
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.tourService = tourService;
        this.tourUI = tourUI;
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n--- Báo cáo & Thống kê ---");
            System.out.println("1. Danh sách khách hàng đã đặt một tour");
            System.out.println("2. Tổng doanh thu của một tour");
            System.out.println("3. Tổng doanh thu tất cả tour");
            System.out.println("4. Danh sách booking theo tour");
            System.out.println("5. Danh sách booking theo khách hàng");
            System.out.println("0. Quay lại Menu chính");
            choice = UserInputHandler.getIntInput("Lựa chọn: ");

            switch (choice) {
                case 1: handleCustomersByTour(); break;
                case 2: handleRevenueByTour(); break;
                case 3: handleTotalRevenue(); break;
                case 4: handleBookingsByTour(); break;
                case 5: handleBookingsByCustomer(); break;
                case 0: System.out.println("...Trở về menu chính"); break;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    // 1. Danh sách khách hàng đã đặt một tour
    private void handleCustomersByTour() {
        System.out.println("\n------Danh sách Khách hàng đã đặt Tour------");

        try {
            tourUI.handleShowTours();
            int tourId = UserInputHandler.getIntInput("Nhập ID tour: ");

            // Kiểm tra tour tồn tại
            TourPackage tour = tourService.findTourById(tourId);

            List<Customer> customers = reportService.getCustomersByTour(tourId);

            if (customers.isEmpty()) {
                System.out.println("Không có khách hàng nào đã đặt tour này.");
                return;
            }

            System.out.println("\n--- Khách hàng đã đặt Tour: " + tour.getTourName() + " ---");
            customers.forEach(System.out::println);
            System.out.println("Tổng số khách hàng: " + customers.size());

        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: Tour không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 2. Tổng doanh thu của một tour
    private void handleRevenueByTour() {
        System.out.println("\n------Doanh thu của Tour------");

        try {
            tourUI.handleShowTours();
            int tourId = UserInputHandler.getIntInput("Nhập ID tour: ");

            // Kiểm tra tour tồn tại
            TourPackage tour = tourService.findTourById(tourId);

            double totalRevenue = reportService.getTotalRevenueForTour(tourId);
            double totalDeposit = bookingService.getTotalDepositByTour(tourId);
            int totalBookings = reportService.countBookingsByTour(tourId);
            int totalPax = bookingService.getTotalBookedSeats(tourId);

            System.out.println("\n--- Báo cáo Doanh thu Tour: " + tour.getTourName() + " ---");
            System.out.println("Tổng số lượt đặt: " + totalBookings);
            System.out.println("Tổng số khách: " + totalPax + " người");
            System.out.println("Tổng tiền cọc đã thu: " + String.format("%,.0f VND", totalDeposit));
            System.out.println("Tổng doanh thu dự kiến: " + String.format("%,.0f VND", totalRevenue));
            System.out.println("--------------------------------------");

        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: Tour không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 3. Tổng doanh thu tất cả tour
    private void handleTotalRevenue() {
        System.out.println("\n------Tổng Doanh thu Tất cả Tour------");

        try {
            double totalRevenue = reportService.getTotalRevenue();
            List<TourPackage> allTours = tourService.getAllTours();

            if (allTours.isEmpty()) {
                System.out.println("Chưa có tour nào trong hệ thống.");
                return;
            }

            System.out.println("\n--- Báo cáo Tổng hợp ---");
            System.out.println("Tổng số tour: " + allTours.size());
            System.out.println("Tổng doanh thu tất cả tour: " + String.format("%,.0f VND", totalRevenue));
            System.out.println("--------------------------------------");

        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 4. Danh sách booking theo tour
    private void handleBookingsByTour() {
        System.out.println("\n------Danh sách Booking theo Tour------");

        try {
            tourUI.handleShowTours();
            int tourId = UserInputHandler.getIntInput("Nhập ID tour: ");

            // Kiểm tra tour tồn tại
            TourPackage tour = tourService.findTourById(tourId);

            List<Booking> bookings = reportService.getBookingsByTour(tourId);

            if (bookings.isEmpty()) {
                System.out.println("Chưa có booking nào cho tour này.");
                return;
            }

            System.out.println("\n--- Danh sách Booking của Tour: " + tour.getTourName() + " ---");
            bookings.forEach(System.out::println);
            System.out.println("Tổng số booking: " + bookings.size());
            System.out.println("--------------------------------------");

        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: Tour không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 5. Danh sách booking theo khách hàng
    private void handleBookingsByCustomer() {
        System.out.println("\n------Danh sách Booking theo Khách hàng------");

        try {
            int customerId = UserInputHandler.getIntInput("Nhập ID khách hàng: ");

            // Kiểm tra khách hàng tồn tại
            Customer customer = customerService.findCustomerById(customerId);

            List<Booking> bookings = reportService.getBookingsByCustomer(customerId);

            if (bookings.isEmpty()) {
                System.out.println("Khách hàng này chưa có booking nào.");
                return;
            }

            System.out.println("\n--- Danh sách Booking của: " + customer.getName() + " ---");
            bookings.forEach(System.out::println);
            System.out.println("Tổng số booking: " + bookings.size());
            System.out.println("--------------------------------------");

        } catch (CustomerNotFoundException e) {
            System.err.println("Lỗi: Khách hàng không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

}