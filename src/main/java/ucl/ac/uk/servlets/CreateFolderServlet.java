package ucl.ac.uk.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ucl.ac.uk.classes.Folder;
import ucl.ac.uk.classes.Item;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;


@WebServlet("/createFolder/*")
public class CreateFolderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get model instance
        Model model = ModelFactory.getModel();

        // Extract the path and folder name from request
        String pathString = request.getPathInfo();
        String folderName = request.getParameter("folderName");
        String parameters = "?sort=" + request.getParameter("sort") + "&order=" + request.getParameter("order");

        List <String> path;

        if (pathString == null)
        {
            pathString = "";
            path = new ArrayList<>();
        }
        else
            path = List.of(pathString.substring(1).split("/"));

        if (model.checkFolder(path)) {
            model.addItem(path, new Folder(folderName));
        }

        // update last edited of each folder the note is in
        for (int i = 0; i <= path.size(); i++) {
            List<String> subPath = path.subList(0, i);
            model.resolvePath(subPath).updateLastEdited();
        }

        response.sendRedirect("/displayFolder" + pathString);
    }
}