package service;

import exception.CustomerNotFoundException;
import model.Customer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    // use Paths.get(...) to create Path
    private final Path storagePath = Paths.get("customers.dat");
    private final List<Customer> customers = new ArrayList<>();

    public CustomerService() {
        load();
    }

    public synchronized List<Customer> getAll() {
        return new ArrayList<>(customers);
    }

    public synchronized Customer addCustomer(Customer c) {
        // Customer constructor already assigns id using Customer.nextId,
        // just ensure it's added and persisted.
        customers.add(c);
        save();
        return c;
    }

    public synchronized Customer findById(int id) throws CustomerNotFoundException {
        return customers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
    }

    public synchronized Optional<Customer> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return customers.stream()
                .filter(c -> email.equalsIgnoreCase(c.getEmail()))
                .findFirst();
    }

    public synchronized void updateCustomer(int id, Customer updated) throws CustomerNotFoundException {
        Customer existing = findById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
        if (updated.getSdt() != null) existing.setSdt(updated.getSdt());
        if (updated.getAddress() != null) existing.setAddress(updated.getAddress());
        save();
    }

    public synchronized void deleteCustomer(int id) throws CustomerNotFoundException {
        Customer existing = findById(id);
        customers.remove(existing);
        save();
    }

    private synchronized void load() {
        if (!Files.exists(storagePath)) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(storagePath)))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Customer> loaded = (List<Customer>) obj;
                customers.clear();
                customers.addAll(loaded);
                // ensure nextId in Customer is advanced to avoid id collision
                for (Customer c : customers) {
                    Customer.updateNextId(c.getId());
                }
            }
        } catch (EOFException eof) {
            // empty file -> ignore
        } catch (Exception e) {
            System.err.println("Failed to load customers: " + e.getMessage());
        }
    }

    private synchronized void save() {
        try {
            // ensure parent directories exist if any (file is at project root)
            if (storagePath.getParent() != null) Files.createDirectories(storagePath.getParent());
        } catch (IOException ignored) {
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(storagePath)))) {
            oos.writeObject(new ArrayList<>(customers));
        } catch (IOException e) {
            System.err.println("Failed to save customers: " + e.getMessage());
        }
    }
}
