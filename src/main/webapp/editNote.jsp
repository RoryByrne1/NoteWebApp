<%@ page import="ucl.ac.uk.classes.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        Note note = (Note) request.getAttribute("note");
    %>
    <title>edit <%=note.getName()%></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/editing.css">
    <jsp:include page="header.jsp"/>
</head>
<script>
    function updateBlockType() {
        document.getElementById('newBlockType').value = document.getElementById('blockType').value;
    }
</script>
<body>
<div class="note-container">
    <div class="metadata">
        <b>created:</b> <%= note.getCreatedAtReadable(true) %>
        <br>
        <b>last edited:</b> <%= note.getLastEditedReadable(true) %>
    </div>

    <%
        String pathString = (String) request.getAttribute("pathString");
        String parentPathString = pathString.substring(0, pathString.lastIndexOf("/")).replaceAll("-", " ");
        if (parentPathString.isEmpty()) parentPathString = "/";
    %>

    <form action="<%= request.getContextPath() %>/editNote<%= pathString %>" method="post" enctype="multipart/form-data">
        <div class="path-container">
            <input type="text" id="path" name="path" value="<%= parentPathString %>" placeholder="enter new path"/>
        </div>
        <div class="name-container">
            <input type="text" id="name" name="name" value="<%= note.getName() %>" placeholder="enter name"/>
        </div>
        <label>
            pin: <input class="pin-box" type="checkbox" name="pinned" <%= note.getPinned() ? "checked" : "" %> />
        </label>
        <div class="top-buttons-container">
            <input type="hidden" id="newBlockType"  name="newBlockType" value="text">
            <select class="add-block-select" title="select block type to add" id="blockType" onchange="updateBlockType()">
                <option value="text">text</option>
                <option value="image">image</option>
                <option value="url">url</option>
            </select>
            <button class="add-block-button" type="submit" title="add a new block at the bottom" name="addBlock">+</button>
            <div class="spacer"></div>
            <button class="save-button" type="submit" title="save changes">save</button>
        </div>
        <%
            String blockLabel;
            for (Block b : note.getBlocksList()) {
                blockLabel = "";
                if (b instanceof TextBlock)
                    blockLabel = "text";
                else if (b instanceof ImageBlock)
                    blockLabel = "image";
                else if (b instanceof URLBlock)
                    blockLabel = "url";
        %>
        <div class="block">
            <div class="block-header">
                <div class="block-label"><%= blockLabel %></div>
                <div class="block-buttons">
                    <button class="block-button" type="submit" name="moveBlockUp" title="move block up" value="<%= b.getId() %>">🡩</button>
                    <button class="block-button" type="submit" name="moveBlockDown" title="move block down" value="<%= b.getId() %>">🡫</button>
                    <button class="block-button" type="submit" name="addBlockAbove" title="insert a block above" value="<%= b.getId() %>">+🡩</button>
                    <button class="block-button" type="submit" name="addBlockBeneath" title="insert a block beneath" value="<%= b.getId() %>">+🡫</button>
                    <button class="block-button" type="submit" name="deleteBlock" title="delete block" value="<%= b.getId() %>">✖</button>
                </div>
            </div>
            <% if (b instanceof TextBlock) { %>
                <textarea name="block_<%= b.getId() %>" placeholder="enter text"><%= ((TextBlock) b).getText() %></textarea>
            <% } else if (b instanceof ImageBlock) { %>
                <div class="image-block">
                    <p><%= ((ImageBlock) b).getImagePath() %></p>
                    <input class="browse-button" type="file" name="imageUpload_<%= b.getId() %>" accept="image/*">
                </div>
            <% } else if (b instanceof URLBlock) { %>
                <textarea name="block_<%= b.getId() %>" placeholder="enter url"><%= ((URLBlock) b).getURL() %></textarea>
            <% } %>
        </div>
        <% } %>
    </form>
    <div class="bottom-buttons-container">
        <form action="<%= request.getContextPath() %>/displayNote<%= request.getAttribute("pathString") %>" method="get">
            <button class="btn" title="return to note display" id="back">back</button>
        </form>
    </div>
</div>
</body>
</html>