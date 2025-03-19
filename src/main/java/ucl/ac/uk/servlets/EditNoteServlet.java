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
import ucl.ac.uk.classes.*;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;


@WebServlet("/editNote/*")
public class EditNoteServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        Model model = ModelFactory.getModel();

        String pathString = request.getPathInfo();
        List<String> path = List.of(pathString.substring(1).split("/"));
        Note note = model.getNote(path);

        // Get the data from the model
        // Then add the data to the request object that will be sent to the Java Server Page, so that
        // the JSP can access the data (a Java data structure).
        request.setAttribute("pathString", pathString);
        request.setAttribute("note", note);

        // Then forward to JSP.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/editNote.jsp");
        dispatch.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Model model = ModelFactory.getModel();
        String pathString = request.getPathInfo();
        List<String> path = List.of(pathString.substring(1).split("/"));

        // name update
        String newName = request.getParameter("name");
        if (newName != null && !newName.trim().isEmpty()) {
            model.editItemName(path, newName);
        }

        // block updates
        for (Block block : model.getNote(path).getBlocksList()) {
            String blockContent = request.getParameter("block_" + block.getId());
            if (blockContent != null)
            {
                model.editBlock(path, block.getId(), blockContent);
            }
        }

        // block buttons
        String moveBlockUpId = request.getParameter("moveBlockUp");
        String moveBlockDownId = request.getParameter("moveBlockDown");
        String addBlockAboveId = request.getParameter("addBlockAbove");
        String addBlockBeneathId = request.getParameter("addBlockBeneath");
        String deleteBlockId = request.getParameter("deleteBlock");

        if (moveBlockUpId != null)
            model.moveBlock(path, moveBlockUpId, false);
        else if (moveBlockDownId != null)
            model.moveBlock(path, moveBlockDownId, true);
        else if (addBlockAboveId != null || addBlockBeneathId != null)
        {
            String blockType = request.getParameter("newBlockType");
            Block newBlock = switch (blockType) {
                case "text" -> new TextBlock();
                case "image" -> new ImageBlock();
                case "url" -> new URLBlock();
                default -> null;
            };
            if (addBlockAboveId != null)
                model.addBlockFrom(path, addBlockAboveId, newBlock, false);
            else
                model.addBlockFrom(path, addBlockBeneathId, newBlock, true);
        }
        else if (deleteBlockId != null)
            model.deleteBlock(path, deleteBlockId);

        // add new block
        if (request.getParameter("addBlock") != null) {
            String blockType = request.getParameter("newBlockType");
            switch (blockType) {
                case "text" -> model.addBlock(path, new TextBlock());
                case "image" -> model.addBlock(path, new ImageBlock());
                case "url" -> model.addBlock(path, new URLBlock());
            }
        }

        // save changes and redirect to the edit page
        model.saveNotes();
        response.sendRedirect(request.getContextPath() + "/editNote" + pathString);
    }
}