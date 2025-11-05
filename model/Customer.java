package model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 2L;
    private static int nextId = 1;
    private int id;
    private String name, email, sdt, address;

    public Customer(String name, String email, String sdt, String address) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.sdt = sdt;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void updateNextId(int id){
        if (id >= nextId){
            nextId = id + 1;
        }
    }

    @Override
    public String toString() {
        return String.format("KH ID: %-3d | Tên: %-20s | SĐT: %-12s | Email: %-20s | Địa chỉ: %s", id, name,sdt, email, address);
    }
}
