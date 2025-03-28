package ucl.ac.uk.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ucl.ac.uk.classes.Folder;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;


@WebServlet("/displayFolder/*")
public class DisplayFolderServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        // get the data from the model
        Model model = ModelFactory.getModel();

        String sortBy = request.getParameter("sort");
        String order = request.getParameter("order");

        if (sortBy == null) sortBy = "name";
        boolean ascending = (order == null || order.equals("asc"));

        String pathString = request.getPathInfo();
        List <String> path;

        if (pathString == null)
        {
            pathString = "";
            path = new ArrayList<>();
        }
        else
            path = List.of(pathString.substring(1).split("/"));

        Folder folder = null;
        if (model.checkFolder(path))
            folder = (Folder) model.resolvePath(path);

        request.setAttribute("pathString", pathString);
        request.setAttribute("folderName", folder.getName());
        request.setAttribute("contentsList", folder.getContentsList(sortBy, ascending));
        request.setAttribute("folderCreatedAt", folder.getCreatedAtReadable(false));
        request.setAttribute("folderLastEdited", folder.getLastEditedReadable(false));
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("ascending", ascending);

        // then forward to JSP
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/displayFolder.jsp");
        dispatch.forward(request, response);
    }
}