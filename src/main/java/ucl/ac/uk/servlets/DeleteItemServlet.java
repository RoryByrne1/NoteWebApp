package ucl.ac.uk.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;

@WebServlet("/deleteItem")
public class DeleteItemServlet extends HttpServlet
{
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Model model = ModelFactory.getModel();
        String pathString = request.getParameter("pathString");
        String itemId = request.getParameter("itemId");

        List<String> path;
        if (!pathString.isEmpty())
            path = List.of(pathString.substring(1).split("/"));
        else
            path = new ArrayList<>();

        // delete item
        model.deleteItem(path, itemId);

        // update last edited of each folder this folder is in
        model.updateLastEditedAlong(path);

        response.sendRedirect(request.getContextPath() + "/displayFolder" + pathString);
    }
}