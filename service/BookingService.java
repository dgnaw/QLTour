package service;

import exception.BookingNotFoundException;
import exception.TourFullException;
import exception.TourNotFoundException;
import model.Booking;
import repository.GenericRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private List<Booking> bookings;
    private final GenericRepository<Booking> bookingRepository;
    private static final String BOOKINGS_FILE = "Bookings.dat";
    private final TourService tourService;

    public BookingService(TourService tourService){
        this.tourService = tourService;
        this.bookingRepository = new GenericRepository<>(BOOKINGS_FILE);
        this.bookings = bookingRepository.load();

        updateNextIdAfterLoad();
    }

    private void updateNextIdAfterLoad() {
        int maxId = 0;
        for (Booking booking : bookings) {
            if (booking.getId() > maxId) {
                maxId = booking.getId();
            }
        }
        Booking.updateNextId(maxId);
    }

    public List<Booking> getAllBookings(){
        return bookings;
    }

    // TÍNH TOÁN TỔNG SỐ CHỖ ĐÃ ĐẶT
    public int getTotalBookedSeats(int tourId) {
        int totalPax = 0;
        for (Booking booking : bookings) {
            if (booking.getTourId() == tourId) {
                totalPax += booking.getNumberOfPax();
            }
        }
        return totalPax;
    }

    public Booking findBookingById(int bookingId) throws BookingNotFoundException{
        for (Booking booking : bookings){
            if (booking.getId() == bookingId){
                return booking;
            }
        }
        throw new BookingNotFoundException("Không tìm thấy booking với ID: " + bookingId);
    }

    public void createBooking(int tourId, int customerId, double deposit, int numberOfPax) throws TourNotFoundException, TourFullException{
        // 1. Kiểm tra tour có đủ chỗ không (TourService sẽ ném ra lỗi nếu hết)
        tourService.bookSpots(tourId, numberOfPax);
        // *Lưu ý: bookSpots() trong TourService sẽ tự động cập nhật bookedCapacity và lưu file.*

        // 2. Tạo đối tượng Booking mới
        Booking newBooking = new Booking(customerId, tourId, deposit, numberOfPax);
        this.bookings.add(newBooking);

        // 3. Lưu lại danh sách booking
        this.bookingRepository.save(this.bookings);
    }

    public void cancelBooking(int id) throws BookingNotFoundException, TourNotFoundException {
        // 1. Tìm Booking cần hủy
        Booking bookingToDelete = findBookingById(id);

        // 2. Hoàn lại số chỗ đã đặt (TourService sẽ tự động cập nhật và lưu file)
        tourService.cancelBookedSpots(bookingToDelete.getTourId(), bookingToDelete.getNumberOfPax());

        // 3. Xóa Booking khỏi danh sách
        this.bookings.remove(bookingToDelete);

        // 4. Lưu lại danh sách booking
        this.bookingRepository.save(this.bookings);
    }

    // HÀM HỖ TRỢ BÁO CÁO (Dùng cho ReportService)

    // Tính tổng số tiền cọc đã thu cho một tour
    public double getTotalDepositByTour(int tourId) {
        double totalDeposit = 0;
        for (Booking booking : bookings) {
            if (booking.getTourId() == tourId) {
                totalDeposit += booking.getDeposit();
            }
        }
        return totalDeposit;
    }

    // Lấy danh sách booking của một tour (dùng cho ReportService)
    public List<Booking> getBookingsByTour(int tourId) {
        List<Booking> results = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getTourId() == tourId) {
                results.add(booking);
            }
        }
        return results;
    }


}
