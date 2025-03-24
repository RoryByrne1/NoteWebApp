package ucl.ac.uk.servlets;

import java.io.*;
import java.util.List;

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


@WebServlet("/displayNote/*")
public class DisplayNoteServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        Model model = ModelFactory.getModel();

        String pathString = request.getPathInfo();
        List<String> path = List.of(pathString.substring(1).split("/"));
        Note note = model.getNote(path);

        request.setAttribute("pathString", pathString);
        request.setAttribute("note", note);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/displayNote.jsp");
        dispatch.forward(request, response);
    }
}