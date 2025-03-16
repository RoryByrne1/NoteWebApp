<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="ucl.ac.uk.classes.Block" %>
<%@ page import="ucl.ac.uk.classes.ImageBlock" %>
<%@ page import="ucl.ac.uk.classes.TextBlock" %>
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
      color: #555; /* Darker grey on hover */
    }
  </style>
</head>
<body>
<div>
  <%
    String pathString = (String) request.getAttribute("pathString");
    String[] pathComponents = pathString == null || pathString.isEmpty() ? new String[0] : pathString.substring(1).split("/");
    Note note = (Note) request.getAttribute("note");
  %>
  <h1><%= note.getName()%></h1>
  <div class="button-container">
    <form action=".." method="get">
      <button class="btn" id="editNote">edit note</button>
    </form>
  </div>
  <div class="breadcrumbs">
    <a href="<%= request.getContextPath() %>/displayFolder">📁</a> /
    <%
      String cumulativePath = "";
      for (int i = 0; i < pathComponents.length - 1; i++) {
        cumulativePath += "/" + pathComponents[i];
    %>
    <a href="<%= request.getContextPath() %>/displayFolder<%= cumulativePath %>"><%= pathComponents[i].replace("-", " ") %></a> /
    <%
      }
    %>
    <a href="<%= request.getContextPath() %>/displayNote<%= cumulativePath + "/" + note.getId() %>"><%= note.getName() %></a>
  </div>
  <%
    for (Block b: note.getBlocksList())
    {
      if (b instanceof TextBlock)
      {
  %>
  <p><%= ((TextBlock) b).getText()%></p>
  <%
  }
  else if (b instanceof ImageBlock)
  {
  %>
  <p><%= ((ImageBlock) b).getImagePath()%></p>
  <%
      }
    }
  %>
</div>
</body>
</html>
