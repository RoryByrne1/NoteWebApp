<%@ page import="java.util.List" %>
<%@ page import="ucl.ac.uk.classes.Item" %>
<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="ucl.ac.uk.classes.Folder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>my notes</title>
    <style>
        h1 {
            margin-bottom: 5px;
        }
        body {
            margin-left: 5%;
        }
        .button-container {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            margin-top: 10px;
        }
        .button-container form {
            margin: 0;
        }
        .breadcrumbs {
            margin-bottom: 12px;
        }
        a {
            text-decoration: none;
            color: black;
        }
        a:hover {
            color: #777;
        }
    </style>
</head>
<body>
<div>
    <%
        String pathString = (String) request.getAttribute("pathString");
        String[] pathComponents = pathString == null || pathString.isEmpty() ? new String[0] : pathString.substring(1).split("/");
        String folderName = (String) request.getAttribute("folderName");
        List<Item> contentsList = (List<Item>) request.getAttribute("contentsList");
        String sortBy = (String) request.getAttribute("sortBy");
        boolean ascending = (boolean) request.getAttribute("ascending");
        String parameters =  "?sort=" + sortBy + "&order=" + (ascending ? "asc" : "desc");
    %>
    <h1><%= folderName.equals("root") ? "my notes" : folderName %></h1>
    <div class="breadcrumbs">
        <a href="<%= request.getContextPath() %>/displayFolder<%= parameters %>">📁</a> /
        <%
            String cumulativePath = "";
            for (int i = 0; i < pathComponents.length; i++) {
                cumulativePath += "/" + pathComponents[i];
        %>
        <a href="<%= request.getContextPath() %>/displayFolder<%= cumulativePath + parameters%>"><%= pathComponents[i].replace("-", " ")%></a> /
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

        <a href="?sort=name&order=<%= nameOrder %>">name<%= "  " + nameArrow %></a> |
        <a href="?sort=createdAt&order=<%= dateOrder %>">date created<%= dateArrow %></a> |
        <a href="?sort=lastEdited&order=<%= modifiedOrder %>">last modified<%= modifiedArrow %></a>
    </div>
    <ul>
        <%
            for (Item item : contentsList)
            {
                if (item instanceof Note)
                {
                    %><li><a href="<%= request.getContextPath() %>/displayNote<%= pathString + "/" + item.getId()%>">📄 <%= item.getName() %></a></li><%
                }
                else if (item instanceof Folder)
                {
                    %><li><a href="<%= request.getContextPath() %>/displayFolder<%= pathString + "/" + item.getId() + parameters%>">📁 <%= item.getName() %></a></li><%
                }
            }
        %>
    </ul>
    <script>
        function addFolder() {
            let folderName = prompt("enter folder name:");
            if (folderName) {
                window.location.href = "<%= request.getContextPath() %>/createFolder<%= pathString + parameters%>&folderName=" + encodeURIComponent(folderName);
            }
        }
    </script>
    <button type="button" onclick="addFolder()">add folder</button>
</div>
</body>
</html>
