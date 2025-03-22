<%@ page import="java.util.List" %>
<%@ page import="ucl.ac.uk.classes.Item" %>
<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="ucl.ac.uk.classes.Folder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        String folderName = (String) request.getAttribute("folderName");
    %>
    <title><%=folderName.equals("root")? "my notes" : folderName%></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
    <style>
        .items {
            margin-top: 10px;
            margin-left: 20px;
            margin-bottom: 15px;
        }
        .item {
            margin-bottom: 1px;
        }
        button {
            border-radius: 0;
        }
    </style>
</head>
<body>
<div>
    <%
        String pathString = (String) request.getAttribute("pathString");
        String[] pathComponents = pathString == null || pathString.isEmpty() ? new String[0] : pathString.substring(1).split("/");
        List<Item> contentsList = (List<Item>) request.getAttribute("contentsList");
        String sortBy = (String) request.getAttribute("sortBy");
        boolean ascending = (boolean) request.getAttribute("ascending");
        String parameters =  "?sort=" + sortBy + "&order=" + (ascending ? "asc" : "desc");
    %>
    <h1><%= folderName.equals("root") ? "my notes" : folderName %></h1>
    <div class="breadcrumbs">
        <a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= parameters %>">📁</a> /
        <%
            String cumulativePath = "";
            for (int i = 0; i < pathComponents.length; i++) {
                cumulativePath += "/" + pathComponents[i];
        %>
        <a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= cumulativePath + parameters%>"><%= pathComponents[i].replace("-", " ")%></a> /
        <%
            }
        %>
    </div>
    <div class="sorting">
        <span><b>sort:</b></span>
        <%
            String nameArrow = sortBy.equals("name") ? (ascending ? " ↑" : " ↓") : "  ";
            String dateArrow = sortBy.equals("createdAt") ? (ascending ? " ↑" : " ↓") : "  ";
            String modifiedArrow = sortBy.equals("lastEdited") ? (ascending ? " ↑" : " ↓") : "  ";

            String nextOrder = ascending ? "desc" : "asc";
            String nameOrder = sortBy.equals("name") ? nextOrder : "asc";
            String dateOrder = sortBy.equals("createdAt") ? nextOrder : "asc";
            String modifiedOrder = sortBy.equals("lastEdited") ? nextOrder : "asc";
        %>

        <a class="nice-link" href="?sort=name&order=<%= nameOrder %>">name<%= "  " + nameArrow %></a> |
        <a class="nice-link" href="?sort=createdAt&order=<%= dateOrder %>">date created<%= dateArrow %></a> |
        <a class="nice-link" href="?sort=lastEdited&order=<%= modifiedOrder %>">last modified<%= modifiedArrow %></a>
    </div>
    <div class="items">
        <%
        for (Item item : contentsList)
        {
            if (item instanceof Note)
                {%><div class="item"><a class="nice-link" href="<%= request.getContextPath() %>/displayNote<%= pathString + "/" + item.getId()%>">📄 <%= item.getName() %></a></div><%}
            else if (item instanceof Folder)
                {%><div class="item"><a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= pathString + "/" + item.getId() + parameters%>">📁 <%= item.getName() %></a></div><%}
        }
        %>
    </div>
    <script>
        function addFolder() {
            let folderName = prompt("enter folder name:");
            if (folderName) {
                window.location.href = "<%= request.getContextPath() %>/createFolder<%= pathString + parameters%>&folderName=" + encodeURIComponent(folderName);
            }
        }
        function addNote() {
            window.location.href = "<%= request.getContextPath() %>/createNote<%= pathString%>?noteName=untitled";
        }
    </script>
    <button type="button" onclick="addFolder()">add folder</button>
    <button type="button" onclick="addNote()">add note</button>
</div>
</body>
</html>
