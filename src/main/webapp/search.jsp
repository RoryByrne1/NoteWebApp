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

  <jsp:include page="sorting.jsp"/>

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
