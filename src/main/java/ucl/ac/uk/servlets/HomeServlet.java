package ucl.ac.uk.servlets;

import java.io.*;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;


@WebServlet("")
public class HomeServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        Model model = ModelFactory.getModel();

        String sortBy = request.getParameter("sort");
        String order = request.getParameter("order");

        if (sortBy == null) sortBy = "name";
        boolean ascending = (order == null || order.equals("asc"));

        List<Map<String, Object>> pinnedMapList = model.getPinned(sortBy, ascending);
        List<Map<String, Object>> recentsMapList = model.getRecents(3);

        request.setAttribute("pinnedMapList", pinnedMapList);
        request.setAttribute("recentsMapList", recentsMapList);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("ascending", ascending);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/home.jsp");
        dispatch.forward(request, response);
    }
}