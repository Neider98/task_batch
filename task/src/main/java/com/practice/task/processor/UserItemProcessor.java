package com.practice.task.processor;

import com.practice.task.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Procesador de elementos para usuarios.
 * Este procesador se encarga de transformar los datos de entrada antes de escribirlos en la base de datos.
 */
@Component
public class UserItemProcessor implements ItemProcessor<User, User> {

    /**
     * Procesa un usuario, convirtiendo su nombre a mayúsculas y validando su dirección de correo electrónico.
     *
     * @param user El usuario a procesar.
     * @return El usuario procesado o null si la dirección de correo electrónico no es válida.
     * @throws Exception si ocurre algún error durante el procesamiento.
     */
    @Override
    public User process(User user) throws Exception {
        user.setNombre(user.getNombre().toUpperCase());
        if (isValidEmail(user.getEmail())) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Valida si una dirección de correo electrónico es válida.
     *
     * @param email La dirección de correo electrónico a validar.
     * @return true si la dirección de correo electrónico es válida, false de lo contrario.
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}
