package ui;

import model.Customer;
import service.CustomerService;
import exception.CustomerNotFoundException;

import java.util.List;
import java.util.Scanner;

public class CustomerUI {
    private final CustomerService customerService;

    private int getCustomerIdInput() {
        try {
            return UserInputHandler.getIntInput("Nhập ID khách hàng: ");
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID không hợp lệ. Vui lòng nhập số.");
            return -1; // Trả về giá trị không hợp lệ
        }
    }
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
            System.out.println("5. Tìm theo ID");
            System.out.println("0. Quay lại");

            // Sử dụng UserInputHandler
            choice = UserInputHandler.getIntInput("Chọn: ");

            switch (choice) {
                case 1:
                    handleListCustomers();
                    break;
                case 2:
                    handleAddCustomer();
                    break;
                case 3:
                    handleUpdateCustomer();
                    break;
                case 4:
                    handleDeleteCustomer();
                    break;
                case 5:
                    handleFindById();
                    break;
                case 0:
                    System.out.println("...Trở về menu chính.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        } while (choice != 0);
    }

    // Đổi tên hàm cho nhất quán
    public void handleListCustomers() {
        System.out.println("\n--- Danh sách khách hàng ---");
        List<Customer> all = customerService.getAllCustomers(); // Giả định Service có hàm getAllCustomers
        if (all.isEmpty()) {
            System.out.println("Chưa có khách hàng nào.");
            return;
        }
        all.forEach(System.out::println);
    }

    private void handleAddCustomer() {
        System.out.println("\n--- Thêm khách hàng ---");
        String name = UserInputHandler.getStringInput("Tên: ");
        String email = UserInputHandler.getStringInput("Email: ");
        String sdt = UserInputHandler.getStringInput("SĐT: ");
        String address = UserInputHandler.getStringInput("Địa chỉ: ");

        customerService.createCustomer(name.trim(), email.trim(), sdt.trim(), address.trim());
        System.out.println("Đã thêm khách hàng mới.");
    }

    private void handleUpdateCustomer() {
        System.out.println("\n--- Sửa khách hàng ---");

        int id = getCustomerIdInput();
        if (id == -1) return;

        try {
            Customer existing = customerService.findCustomerById(id);
            System.out.println("Thông tin hiện tại: " + existing);

            // --- BƯỚC 1: THU THẬP INPUTS ---
            String nameInput = UserInputHandler.getStringInput("Tên mới (Enter để giữ nguyên: " + existing.getName() + "): ");
            String emailInput = UserInputHandler.getStringInput("Email mới (Enter để giữ nguyên: " + existing.getEmail() + "): ");
            String sdtInput = UserInputHandler.getStringInput("SĐT mới (Enter để giữ nguyên: " + existing.getSdt() + "): ");
            String addressInput = UserInputHandler.getStringInput("Địa chỉ mới (Enter để giữ nguyên: " + existing.getAddress() + "): ");

            // --- BƯỚC 2: XỬ LÝ LOGIC GIỮ LẠI GIÁ TRỊ CŨ ---

            String newName = resolveNewValue(nameInput, existing.getName());
            String newEmail = resolveNewValue(emailInput, existing.getEmail());
            String newSdt = resolveNewValue(sdtInput, existing.getSdt());
            String newAddress = resolveNewValue(addressInput, existing.getAddress());

            // --- BƯỚC 3: GỌI SERVICE ĐỂ LƯU THAY ĐỔI ---

            customerService.updateCustomer(id, newName, newSdt, newEmail, newAddress);
            System.out.println("Cập nhật thành công.");

        } catch (CustomerNotFoundException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
    // ------------------------------------------------------------------
// Bắt buộc phải thêm hàm helper này (hoặc nội tuyến)
    private String resolveNewValue(String newValueInput, String existingValue) {
        // Trả về giá trị cũ nếu chuỗi input mới (sau khi cắt khoảng trắng) là rỗng
        if (newValueInput != null && newValueInput.trim().isEmpty()) {
            return existingValue;
        }
        // Nếu có giá trị mới, trả về giá trị mới đã được cắt khoảng trắng
        return newValueInput.trim();
    }

    private void handleDeleteCustomer() {
        System.out.println("\n--- Xóa khách hàng ---");

        int id = getCustomerIdInput();
        if (id == -1) return;

        try {
            Customer c = customerService.findCustomerById(id);
            System.out.println("Thông tin khách hàng: " + c);

            String conf = UserInputHandler.getStringInput("Xác nhận xóa (y/N): ");
            if (!"y".equalsIgnoreCase(conf.trim())) {
                System.out.println("Hủy thao tác.");
                return;
            }

            customerService.deleteCustomer(id);
            System.out.println("Đã xóa khách hàng.");

        } catch (CustomerNotFoundException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void handleFindById() {
        System.out.println("\n--- Tìm khách hàng theo ID ---");

        int id = getCustomerIdInput();
        if (id == -1) return;

        try {
            Customer c = customerService.findCustomerById(id);
            System.out.println("Tìm thấy: " + c);
        } catch (CustomerNotFoundException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}