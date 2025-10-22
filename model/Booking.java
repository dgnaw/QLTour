package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking implements Serializable {
    private static final long serialVersionUID = 3L;
    private static int nextId = 1;
    private int id, customerId, tourId;
    private LocalDateTime bookingTime;
    private double deposit;
    private int numberOfPax;

    public Booking(int customerId, int tourId, double deposit, int numberOfPax) {
        this.id = nextId++;
        this.customerId = customerId;
        this.tourId = tourId;
        this.deposit = deposit;
        this.numberOfPax = numberOfPax;
        this.bookingTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public int getNumberOfPax() {
        return numberOfPax;
    }

    public void setNumberOfPax(int numberOfPax) {
        this.numberOfPax = numberOfPax;
    }

    public static void updateNextId(int id){
        if (id >= nextId){
            nextId = id + 1;
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Booking ID: %-3d | Tour ID: %-3d | KH ID: %-3d | Số lượng khách: %-2d | Đã cọc: %,.0f | Ngày đặt: %s",
                id, tourId, customerId, numberOfPax, deposit, bookingTime.format(formatter));
    }
}
