<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Hello from the JSP page</title>
</head>
<body>
<div class="container mt-5">
  <h2>My Notes</h2>
  <ul>
    <%
      Map<String, List<Note>> notesList = (Map<String, List<Note>>) request.getAttribute("notesList");
      for (String category : notesList.keySet())
      {
    %>
    <li><strong><%= category %></strong>
      <ul>
        <%
          for (Note note : notesList.get(category))
          {
        %>
        <li><%= note.getTitle() %></li>
        <% } %>
      </ul>
    </li>
    <% } %>
  </ul>
</div>
</body>
</html>
