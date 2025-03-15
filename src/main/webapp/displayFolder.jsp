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
        Folder folder = (Folder) request.getAttribute("folder");
    %>
    <h1><%=folder.getName().equals("root") ? "my notes" : folder.getName()%></h1>
    <div class="button-container">
        <form action="displayFolder" method="get">
            <button class="btn" id="addCategory">add folder</button>
        </form>
        <form action="displayFolder" method="get">
            <button class="btn" id="addNote">add note</button>
        </form>
    </div>
    <div class="breadcrumbs">
        <a href="<%= request.getContextPath() %>/displayFolder">📁</a> /
        <%
            String cumulativePath = "";
            for (int i = 0; i < pathComponents.length; i++) {
                cumulativePath += "/" + pathComponents[i];
        %>
        <a href="<%= request.getContextPath() %>/displayFolder<%= cumulativePath %>"><%= pathComponents[i].replace("-", " ")%></a> /
        <%
            }
        %>
    </div>
    <ul>
        <%
            for (Item item : folder.getContentsList("name", true))
            {
                if (item instanceof Note)
                {
                    %><li><a href="<%= request.getContextPath() %>/displayNote<%= pathString + "/" + item.getId()%>">📄 <%= item.getName() %></a></li><%
                }
                else if (item instanceof Folder)
                {
                    %><li><a href="<%= request.getContextPath() %>/displayFolder<%= pathString + "/" + item.getId()%>">📁 <%= item.getName() %></a></li><%
                }
            }
        %>
    </ul>
</div>
</body>
</html>
