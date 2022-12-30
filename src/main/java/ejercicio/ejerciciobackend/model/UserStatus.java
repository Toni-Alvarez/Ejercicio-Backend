package ejercicio.ejerciciobackend.model;

public enum UserStatus {
    ACTIVE(200),
    INACTIVE(500);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
