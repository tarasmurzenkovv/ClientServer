package model.processor;

import java.io.IOException;

public interface Processable<T> {
    void process(T message) throws IOException;
}
