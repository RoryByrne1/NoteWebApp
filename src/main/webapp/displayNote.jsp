<%@ page import="ucl.ac.uk.classes.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <%
    Note note = (Note) request.getAttribute("note");
  %>
  <title><%=note.getName()%></title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
</head>
<body>
<div>
  <%
    String pathString = (String) request.getAttribute("pathString");
    String[] pathComponents = pathString == null || pathString.isEmpty() ? new String[0] : pathString.substring(1).split("/");
  %>
  <h1><%= note.getName()%></h1>
  <div class="breadcrumbs">
    <a class="nice-link" href="<%= request.getContextPath() %>/displayFolder">📁</a> /
    <%
      String cumulativePath = "";
      for (int i = 0; i < pathComponents.length - 1; i++) {
        cumulativePath += "/" + pathComponents[i];
    %>
    <a class="nice-link" href="<%= request.getContextPath() %>/displayFolder<%= cumulativePath %>"><%= pathComponents[i].replace("-", " ") %></a> /
    <%
      }
    %>
    <a class="nice-link" href="<%= request.getContextPath() %>/displayNote<%= cumulativePath + "/" + note.getId() %>"><%= note.getName() %></a>
  </div>
  <div class="note-contents">
    <%
      for (Block b: note.getBlocksList())
      {
        if (b instanceof TextBlock) { %>
          <p><%= ((TextBlock) b).getText()%></p>
        <% } else if (b instanceof ImageBlock) { %>
          <p><%= ((ImageBlock) b).getImagePath()%></p>
        <% } else if (b instanceof URLBlock) { %>
          <a href="<%= ((URLBlock) b).getURL() %>" target="_blank"><%= ((URLBlock) b).getURL() %></a>
        <%}%>
     <%}%>
  </div>
  <div class="button-container">
    <form action="/editNote<%= cumulativePath + "/" + note.getId() %>" method="get">
      <button class="btn" id="editNote">edit note</button>
    </form>
  </div>
</div>
</body>
</html>
