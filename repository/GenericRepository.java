package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenericRepository<T extends Serializable> {
    private final String fileName;
    public GenericRepository(String fileName){
        this.fileName = fileName;
    }

    @SuppressWarnings("unchecked")
    public List<T> load(){
        File file = new File(fileName);
        if (!file.exists()){
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            return (List<T>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Loi doc file " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void save(List<T> dataList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(dataList);
        }catch(IOException e){
            System.err.println("Loi luu file " + fileName + ": " + e.getMessage());
        }
    }
}
