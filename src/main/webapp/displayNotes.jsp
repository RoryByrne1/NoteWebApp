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
        List<Note> notesList = (List<Note>) request.getAttribute("notesList");
//        for (String category: notesList.keySet())
//        {
          for (Note note: notesList)
          {
      %>
      <li> <%=note.getTitle()%>
      </li>
      <%
        } %>
    </ul>
  </div>
</body>
</html>
