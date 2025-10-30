package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInputHandler {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Tiêu thụ ký tự xuống dòng
                return value;
            }
            catch (InputMismatchException e) {
                System.out.println("Đầu vào không hợp lệ. Vui lòng nhập một số nguyên.");
                scanner.nextLine(); // Xóa bộ đệm
                System.out.print(prompt);
            }
        }
    }


    public static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Tiêu thụ ký tự xuống dòng
                return value;
            }
            catch (InputMismatchException e) {
                System.out.println("Đầu vào không hợp lệ. Vui lòng nhập một số.");
                scanner.nextLine(); // Xóa bộ đệm
                System.out.print(prompt);
            }
        }
    }


    public static LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        while(true) {
            try {
                String dateString = scanner.nextLine();
                return LocalDate.parse(dateString, DATE_FORMATTER);
            }
            catch(DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ (dd/MM/yyyy). Vui lòng nhập lại.");
                System.out.print(prompt);
            }
        }
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}