<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>home page</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
  <jsp:include page="header.jsp"/>
</head>
<body>
<div>
  <%
    List<Map<String, Object>> pinnedMapList = (List<Map<String, Object>>) request.getAttribute("pinnedMapList");
    List<Map<String, Object>> recentsMapList = (List<Map<String, Object>>) request.getAttribute("recentsMapList");
  %>

  <h1>pinned</h1>

  <jsp:include page="sorting.jsp"/>

  <div class="notes-container">
    <%
      Note note;
      String pathString;
      for (Map<String, Object> noteMap: pinnedMapList)
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
    <% }
    if (pinnedMapList.isEmpty())
    { %>
    <p>no notes pinned</p>
    <% } %>
  </div>

  <h1>recents</h1>

  <div class="notes-container">
    <%
      for (Map<String, Object> noteMap: recentsMapList)
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
    <% }
      if (recentsMapList.isEmpty())
      { %>
    <p>no recent notes</p>
    <% } %>
  </div>
</div>
</body>
</html>
