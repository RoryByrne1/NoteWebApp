<%@ page import="ucl.ac.uk.classes.Note" %>
<%@ page import="ucl.ac.uk.classes.Block" %>
<%@ page import="ucl.ac.uk.classes.ImageBlock" %>
<%@ page import="ucl.ac.uk.classes.TextBlock" %>
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
    <%
        Note note = (Note) request.getAttribute("note");
    %>
    <h1><%= note.getTitle()%></h1>
    <div class="button-container">
        <form action=".." method="get">
            <button class="btn" id="editNote">edit note</button>
        </form>
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
    <a href="displayNotes" class="back-link">&larr; back</a>
</div>
</body>
</html>
