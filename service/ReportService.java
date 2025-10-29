package service;

import model.Customer;
import model.Booking;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    /**
     * Trả về danh sách khách hàng đã đặt tour có id = tourId.
     * Sử dụng int tourId vì model Customer dùng int id.
     */
    public List<Customer> getCustomersByTour(int tourId, List<Booking> bookings, List<Customer> customers) {
        if (bookings == null || customers == null) return Collections.emptyList();

        Set<Integer> customerIds = bookings.stream()
                .map(b -> tryGetInt(b, "getTourId") == tourId ? tryGetInt(b, "getCustomerId") : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return customers.stream()
                .filter(c -> customerIds.contains(c.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Tính tổng doanh thu cho một tour dựa trên danh sách booking.
     * Hỗ trợ method getTotalPrice() hoặc getPricePerSeat()*getSeatsBooked().
     */
    public double totalRevenueForTour(int tourId, List<Booking> bookings) {
        if (bookings == null) return 0.0;
        double total = 0.0;
        for (Booking b : bookings) {
            Integer bTour = tryGetInt(b, "getTourId");
            if (bTour == null || bTour != tourId) continue;

            Double totalPrice = tryGetDouble(b, "getTotalPrice");
            if (totalPrice != null) {
                total += totalPrice;
                continue;
            }

            Double pricePerSeat = tryGetDouble(b, "getPricePerSeat");
            Double seats = tryGetDouble(b, "getSeatsBooked");
            if (pricePerSeat != null && seats != null) {
                total += pricePerSeat * seats;
            }
        }
        return total;
    }

    // Reflection helpers to be tolerant with different Booking implementations
    private Integer tryGetInt(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            Object r = m.invoke(obj);
            if (r instanceof Integer) return (Integer) r;
            if (r instanceof Short) return ((Short) r).intValue();
            if (r instanceof Long) return ((Long) r).intValue();
            if (r instanceof String) return Integer.parseInt((String) r);
        } catch (Exception ignored) {
        }
        return null;
    }

    private Double tryGetDouble(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            Object r = m.invoke(obj);
            if (r instanceof Double) return (Double) r;
            if (r instanceof Float) return ((Float) r).doubleValue();
            if (r instanceof Integer) return ((Integer) r).doubleValue();
            if (r instanceof Long) return ((Long) r).doubleValue();
            if (r instanceof String) return Double.parseDouble((String) r);
        } catch (Exception ignored) {
        }
        return null;
    }
}
