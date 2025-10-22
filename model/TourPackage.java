package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TourPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private String tourName, itinerary;
    private double price;
    private int maxCapacity;
    private LocalDate startDate;
    private LocalDate endDate;

    public TourPackage(String tourName, String itinerary, double price, int maxCapacity, LocalDate startDate, LocalDate endDate) {
        this.id = nextId++;
        this.tourName = tourName;
        this.itinerary = itinerary;
        this.price = price;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static void updateNextId(int id){
        if (id >= nextId){
            nextId = id + 1;
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Tour ID: %-3d | Tên: %-25s | Ngày đi: %s | Ngày về: %s | % Giá: %,.0f VND | Sức chứa: %d/%d",
                id, tourName, startDate.format(formatter), endDate.format(formatter), price, maxCapacity);
    }
}
