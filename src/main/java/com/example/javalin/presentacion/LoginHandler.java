package com.example.javalin.presentacion;

import com.example.javalin.modelo.Dueño;
import com.example.javalin.persistencia.RepositorioDueños;
import com.example.javalin.presentacion.dto.LoginRequest;
import com.example.javalin.presentacion.dto.LoginResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class LoginHandler implements Handler {

    private final RepositorioDueños repoDueños;

    public LoginHandler() {
        this.repoDueños = new RepositorioDueños();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        //validamos user/pass y buscamos datos de ese usuario para agregar en la sesión

        LoginRequest loginRequest = context.bodyAsClass(LoginRequest.class);

        Dueño dueño = repoDueños.buscarDueñoSegunLogin(loginRequest.getUsername(),loginRequest.getPassword());

        if(dueño!=null){
            System.out.println("Login exitoso: " + loginRequest);
            System.out.println("Login: " + dueño);
            SesionManager sesionManager = SesionManager.get();
            String idSesion = sesionManager.crearSesion("dueño", dueño);
            context.json(new LoginResponse(idSesion));
            context.status(200);
        } else{
            System.out.println("Error en el login: " + loginRequest);
            context.status(400).result("Usuario y/o contraseña incorrectos");
        }
    }

}
