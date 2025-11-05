import service.BookingService;
import service.CustomerService;
import service.ReportService;
import service.TourService;
import ui.*;

public class Main {
    public static void main(String[] args){
// --- 1. KHỞI TẠO TẦNG SERVICE (Logic và Dữ liệu) ---

        // Khởi tạo Service cơ bản (không phụ thuộc vào ai)
        TourService tourService = new TourService();
        CustomerService customerService = new CustomerService();

        // Khởi tạo Service phụ thuộc (Booking phụ thuộc Tour)
        BookingService bookingService = new BookingService(tourService);

        // Khởi tạo Service báo cáo (phụ thuộc cả 3 Service CRUD)
        ReportService reportService = new ReportService(bookingService, customerService, tourService);

        // --- 2. LIÊN KẾT CHÉO (Dependency Injection) ---

        // Inject BookingService vào CustomerService để kiểm tra ràng buộc xóa
        customerService.setBookingService(bookingService);


        // --- 3. KHỞI TẠO TẦNG UI (Giao diện) ---

        // Khởi tạo UI cơ bản (Cần Service tương ứng)
        CustomerUI customerUI = new CustomerUI(customerService);
        TourUI tourUI = new TourUI(tourService);

        // Khởi tạo UI phức tạp (Cần Service và các UI khác)
        BookingUI bookingUI = new BookingUI(bookingService, tourService, customerService, customerUI, tourUI);
        ReportUI reportUI = new ReportUI(reportService, bookingService, customerService, tourService, tourUI);


        // --- 4. VÒNG LẶP ĐIỀU KHIỂN CHÍNH ---

        int choice;
        do {
            showMainMenu();
            choice = UserInputHandler.getIntInput("Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1: tourUI.showMenu(); break; // Gọi menu Tour
                case 2: customerUI.showMenu(); break; // Gọi menu Customer
                case 3: bookingUI.showMenu(); break; // Gọi menu Booking
                case 4: reportUI.showMenu(); break; // Gọi menu Report
                case 0:
                    saveAllData(tourService, customerService, bookingService);
                    System.out.println("Đang thoát chương trình...");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        } while (choice != 0);

        // Đóng Scanner
        UserInputHandler.closeScanner();
    }

    // Hiển thị Menu Chính
    private static void showMainMenu() {
        System.out.println("\n======== HỆ THỐNG QUẢN LÝ TOUR DU LỊCH ========");
        System.out.println("1. Quản lý Tour");
        System.out.println("2. Quản lý Khách hàng");
        System.out.println("3. Quản lý Đặt Tour");
        System.out.println("4. Xem Báo cáo");
        System.out.println("0. Lưu và Thoát");
        System.out.println("==============================================");
    }

    // Hàm lưu tất cả dữ liệu
    private static void saveAllData(TourService ts, CustomerService cs, BookingService bs) {
        ts.saveChanges();
        cs.saveChanges();
        bs.saveChanges();
        System.out.println("Đã lưu tất cả dữ liệu thành công!");
    }
}
