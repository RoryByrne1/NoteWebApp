<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sorting">
    <span><b>sort:</b></span>
    <%
        String sortBy = (String) request.getAttribute("sortBy");
        boolean ascending = (boolean) request.getAttribute("ascending");

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