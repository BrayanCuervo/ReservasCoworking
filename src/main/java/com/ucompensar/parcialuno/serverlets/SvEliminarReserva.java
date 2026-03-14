
package com.ucompensar.parcialuno.serverlets;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import persistencia.ReservaJpaController;

@WebServlet(name = "SvEliminarReserva", urlPatterns = {"/SvEliminarReserva"})
public class SvEliminarReserva extends HttpServlet{
     EntityManagerFactory emf = Persistence.createEntityManagerFactory("parcialUnoPU");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        ReservaJpaController controlador = new ReservaJpaController(emf);

        try {
            controlador.destroy(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("SvListaReservas");
    }
}
