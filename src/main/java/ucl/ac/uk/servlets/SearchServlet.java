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
import ucl.ac.uk.classes.Note;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;


@WebServlet("/search/*")
public class SearchServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        Model model = ModelFactory.getModel();

        String sortBy = request.getParameter("sort");
        String order = request.getParameter("order");
        String query = request.getParameter("q");

        if (sortBy == null) sortBy = "name";
        boolean ascending = (order == null || order.equals("asc"));
        if (query == null) query = "";

        List<Map<String, Object>> notesMapList = model.searchNotes(sortBy, ascending, query);

        request.setAttribute("notesMapList", notesMapList);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("ascending", ascending);
        request.setAttribute("query", query);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/search.jsp");
        dispatch.forward(request, response);
    }
}