package ui;

import model.Customer;
import service.CustomerService;
import exception.CustomerNotFoundException;

import java.util.List;
import java.util.Scanner;

public class CustomerUI {
    private final CustomerService customerService;
    private final Scanner scanner = new Scanner(System.in);

    public CustomerUI(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Quản lý Khách hàng ---");
            System.out.println("1. Danh sách khách hàng");
            System.out.println("2. Thêm khách hàng");
            System.out.println("3. Sửa khách hàng");
            System.out.println("4. Xóa khách hàng");
            System.out.println("5. Tìm theo ID");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listCustomers();
                    break;
                case "2":
                    addCustomer();
                    break;
                case "3":
                    updateCustomer();
                    break;
                case "4":
                    deleteCustomer();
                    break;
                case "5":
                    findById();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void listCustomers() {
        List<Customer> all = customerService.getAll();
        if (all.isEmpty()) {
            System.out.println("Chưa có khách hàng nào.");
            return;
        }
        all.forEach(c -> System.out.println(c.toString()));
    }

    private void addCustomer() {
        System.out.print("Tên: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("SĐT: ");
        String sdt = scanner.nextLine().trim();
        System.out.print("Địa chỉ: ");
        String address = scanner.nextLine().trim();

        Customer c = new Customer(name, email, sdt, address);
        customerService.addCustomer(c);
        System.out.println("Đã thêm. ID = " + c.getId());
    }

    private void updateCustomer() {
        try {
            System.out.print("Nhập ID khách hàng cần sửa: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Customer existing = customerService.findById(id);
            System.out.println("Thông tin hiện tại: " + existing);

            System.out.print("Tên mới (Enter để giữ nguyên): ");
            String name = scanner.nextLine();
            System.out.print("Email mới (Enter để giữ nguyên): ");
            String email = scanner.nextLine();
            System.out.print("SĐT mới (Enter để giữ nguyên): ");
            String sdt = scanner.nextLine();
            System.out.print("Địa chỉ mới (Enter để giữ nguyên): ");
            String address = scanner.nextLine();

            String newName = name == null ? existing.getName() : name.trim().isEmpty() ? existing.getName() : name.trim();
            String newEmail = email == null ? existing.getEmail() : email.trim().isEmpty() ? existing.getEmail() : email.trim();
            String newSdt = sdt == null ? existing.getSdt() : sdt.trim().isEmpty() ? existing.getSdt() : sdt.trim();
            String newAddress = address == null ? existing.getAddress() : address.trim().isEmpty() ? existing.getAddress() : address.trim();

            Customer updated = new Customer(newName, newEmail, newSdt, newAddress);
            customerService.updateCustomer(id, updated);
            System.out.println("Cập nhật thành công.");
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteCustomer() {
        try {
            System.out.print("Nhập ID khách hàng cần xóa: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Xác nhận xóa (y/N): ");
            String conf = scanner.nextLine().trim();
            if (!"y".equalsIgnoreCase(conf)) {
                System.out.println("Hủy thao tác.");
                return;
            }
            customerService.deleteCustomer(id);
            System.out.println("Đã xóa khách hàng.");
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findById() {
        try {
            System.out.print("Nhập ID khách hàng: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Customer c = customerService.findById(id);
            System.out.println(c);
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
