
package ui;

import model.Customer;
import service.CustomerService;
import exception.CustomerNotFoundException;

import java.util.List;

public class CustomerUI {

    private final CustomerService customerService;

    // Constructor
    public CustomerUI(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n--- Quản lý Khách hàng ---");
            System.out.println("1. Danh sách khách hàng");
            System.out.println("2. Thêm khách hàng");
            System.out.println("3. Sửa khách hàng");
            System.out.println("4. Xóa khách hàng");
            System.out.println("5. Tìm khách hàng theo ID");
            System.out.println("6. Tìm khách hàng theo Email");
            System.out.println("0. Quay lại Menu chính");
            choice = UserInputHandler.getIntInput("Lựa chọn: ");

            switch (choice) {
                case 1: handleListCustomers(); break;
                case 2: handleAddCustomer(); break;
                case 3: handleUpdateCustomer(); break;
                case 4: handleDeleteCustomer(); break;
                case 5: handleFindById(); break;
                case 6: handleFindByEmail(); break;
                case 0: System.out.println("...Trở về menu chính"); break;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    // 1. Hiển thị danh sách khách hàng
    public void handleListCustomers() {
        System.out.println("\n--- Danh sách Khách hàng ---");
        List<Customer> allCustomers = customerService.getAllCustomers();

        if (allCustomers.isEmpty()) {
            System.out.println("Chưa có khách hàng nào.");
            return;
        }

        allCustomers.forEach(System.out::println);
        System.out.println("Tổng số khách hàng: " + allCustomers.size());
        System.out.println("-------------------------------");
    }

    // 2. Thêm khách hàng mới
    private void handleAddCustomer() {
        System.out.println("\n------Thêm Khách hàng Mới------");

        try {
            String name = UserInputHandler.getStringInput("Nhập tên: ");
            String email = UserInputHandler.getStringInput("Nhập email: ");
            String sdt = UserInputHandler.getStringInput("Nhập SĐT: ");
            String address = UserInputHandler.getStringInput("Nhập địa chỉ: ");

            // Validate dữ liệu
            if (name.trim().isEmpty()) {
                System.err.println("Lỗi: Tên không được để trống!");
                return;
            }

            customerService.createCustomer(name.trim(), email.trim(), sdt.trim(), address.trim());
            System.out.println("Thêm khách hàng thành công!");

        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 3. Sửa thông tin khách hàng
    private void handleUpdateCustomer() {
        System.out.println("\n------Sửa Thông tin Khách hàng------");

        try {
            handleListCustomers();
            int id = UserInputHandler.getIntInput("Nhập ID khách hàng cần sửa: ");

            // Tìm khách hàng
            Customer existing = customerService.findCustomerById(id);
            System.out.println("\nThông tin hiện tại: " + existing);

            // Thu thập thông tin mới (Enter để giữ nguyên)
            String nameInput = UserInputHandler.getStringInput(
                    "Tên mới (Enter để giữ nguyên: " + existing.getName() + "): ");
            String emailInput = UserInputHandler.getStringInput(
                    "Email mới (Enter để giữ nguyên: " + existing.getEmail() + "): ");
            String sdtInput = UserInputHandler.getStringInput(
                    "SĐT mới (Enter để giữ nguyên: " + existing.getSdt() + "): ");
            String addressInput = UserInputHandler.getStringInput(
                    "Địa chỉ mới (Enter để giữ nguyên: " + existing.getAddress() + "): ");

            // Xử lý logic giữ lại giá trị cũ nếu không nhập gì
            String newName = resolveNewValue(nameInput, existing.getName());
            String newEmail = resolveNewValue(emailInput, existing.getEmail());
            String newSdt = resolveNewValue(sdtInput, existing.getSdt());
            String newAddress = resolveNewValue(addressInput, existing.getAddress());

            // Cập nhật
            customerService.updateCustomer(id, newName, newEmail, newSdt, newAddress);
            System.out.println("Cập nhật khách hàng thành công!");

        } catch (CustomerNotFoundException e) {
            System.err.println("Lỗi: Khách hàng không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // Hàm helper: Xử lý giá trị mới (nếu rỗng thì giữ giá trị cũ)
    private String resolveNewValue(String newValueInput, String existingValue) {
        if (newValueInput == null || newValueInput.trim().isEmpty()) {
            return existingValue;
        }
        return newValueInput.trim();
    }

    // 4. Xóa khách hàng
    private void handleDeleteCustomer() {
        System.out.println("\n------Xóa Khách hàng------");

        try {
            handleListCustomers();
            int id = UserInputHandler.getIntInput("Nhập ID khách hàng cần xóa: ");

            // Tìm khách hàng
            Customer customer = customerService.findCustomerById(id);
            System.out.println("\nThông tin khách hàng: " + customer);

            // Xác nhận xóa
            String confirm = UserInputHandler.getStringInput("Xác nhận xóa (y/N): ");
            if (!"y".equalsIgnoreCase(confirm.trim())) {
                System.out.println("Hủy thao tác xóa.");
                return;
            }

            customerService.deleteCustomer(id);
            System.out.println("Xóa khách hàng thành công!");

        } catch (CustomerNotFoundException e) {
            System.err.println("Lỗi: Khách hàng không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 5. Tìm khách hàng theo ID
    private void handleFindById() {
        System.out.println("\n------Tìm Khách hàng theo ID------");

        try {
            int id = UserInputHandler.getIntInput("Nhập ID khách hàng: ");
            Customer customer = customerService.findCustomerById(id);

            System.out.println("\n--- Thông tin Khách hàng ---");
            System.out.println(customer);
            System.out.println("----------------------------");

        } catch (CustomerNotFoundException e) {
            System.err.println("Lỗi: Khách hàng không tồn tại. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }

    // 6. Tìm khách hàng theo Email
    private void handleFindByEmail() {
        System.out.println("\n------Tìm Khách hàng theo Email------");

        try {
            String email = UserInputHandler.getStringInput("Nhập email khách hàng: ");

            if (email.trim().isEmpty()) {
                System.err.println("Lỗi: Email không được để trống!");
                return;
            }

            Customer customer = customerService.findCustomerByEmail(email.trim());

            System.out.println("\n--- Thông tin Khách hàng ---");
            System.out.println(customer);
            System.out.println("----------------------------");

        } catch (CustomerNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy khách hàng với email này. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }
}