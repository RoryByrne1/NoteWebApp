<script>
  window.onload = function() {
    document.getElementById("datetime").textContent = new Date().toLocaleString('en-GB', {dateStyle:"short"});
  }
</script>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="header">
  <a href="javascript:window.history.back()" class="header-button-smaller">🡨</a>
  <a href="<%= request.getContextPath() %>/" class="header-button">home</a>
  <a href="<%= request.getContextPath() %>/displayFolder" class="header-button">my notes</a>
  <a href="<%= request.getContextPath() %>/search" class="header-button">search</a>
  <a class="date" id="datetime"></a>
</div>