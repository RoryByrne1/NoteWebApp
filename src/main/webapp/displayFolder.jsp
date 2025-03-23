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
    <jsp:include page="header.jsp"/>
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

    <div class="time-details">
        created: <%= request.getAttribute("folderCreatedAt") %> | last modified: <%= request.getAttribute("folderLastEdited") %>
    </div>

    <div class="breadcrumbs">
        <a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= parameters %>">📁</a> /
        <%
            StringBuilder cumulativePath = new StringBuilder();
            for (int i = 0; i < pathComponents.length; i++) {
                cumulativePath.append("/").append(pathComponents[i]);
        %>
        <a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= cumulativePath + parameters%>"><%= pathComponents[i].replace("-", " ")%></a> /
        <%
            }
        %>
    </div>

    <%
        String nextOrder = ascending ? "desc" : "asc";
    %>
    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th class="table-item"><a class="nice-link" href="?sort=name&order=<%= sortBy.equals("name") ? nextOrder : "asc" %>">
                        name <%= sortBy.equals("name") ? (ascending ? "↑" : "↓") : "" %>
                    </a></th>
                    <th class="table-edited"><a class="nice-link" href="?sort=lastEdited&order=<%= sortBy.equals("lastEdited") ? nextOrder : "asc" %>">
                        last modified <%= sortBy.equals("lastEdited") ? (ascending ? "↑" : "↓") : "" %>
                    </a></th>
                    <th class="table-created"><a class="nice-link" href="?sort=createdAt&order=<%= sortBy.equals("createdAt") ? nextOrder : "asc" %>">
                        created <%= sortBy.equals("createdAt") ? (ascending ? "↑" : "↓") : "" %>
                    </a></th>
                </tr>
            </thead>
            <tbody>
            <% for (Item item : contentsList) { %>
            <tr>
                <% if (item instanceof Note) { %>
                <td class="table-item"><a class="nice-link" href="<%= request.getContextPath() %>/displayNote<%= pathString + "/" + item.getId() %>">📄 <%= item.getName() %></a></td>
                <% } else if (item instanceof Folder) { %>
                <td class="table-item"><a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= pathString + "/" + item.getId() %>">📁 <%= item.getName() %></a></td>
                <% } %>
                <td class="table-edited"><div class="table-date-body"><%= item.getLastEditedReadable(true) %></div></td>
                <td class="table-created"><div class="table-date-body"><%= item.getCreatedAtReadable(true) %></div></td>
            </tr>
            <% } %>
            </tbody>
        </table>
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

    <button type="button" title="create a new folder in this folder" onclick="addFolder()">add folder</button>
    <button type="button" title="create a new note in this folder" onclick="addNote()">add note</button>
</div>
</body>
</html>
