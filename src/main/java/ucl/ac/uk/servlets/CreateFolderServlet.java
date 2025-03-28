package ucl.ac.uk.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ucl.ac.uk.classes.Folder;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;


@WebServlet("/createFolder/*")
public class CreateFolderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Model model = ModelFactory.getModel();

        String pathString = request.getPathInfo();
        String folderName = request.getParameter("folderName");

        List <String> path;

        if (pathString == null)
        {
            pathString = "";
            path = new ArrayList<>();
        }
        else
            path = List.of(pathString.substring(1).split("/"));

        if (model.checkFolder(path) && !((Folder)model.resolvePath(path)).containsName(folderName)) {
            model.addItem(path, new Folder(folderName));

            // update last edited of each folder the note is in
            model.updateLastEditedAlong(path);
        }

        response.sendRedirect("/displayFolder" + pathString);
    }
}