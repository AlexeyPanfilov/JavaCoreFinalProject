package org.example.serverlogic;

import java.io.*;

public class DataSaveAndLoad {

    public void saveServerStatToBin (File file, MaxCategoryCalc serverState) {
        try (ObjectOutputStream objOS = new ObjectOutputStream(new FileOutputStream(file))) {
            objOS.writeObject(serverState);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MaxCategoryCalc loadServerStateFromBin (File file) {
        MaxCategoryCalc serverState = null;
        try (FileInputStream inputStream = new FileInputStream(file);
        ObjectInputStream objIS = new ObjectInputStream(inputStream)) {
            serverState = (MaxCategoryCalc) objIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return serverState;
    }
}
