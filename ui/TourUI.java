package ui;

import model.TourPackage;
import service.TourService;
import exception.TourNotFoundException; // case 3, 4

import java.time.LocalDate; // case 2
import java.time.format.DateTimeParseException; //  case 2
import java.util.List;



public class TourUI {
    // 1. Dependency Injection: Khai báo 2 thứ UI này CẦN để hoạt động
    // Nó không tự tạo ra, mà sẽ được "nhận" từ bên ngoài
    private final TourService tourService;

    // 2. Constructor: Đây là "cửa" để nhận 2 thứ trên
    public TourUI(TourService tourService){
        this.tourService = tourService;
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n------Tour Menu------");
            System.out.println("1. Hiển thị danh sách các gói Tour hiện có");
            System.out.println("2. Thêm gói Tour mới");
            System.out.println("3. Sửa gói Tour");
            System.out.println("4. Xóa gói Tour");
            System.out.println("0. Trở về menu chính");

            choice = UserInputHandler.getIntInput("Nhập vào lựa chọn của bạn: ");

            switch (choice) {
                case 1: {
                    handleShowTours();
                    break;
                }
                case 2: {
                    handleAddTour();
                    break;
                }
                case 3: {
                    handleUpdateTour();
                    break;
                }
                case 4: {
                    handleDeleteTour();
                    break;
                }
                case 0: {
                    // Gọi break lập tức
                    System.out.println("...Trở về menu chính");
                    break;
                }
                default: {
                    System.out.println("Lựa chọn không hợp lệ, vui lòng nhập lại!");
                }
            }
        } while (choice != 0);
    }

    public void handleShowTours() {
        System.out.println("\n------Danh sách các Tour hiện có------");
        List<TourPackage> tours = tourService.getAllTours();

        if(tours.isEmpty()) {
            System.out.print("Không có thông tin danh sách Tour (Trống)");
            return;
        }

        for(TourPackage tour : tours) {
            System.out.println(tour.toString());
        }
    }

    private void handleAddTour() {
        try {
            System.out.println("\n------Thêm Tour Mới------");
            String name = UserInputHandler.getStringInput("Nhập tên tour: ");
            String itinerary = UserInputHandler.getStringInput("Nhập lịch trình: ");
            double price = UserInputHandler.getDoubleInput("Nhập giá: ");
            int capacity = UserInputHandler.getIntInput("Nhập sức chứa tối đa: ");
            LocalDate startDate = UserInputHandler.getDateInput("Nhập ngày đi (dd/MM/yyyy): ");
            LocalDate endDate = UserInputHandler.getDateInput("Nhập ngày về (dd/MM/yyyy): ");
            // Validate ngày
            if (endDate.isBefore(startDate)) {
                System.err.println("Lỗi: Ngày về phải sau hoặc bằng ngày đi. Thao tác thêm tour đã bị hủy.");
                return; // Dừng hàm, không tạo tour nữa
            }

            // Gọi service để tạo
            tourService.createTour(name, itinerary, price, capacity, startDate, endDate);
            System.out.println("Thêm tour thành công!");
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private void handleUpdateTour() {
        try {
            System.out.println("\n------Cập Nhật Tour------");
            int id = UserInputHandler.getIntInput("Nhập ID tour cần sửa: ");
            // Lấy đối tượng tour
            TourPackage tourToUpdate = tourService.findTourById(id);

            int choice;
            do {
                System.out.println("\n------Đang sửa tour '" + tourToUpdate.getTourName() + "'------");
                System.out.println("Thông tin hiện tại: " + tourToUpdate.toString());
                System.out.println("Hãy chọn trường muốn sửa!");
                System.out.println("1. Sửa tên");
                System.out.println("2. Sửa lịch trình");
                System.out.println("3. Sửa giá");
                System.out.println("4. Sửa sức chứa");
                System.out.println("5. Sửa ngày đi");
                System.out.println("6. Sửa ngày về");
                System.out.println("0. Hoàn tất và Lưu thay đổi");

                choice = UserInputHandler.getIntInput("Nhập vào lựa chọn của bạn");

                switch (choice) {
                    case 1:
                        String newName = UserInputHandler.getStringInput("Nhập tên mới: ");
                        tourToUpdate.setTourName(newName); // 2. Cập nhật trực tiếp
                        System.out.println("Đã cập nhật tên!");
                        break;
                    case 2:
                        String newItinerary = UserInputHandler.getStringInput("Nhập lịch trình mới: ");
                        tourToUpdate.setItinerary(newItinerary); // 2. Cập nhật trực tiếp
                        System.out.println("Đã cập nhật lịch trình!");
                        break;
                    case 3:
                        double newPrice = UserInputHandler.getDoubleInput("Nhập giá mới: ");
                        tourToUpdate.setPrice(newPrice); // 2. Cập nhật trực tiếp
                        System.out.println("Đã cập nhật giá!");
                        break;
                    case 4:
                        int newCapacity = UserInputHandler.getIntInput("Nhập sức chứa mới: ");
                        int currentBooked = tourToUpdate.getBookedCapacity(); // Lấy số chỗ đã đặt

                        if (newCapacity < currentBooked) {
                            System.err.println("Lỗi: Sức chứa mới (" + newCapacity + ") không thể nhỏ hơn số chỗ đã được đặt (" + currentBooked + ").");
                        } else {
                            tourToUpdate.setMaxCapacity(newCapacity);
                            System.out.println("Đã cập nhật sức chứa!");
                        }
                        break;
                    case 5:
                        LocalDate endDate = tourToUpdate.getEndDate();
                        LocalDate newStartDate = UserInputHandler.getDateInput("Nhập ngày đi mới (dd/MM/yyyy): ");

                        // Ngày đi mới không được phép SAU ngày về hiện tại
                        if (!newStartDate.isAfter(endDate)) {
                            tourToUpdate.setStartDate(newStartDate);
                            System.out.println("Đã cập nhật ngày đi");
                        } else {
                            System.err.println("Lỗi: Ngày đi phải trước hoặc bằng ngày về!");
                        }
                        break;
                    case 6:
                        LocalDate startDate = tourToUpdate.getStartDate();
                        LocalDate newEndDate = UserInputHandler.getDateInput("Nhập ngày về mới (dd/MM/yyyy): ");

                        // Ngày về mới không được phép TRƯỚC ngày đi hiện tại
                        if (!newEndDate.isBefore(startDate)) {
                            tourToUpdate.setEndDate(newEndDate);
                            System.out.println("Đã cập nhật ngày về");
                        } else {
                            System.err.println("Lỗi: Ngày về phải sau hoặc bằng ngày đi!");
                        }
                        break;
                    case 0:
                        // Gọi hàm saveChanges() khi người dùng hoàn tất
                        tourService.saveChanges();
                        System.out.println("Đã lưu tất cả thay đổi!");
                        break;
                    default:
                        System.err.println("Lựa chọn không hợp lệ.");
                }

            } while (choice != 0);
        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra: " + e.getMessage());
        }
    }

    private void handleDeleteTour() {
        try {
            System.out.println("\n------Xóa Tour------");
            handleShowTours();

            int id = UserInputHandler.getIntInput("Nhập id cần xóa: ");
            TourPackage tourToDelete = tourService.findTourById(id);
            System.out.println("Bạn có chắc muốn xóa tour sau?");
            System.out.println(tourToDelete.toString());
            String confirm = UserInputHandler.getStringInput("Nhập 'y' để xác nhận xóa hoặc nhập bất kỳ để hủy thao tác: ");

            if(confirm.equals("y")) {
                tourService.deleteTour(id);
                System.out.println("Xóa tour thành công");
            } else {
                System.out.println("Hủy xóa tour thành công");
            }
        } catch (TourNotFoundException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }
}
