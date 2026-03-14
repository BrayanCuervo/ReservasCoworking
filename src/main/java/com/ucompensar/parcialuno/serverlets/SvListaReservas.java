
package com.ucompensar.parcialuno.serverlets;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import logica.Reserva;
import persistencia.ReservaJpaController;

@WebServlet(name = "SvListaReservas", urlPatterns = {"/SvListaReservas"})
public class SvListaReservas extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("parcialUnoPU");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReservaJpaController controlador = new ReservaJpaController(emf);

     
        List<Reserva> listaReservas = controlador.findReservaEntities();

       
        request.setAttribute("listaReservas", listaReservas);

      
        request.getRequestDispatcher("lista_reservas.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        if (emf != null) {
            emf.close();
        }
    }
}