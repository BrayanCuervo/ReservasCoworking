
package com.ucompensar.parcialuno.serverlets;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import logica.Reserva;
import persistencia.ReservaJpaController;

@WebServlet(name = "SvReserva", urlPatterns = {"/SvReserva"})
public class SvReserva extends HttpServlet {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("parcialUnoPU");

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String fecha = request.getParameter("fecha");
        String espacio = request.getParameter("espacio");
        String duracionStr = request.getParameter("duracion");

        // Validación
        if (nombre == null || nombre.isEmpty() ||
            fecha == null || fecha.isEmpty() ||
            espacio == null || espacio.isEmpty() ||
            duracionStr == null || duracionStr.isEmpty()) {

            response.sendRedirect("error.html");
            return;
        }

        int duracion = Integer.parseInt(duracionStr);

        Reserva reserva = new Reserva();
        reserva.setNombre(nombre);
        reserva.setFecha(fecha);
        reserva.setEspacio(espacio);
        reserva.setDuracion(duracion);

        ReservaJpaController controlador = new ReservaJpaController(emf);
        controlador.create(reserva);

        // Redirigir a la página de confirmación
        response.sendRedirect("confirmacion_reserva.jsp");
    }
}
