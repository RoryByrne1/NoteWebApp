<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>my notes</title>
  <style>
    body {
      margin-left: 5%;
    }
    .button-container {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
    }
    .button-container form {
      margin: 0;
    }
  </style>
</head>
<body>
<div>
  <h1>my notes</h1>
  <div class="button-container">
    <form action="displayNotes.html" method="get">
      <button class="btn" id="addCategory">add category</button>
    </form>
    <form action="displayNotes.html" method="get">
      <button class="btn" id="addNote">add note</button>
    </form>
  </div>
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
        <li><a href="viewNote?noteId=<%= note.getId() %>"><%= note.getTitle() %></a></li>
        <% } %>
      </ul>
    </li>
    <% } %>
  </ul>
  <a href="index.jsp" class="back-link">&larr; back</a>
</div>
</body>
</html>
