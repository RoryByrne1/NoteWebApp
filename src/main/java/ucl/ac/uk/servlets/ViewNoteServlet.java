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


@WebServlet("/viewNote")
public class ViewNoteServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String noteId = request.getParameter("noteId");

        if (noteId == null || noteId.isEmpty())
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing noteId");
            return;
        }

        // Get the data from the model
        Model model = ModelFactory.getModel();
        Note note = model.getNote(noteId);
        // Then add the data to the request object that will be sent to the Java Server Page, so that
        // the JSP can access the data (a Java data structure).
        request.setAttribute("note", note);

        // Then forward to JSP.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/viewNote.jsp");
        dispatch.forward(request, response);
    }
}