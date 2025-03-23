<%@ page import="ucl.ac.uk.classes.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <%
    Note note = (Note) request.getAttribute("note");
  %>
  <title><%=note.getName()%></title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
  <jsp:include page="header.jsp"/>
  <style>
    .note-contents {
      margin: 20px;
    }
    .text-block {
      white-space: pre-wrap;
      margin-top: 5px;
      margin-bottom: 5px;
    }
  </style>
</head>
<body>
<div>
  <%
    String pathString = (String) request.getAttribute("pathString");
    String[] pathComponents = pathString == null || pathString.isEmpty() ? new String[0] : pathString.substring(1).split("/");
  %>
  <h1><%= note.getName()%></h1>

  <div class="time-details">
    created: <%= note.getCreatedAtReadable(false) %> | last modified: <%= note.getLastEditedReadable(false) %>
  </div>

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
          <div class="text-block"><%= ((TextBlock) b).getText()%></div>
        <% } else if (b instanceof ImageBlock) { %>
          <img src="/<%= ((ImageBlock) b).getImagePath()%>" width="300" alt="<%= ((ImageBlock) b).getImagePath()%> 1">
        <% } else if (b instanceof URLBlock) { %>
          <%
            String url = ((URLBlock) b).getURL();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
              url = "http://" + url; // default to http if no protocol is specified
            }
          %>
          <a href="<%= url %>" target="_blank"><%= ((URLBlock) b).getURL() %></a>
        <%}%>
     <%}%>
  </div>

  <div class="button-container">
    <form action="/editNote<%= cumulativePath + "/" + note.getId() %>" method="get">
      <button class="btn" title="edit this note" id="editNote">edit note</button>
    </form>
  </div>
</div>
</body>
</html>
