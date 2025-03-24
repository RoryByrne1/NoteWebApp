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


@WebServlet("/renameFolder/*")
public class RenameFolderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        String pathString = request.getPathInfo();
        String newName = request.getParameter("newName");

        System.out.println("name" + newName);

        List <String> path;

        if (pathString == null)
        {
            pathString = "";
            path = new ArrayList<>();
        }
        else
            path = List.of(pathString.substring(1).split("/"));

        if (model.checkFolder(path)) {
            List<String> parentPath = path.subList(0, path.size() - 1);

            Folder oldFolder = (Folder) model.resolvePath(path);
            Folder newFolder = new Folder(newName, oldFolder.getContents(), oldFolder.getCreatedAt());

            model.deleteItem(parentPath, path.getLast());
            model.addItem(parentPath, newFolder);

            // update last edited of each folder the note is in
            model.updateLastEditedAlong(parentPath);

            response.sendRedirect("/displayFolder" + pathString.substring(0, pathString.lastIndexOf("/")) + "/" + newFolder.getId());
        }
    }
}