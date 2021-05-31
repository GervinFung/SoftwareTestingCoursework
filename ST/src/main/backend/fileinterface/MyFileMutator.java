package main.backend.fileinterface;

public interface MyFileMutator<T> extends MyFileAccessor<T> {
    void writeToFile();
    void add(T t);
    void remove(T t);
}