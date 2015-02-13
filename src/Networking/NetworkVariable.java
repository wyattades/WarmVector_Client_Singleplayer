package Networking;

public class NetworkVariable<T extends Object> {
    T value;

    NetworkVariable(T value) {
        this.value = value;
    }

    T get() { return value; }
    void set(T value) { this.value = value; }
}
