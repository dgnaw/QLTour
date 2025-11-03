package service;

import exception.TourNotFoundException;
import model.Customer;
import model.Booking;
import exception.BookingNotFoundException;
import exception.CustomerNotFoundException;
import model.TourPackage;

import java.util.ArrayList;
import java.util.List;

public class ReportService {
    //Khai báo các Service
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final TourService tourService;

    //Constructor
    public ReportService(BookingService bookingService, CustomerService customerService, TourService tourService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.tourService = tourService;
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
    public double getTotalRevenueForTour(int tourId) throws TourNotFoundException {

        TourPackage tour = tourService.findTourById(tourId);
        double pricePerPax = tour.getPrice();
        double totalRevenue = 0.0;

        List<Booking> allBookings = this.bookingService.getAllBookings();
        //Duyệt qua tất cả booking
        for(Booking booking : allBookings) {
            if(booking.getTourId() == tourId) {
                //Tính doanh thu từ booking
                totalRevenue += (booking.getNumberOfPax() * pricePerPax);
            }
        }
        
        return totalRevenue;
    }

    //Tính tổng doanh thu cho tất cả tour
    public double getTotalRevenue() throws TourNotFoundException{
        double totalRevenue = 0.0;
        List<TourPackage> allTours = tourService.getAllTours();

        //Duyệt qua tất cả booking
        for(TourPackage tour : allTours) {
            totalRevenue += getTotalRevenueForTour(tour.getId());
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