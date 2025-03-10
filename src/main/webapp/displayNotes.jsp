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
    <h2>my notes</h2>
    <ul>
      <%
        Object notesListObj = request.getAttribute("notesList");
        System.out.println("notesList object class: " + notesListObj.getClass().getName());
        Map<String, List<Note>> notesList = (Map<String, List<Note>>) notesListObj;
        for (String category: notesList.keySet())
        {
          for (Note note: notesList.get(category))
          {
      %>
      <li>
        <%=note.getTitle()%>
      </li>
      <% }
        } %>
    </ul>
  </div>
</body>
</html>
