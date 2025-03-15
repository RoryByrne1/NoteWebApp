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
        System.out.println("noting");
        Model model = ModelFactory.getModel();

        String pathString = request.getPathInfo();
        System.out.println(pathString);
        List<String> path = List.of(pathString.substring(1).split("/"));
        Note note = model.getNote(path);

        // Get the data from the model
        // Then add the data to the request object that will be sent to the Java Server Page, so that
        // the JSP can access the data (a Java data structure).
        request.setAttribute("pathString", pathString);
        request.setAttribute("note", note);

        // Then forward to JSP.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/displayNote.jsp");
        dispatch.forward(request, response);
    }
}