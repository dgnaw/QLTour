package ui;

import service.ReportService;
import service.TourService;
import service.BookingService;
import service.CustomerService;
import model.Customer;
import model.Booking;
import model.TourPackage;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReportUI {
    private final ReportService reportService;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final TourService tourService;
    private final Scanner scanner = new Scanner(System.in);

    public ReportUI(ReportService reportService,
                    BookingService bookingService,
                    CustomerService customerService,
                    TourService tourService) {
        this.reportService = reportService;
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.tourService = tourService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Báo cáo & Thống kê ---");
            System.out.println("1. Danh sách khách hàng đã đặt một tour (theo ID tour)");
            System.out.println("2. Tổng doanh thu của một tour (theo ID tour)");
            System.out.println("3. Liệt kê các tour sắp khởi hành");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    customersByTour();
                    break;
                case "2":
                    revenueByTour();
                    break;
                case "3":
                    upcomingTours();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void customersByTour() {
        if (bookingService == null || customerService == null) {
            System.out.println("Dịch vụ booking hoặc customer chưa được khởi tạo.");
            return;
        }
        try {
            System.out.print("Nhập ID tour (số): ");
            int tourId = Integer.parseInt(scanner.nextLine().trim());
            List<Booking> bookings = bookingService.getAllBookings();
            List<Customer> customers = customerService.getAll();
            List<Customer> result = reportService.getCustomersByTour(tourId, bookings, customers);
            if (result.isEmpty()) {
                System.out.println("Không tìm thấy khách hàng nào đã đặt tour này.");
                return;
            }
            System.out.println("Danh sách khách hàng đã đặt tour " + tourId + ":");
            result.forEach(c -> System.out.println(c));
        } catch (NumberFormatException e) {
            System.out.println("ID tour không hợp lệ.");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void revenueByTour() {
        if (bookingService == null) {
            System.out.println("Dịch vụ booking chưa được khởi tạo.");
            return;
        }
        try {
            System.out.print("Nhập ID tour (số): ");
            int tourId = Integer.parseInt(scanner.nextLine().trim());
            List<Booking> bookings = bookingService.getAllBookings();
            double total = reportService.totalRevenueForTour(tourId, bookings);
            System.out.printf("Tổng doanh thu cho tour %d: %.2f%n", tourId, total);
        } catch (NumberFormatException e) {
            System.out.println("ID tour không hợp lệ.");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void upcomingTours() {
        if (tourService == null) {
            System.out.println("Dịch vụ tour chưa được khởi tạo.");
            return;
        }
        List<TourPackage> tours = tourService.getAllTours();
        if (tours == null || tours.isEmpty()) {
            System.out.println("Không có tour nào.");
            return;
        }

        LocalDate today = LocalDate.now();
        List<TourPackage> upcoming = tours.stream()
                .filter(t -> {
                    LocalDate d = extractDate(t);
                    return d != null && (d.isEqual(today) || d.isAfter(today));
                })
                .collect(Collectors.toList());

        if (upcoming.isEmpty()) {
            System.out.println("Không có tour sắp khởi hành.");
            return;
        }

        System.out.println("Danh sách tour sắp khởi hành:");
        upcoming.forEach(t -> System.out.println(t));
    }

    // Attempts to extract a start/departure date from TourPackage via common getters
    private LocalDate extractDate(Object tour) {
        if (tour == null) return null;
        String[] candidates = {"getStartDate", "getDepartureDate", "getDate", "getStart"};
        for (String mName : candidates) {
            try {
                Method m = tour.getClass().getMethod(mName);
                Object r = m.invoke(tour);
                if (r == null) continue;
                if (r instanceof LocalDate) return (LocalDate) r;
                if (r instanceof Date) {
                    Instant ins = ((Date) r).toInstant();
                    return ins.atZone(ZoneId.systemDefault()).toLocalDate();
                }
                if (r instanceof String) {
                    try {
                        return LocalDate.parse((String) r);
                    } catch (DateTimeParseException ignored) {
                    }
                }
            } catch (NoSuchMethodException ignored) {
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
