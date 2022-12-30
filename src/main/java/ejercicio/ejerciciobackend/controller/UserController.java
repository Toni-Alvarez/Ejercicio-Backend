package ejercicio.ejerciciobackend.controller;

import ejercicio.ejerciciobackend.exception.UserNotFoundException;
import ejercicio.ejerciciobackend.model.User;
import ejercicio.ejerciciobackend.model.UserStatus;
import ejercicio.ejerciciobackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/** Clase controladora de la API de usuarios */
@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    /**Inyección de dependencia con @Autowired*/
    @Autowired
    private UserRepository userRepository;

    /** Endpoint que permite crear un nuevo usuario.
     @param newUser El usuario a crear.
     @return El usuario creado.
     */

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

 // Este método maneja la petición HTTP GET a la ruta '/users' y devuelve una lista de todos los usuarios
 // almacenadosen el repositorio de usuarios.
 // El método se utiliza para obtener una lista de todos los usuarios disponibles en el sistema.

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

//se encarga de obtener un usuario en particular a partir de su identificador.
// Se usa el método findById del repositorio de usuarios para buscar el usuario con el Id especificado
    @GetMapping("/user/{Id}")
    User getUserById(@PathVariable Long Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException(Id));
    }
//Este método se encarga de actualizar los datos de un usuario en la base de datos.
// Recibe como parámetro el nuevo objeto de usuario y su identificador único.
// Primero, busca el usuario en la base de datos utilizando el repositorio y el identificador.
    @PutMapping("/user/{Id}")
    User updateUser(@RequestBody User newUser,@PathVariable Long Id){
        return userRepository.findById(Id)
                .map(user -> {
                    user.setUsername(newUser.getGender());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setEmails(newUser.getEmails());
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException(Id));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @RequestBody User user) {
        // Lógica para actualizar el estado del usuario...

        if (user.getUserStatus() == UserStatus.ACTIVE) {
            // El usuario está activo
        } else if (user.getUserStatus() == UserStatus.INACTIVE) {
            // El usuario está inactivo
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
    // Este método actualiza la foto de perfil del usuario con el ID especificado.
    // Se utiliza el parámetro "photo"
    // en el cuerpo de la solicitud para proporcionar la foto a actualizar.
    // Si la actualización se realiza con éxito, se devuelve un código de estado HTTP 204 (Sin contenido).
    // Si ocurre un error durante la actualización, se devuelve un código de estado HTTP 500 (Error interno del servidor).
    @PutMapping("/users/{id}/photo")
    public ResponseEntity<Void> updateUserPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            user.setPhoto(photo.getBytes());
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            // Mostrar mensaje de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/user/{Id}")
    String deleteUser(@PathVariable Long Id) {
        if (!userRepository.existsById(Id)) {
            throw new UserNotFoundException(Id);

        }
        userRepository.deleteById(Id);
        return "El usuario con Id " + Id + " ha sido eliminado.";

    }
    // Este método permite obtener una lista de usuarios de manera paginada, utilizando los parámetros 'page' y 'size'
    // para indicar la página y el tamaño de la página, respectivamente.
    // La lista de usuarios se obtiene llamando al método findAll del repositorio de usuarios
    // y aplicándole el objeto PageRequest con los parámetros proporcionados.
    // Finalmente, se devuelve el contenido de la página obtenida.
    @GetMapping("/pageable")
    List<User> getUserPageable(@RequestParam int page, @RequestParam int size){
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
}

}