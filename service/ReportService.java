package service;

import model.Customer;
import model.Booking;
import exception.BookingNotFoundException;
import exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ReportService {
    //Khai báo các Service
    private final BookingService bookingService;
    private final CustomerService customerService;

    //Constructor
    public ReportService(BookingService bookingService, CustomerService customerService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
    }

    //Báo cáo
    //Lấy danh sách khách hàng đã đặt tour theo id
    public List<Customer> getCustomersByTour(int tourId) {
        List<Customer> result = new ArrayList<>();
        List<Booking> allBookings = this.bookingService.getAllBookings();
        
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            if(booking.getTourId() == tourId) {
                try {
                    //Tìm khách hàng theo customerId
                    Customer customer = this.customerService.findCustomerById(booking.getCustomerId());
                    //Kiểm tra trùng lặp
                    if(!result.contains(customer)) {
                        result.add(customer);
                    }
                } catch (CustomerNotFoundException e) {
                    //Bỏ qua nếu không tìm thấy khách hàng
                    System.err.println("Canh bao: " + e.getMessage());
                }
            }
        }
        
        return result;
    }

    //Tính tổng doanh thu cho một tour
    public double getTotalRevenueForTour(int tourId) {
        double totalRevenue = 0.0;
        List<Booking> allBookings = this.bookingService.getAllBookings();
        
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            if(booking.getTourId() == tourId) {
                //Tính doanh thu từ booking
                totalRevenue += booking.getTotalPrice();
            }
        }
        
        return totalRevenue;
    }

    //Tính tổng doanh thu cho tất cả tour
    public double getTotalRevenue() {
        double totalRevenue = 0.0;
        List<Booking> allBookings = this.bookingService.getAllBookings();
        
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            totalRevenue += booking.getTotalPrice();
        }
        
        return totalRevenue;
    }

    //Lấy danh sách booking theo khách hàng
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> result = new ArrayList<>();
        List<Booking> allBookings = this.bookingService.getAllBookings();
        
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            if(booking.getCustomerId() == customerId) {
                result.add(booking);
            }
        }
        
        return result;
    }

    //Lấy danh sách booking theo tour
    public List<Booking> getBookingsByTour(int tourId) {
        List<Booking> result = new ArrayList<>();
        List<Booking> allBookings = this.bookingService.getAllBookings();
        
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            if(booking.getTourId() == tourId) {
                result.add(booking);
            }
        }
        
        return result;
    }

    //Đếm số lượng booking theo tour
    public int countBookingsByTour(int tourId) {
        int count = 0;
        List<Booking> allBookings = this.bookingService.getAllBookings();
        
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            if(booking.getTourId() == tourId) {
                count++;
            }
        }
        
        return count;
    }

    //Đếm số lượng khách hàng theo tour
    public int countCustomersByTour(int tourId) {
        List<Customer> customers = getCustomersByTour(tourId);
        return customers.size();
    }
}