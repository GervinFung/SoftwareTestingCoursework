package main.backend.fileinterface;

import java.util.List;

public interface MyFileAccessor<T> {

    List<T> readFromFile();
    List<T> getDataList();
}