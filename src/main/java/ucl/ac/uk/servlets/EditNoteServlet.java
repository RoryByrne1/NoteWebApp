package ucl.ac.uk.servlets;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ucl.ac.uk.classes.*;
import ucl.ac.uk.model.Model;
import ucl.ac.uk.model.ModelFactory;

import jakarta.servlet.http.Part;


@WebServlet("/editNote/*")
@MultipartConfig (
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
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
            if (block instanceof TextBlock || block instanceof URLBlock)
            {
                String blockContent = request.getParameter("block_" + block.getId());
                model.editBlock(path, block.getId(), blockContent);
            }
            else if (block instanceof ImageBlock)
            {
                handleImageUpload(request, response, block);
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
    public void handleImageUpload(HttpServletRequest request, HttpServletResponse response, Block block)
            throws IOException, ServletException {
        Part imagePart = request.getPart("imageUpload_" + block.getId());
        if (imagePart != null && imagePart.getSize() > 0) {
            String originalFileName = extractFileName(imagePart);
            String fileName = generateUniqueFileName(originalFileName);
            String uploadPath = getServletContext().getRealPath("/images");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            File imageFile = new File(uploadPath + File.separator + fileName);
            try (InputStream input = imagePart.getInputStream(); FileOutputStream output = new FileOutputStream(imageFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            ((ImageBlock) block).setImagePath("images/" + fileName);
        }
    }

    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private String generateUniqueFileName(String originalFileName) {
        File file;
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        int counter = 0;

        // check if the file already exists and generate a new name if needed
        file = new File(getServletContext().getRealPath("/images") + File.separator + originalFileName);
        while (file.exists())
        {
            counter++;
            String newFileName = baseName + (counter) + extension;
            file = new File(getServletContext().getRealPath("/images") + File.separator + newFileName);
        }

        return baseName + (counter == 0? "" : counter) + extension;
    }
}