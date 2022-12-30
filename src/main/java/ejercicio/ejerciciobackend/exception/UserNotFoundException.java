package ejercicio.ejerciciobackend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long Id){
        super("No se ha podido encontrar el usuario con Id "+ Id);

    }
}