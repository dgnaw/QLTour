package service;

import exception.CustomerNotFoundException;
import model.Customer;
import repository.GenericRepository;

import java.util.List;

public class CustomerService {
    //Khai báo Repo và List
    private final GenericRepository<Customer> customerRepository;
    private final List<Customer> customers;
    //Tên file chứa dữ liệu
    private static final String FILE_NAME = "customers.dat";

    //Constructor
    public CustomerService() {
        this.customerRepository = new GenericRepository<>(FILE_NAME);
        this.customers = this.customerRepository.load();
        
        //Cập nhật ID để tránh trùng
        updateNextIdAfterLoad();
    }

    private void updateNextIdAfterLoad() {
        int maxId = 0;
        for(Customer customer : customers) {
            if(customer.getId() > maxId) {
                maxId = customer.getId();
            }
        }
        Customer.updateNextId(maxId);
    }

    //CRUD
    //Read
    //Lấy toàn bộ danh sách khách hàng
    public List<Customer> getAllCustomers() {
        return this.customers;
    }

    //Tìm khách hàng theo id
    public Customer findCustomerById(int id) throws CustomerNotFoundException {
        for(Customer customer : this.customers) {
            if(customer.getId() == id) {
                return customer;
            }
        }
        //Không return -> không thấy khách hàng
        throw new CustomerNotFoundException("Khong thay khach hang voi id " + id);
    }

    //Tìm khách hàng theo email
    public Customer findCustomerByEmail(String email) throws CustomerNotFoundException {
        for(Customer customer : this.customers) {
            if(customer.getEmail() != null && customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }
        //Không tìm thấy
        throw new CustomerNotFoundException("Khong thay khach hang voi email " + email);
    }

    //Create
    public void createCustomer(String name, String email, String sdt, String address) {
        Customer newCustomer = new Customer(name, email, sdt, address);
        //Thêm khách hàng vào danh sách
        this.customers.add(newCustomer);
        this.customerRepository.save(this.customers);
    }

    //Update
    public void updateCustomer(int id, String name, String email, String sdt, String address) throws CustomerNotFoundException {
        //Tìm với id
        Customer customerToUpdate = findCustomerById(id);
        //Cập nhật dữ liệu mới
        customerToUpdate.setName(name);
        customerToUpdate.setEmail(email);
        customerToUpdate.setSdt(sdt);
        customerToUpdate.setAddress(address);
        //Lưu lại
        customerRepository.save(this.customers);
    }

    //Delete
    public void deleteCustomer(int id) throws CustomerNotFoundException {
        Customer customerToDelete = findCustomerById(id);
        
        this.customers.remove(customerToDelete);
        this.customerRepository.save(this.customers);
    }
}