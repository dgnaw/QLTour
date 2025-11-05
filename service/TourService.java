package service;

import exception.TourNotFoundException;
import exception.TourFullException;
import model.TourPackage;
import repository.GenericRepository;

import java.util.List;
import java.time.LocalDate;

public class TourService {
    //Khai báo Repo và List
    private final GenericRepository<TourPackage> tourRepository;
    private final List<TourPackage> tourPackages;
    //Tên file chứa dữ liệu

    private static final String FILE_NAME = "data/TourPackage.dat";

    //Constructor
    public TourService() {
        this.tourRepository = new GenericRepository<>(FILE_NAME);
        this.tourPackages = this.tourRepository.load();

        //Cập nhật ID để tránh trùng
        updateNextIdAfterLoad();
    }

    private void updateNextIdAfterLoad(){
        int maxId = 0;
        for(TourPackage tour : tourPackages) {
            if (tour.getId() > maxId) {
                maxId = tour.getId();
            }
        }
        TourPackage.updateNextId(maxId);
    }

    //CRUD
    //Read
    //Lấy toàn bộ danh sách tour
    public List<TourPackage> getAllTours() {
        return this.tourPackages;
    }
    //Tìm tour theo id
    public TourPackage findTourById(int id) throws TourNotFoundException {
        for(TourPackage tour : this.tourPackages) {
            if(tour.getId() == id) {
                return tour;
            }
        }
        //Không return -> không thấy tour
        throw new TourNotFoundException("Không thấy tour với ID: " + id);
    }

    //Create
    public void createTour(String tourName, String itinerary, double price, int maxCapacity, LocalDate startDate, LocalDate endDate) throws TourNotFoundException {
        TourPackage newTourPackage = new TourPackage(tourName, itinerary, price, maxCapacity, startDate, endDate);
        //Thêm tour vào Package
        this.tourPackages.add(newTourPackage);
        saveChanges();
    }

    //Update
    public void updateTour(int id, String tourName, String itinerary, double price, int maxCapacity, int bookedCapacity, LocalDate startDate, LocalDate endDate) throws TourNotFoundException {
        //Tìm với id
        TourPackage tourToUpdate = findTourById(id);
        //Cập nhật dữ liệu mới
        tourToUpdate.setTourName(tourName);
        tourToUpdate.setItinerary(itinerary);
        tourToUpdate.setPrice(price);
        tourToUpdate.setMaxCapacity(maxCapacity);
        tourToUpdate.setBookedCapacity(bookedCapacity);
        tourToUpdate.setStartDate(startDate);
        tourToUpdate.setEndDate(endDate);
        //Lưu lại
        saveChanges();
    }

    //Delete
    public void deleteTour(int id) throws TourNotFoundException {
        TourPackage tourToDelete = findTourById(id);

        this.tourPackages.remove(tourToDelete);
        saveChanges();
    }

    //Check tour còn đủ slot không
    public boolean checkAvailability(int id, int guests) throws TourNotFoundException {
        TourPackage tourToCheck = findTourById(id);
        //
        int slots = tourToCheck.getMaxCapacity() - tourToCheck.getBookedCapacity();
        return slots >= guests;
    }

    public void bookSpots(int id, int guests) throws TourNotFoundException, TourFullException {
        TourPackage tourToCheck = findTourById(id);

        int slots = tourToCheck.getMaxCapacity() - tourToCheck.getBookedCapacity();
        if(slots < guests) {
            throw new TourFullException("Khong du cho, chi con " + slots + " cho!");
        }

        tourToCheck.setBookedCapacity(tourToCheck.getBookedCapacity() + guests);
        saveChanges();
    }

    public void cancelBookedSpots(int id, int guests) throws TourNotFoundException {
        TourPackage tour = findTourById(id);

        //
        int newBookedCapacity = tour.getBookedCapacity() - guests;
        tour.setBookedCapacity(Math.max(0, newBookedCapacity)); // đảm bảo không âm

        //
        saveChanges();
    }

    public void saveChanges() {
        this.tourRepository.save(this.tourPackages);
    }
}
