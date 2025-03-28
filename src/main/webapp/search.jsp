<%@ page import="java.util.List" %>
<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>search notes</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
  <jsp:include page="header.jsp"/>
</head>
<body>
<div>
  <%
    List<Map<String, Object>> notesMapList = (List<Map<String, Object>>) request.getAttribute("notesMapList");
    String sortBy = (String) request.getAttribute("sortBy");
    boolean ascending = (boolean) request.getAttribute("ascending");
    String query = (String) request.getAttribute("query");
    String parameters =  "?sort=" + sortBy + "&order=" + (ascending ? "asc" : "desc") + "&q=" + query;
  %>

  <form action="<%= request.getContextPath() %>/search<%= parameters %>" method="get">
    <input type="hidden" name="sort" value="<%= sortBy %>">
    <input type="hidden" name="order" value="<%= ascending ? "asc" : "desc" %>">
    <input type="text" id="query" name="q" value="<%= query %>" placeholder="enter search query"/>
    <button class="query-button" title="search notes for query" id="search" style="font-size: 20px">🔎︎</button>
  </form>

  <div class="sorting">
    <span><b>sort:</b></span>
    <%
      String nameArrow = sortBy.equals("name") ? (ascending ? " ↑" : " ↓") : "  ";
      String modifiedArrow = sortBy.equals("lastEdited") ? (ascending ? " ↑" : " ↓") : "  ";
      String dateArrow = sortBy.equals("createdAt") ? (ascending ? " ↑" : " ↓") : "  ";

      String nextOrder = ascending ? "desc" : "asc";

      String nameOrder = sortBy.equals("name") ? nextOrder : "asc";
      String modifiedOrder = sortBy.equals("lastEdited") ? nextOrder : "asc";
      String dateOrder = sortBy.equals("createdAt") ? nextOrder : "asc";
    %>

    <a class="nice-link" href="?sort=name&order=<%= nameOrder %>&q=<%= query %>">name<%= "  " + nameArrow %></a> |
    <a class="nice-link" href="?sort=lastEdited&order=<%= modifiedOrder %>&q=<%= query %>">last modified<%= modifiedArrow %></a> |
    <a class="nice-link" href="?sort=createdAt&order=<%= dateOrder %>&q=<%= query %>">date created<%= dateArrow %></a>
  </div>

  <div>
    <p><%= notesMapList.size() %> results</p>
  </div>

  <div class="notes-container">
    <%
      Note note;
      String pathString;
      for (Map<String, Object> noteMap: notesMapList)
      {
        note = (Note) noteMap.get("note");
        pathString = String.join("/", (List<String>) noteMap.get("path"));
    %>
    <div class="note">
      <div class="note-path" style="color: grey; font-size: 13px;">
        / <%= pathString.replaceAll("-"," ").replaceAll("/"," / ") %>
      </div>
      <a class="nice-link" href="<%= request.getContextPath() %>/displayNote/<%= pathString + "/" + note.getId()%>" style="font-size: 20px; font-weight: bold;">
        <%= note.getName() %>
      </a>
      <div class="note-summary">
        <%= note.getSummary() %>
      </div>
      <div class="note-details" style="color: grey; font-size: 13px; margin-top: 4px;">
        created: <%= note.getCreatedAtReadable(false) %> | last modified: <%= note.getLastEditedReadable(false) %>
      </div>
    </div>
    <% } %>
  </div>
</div>
</body>
</html>
