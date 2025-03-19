<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>home page</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles.css">
  <script>
    window.onload = function() {
      document.getElementById("datetime").textContent = new Date().toLocaleString('en-GB', {dateStyle:"short"});
      }
  </script>
</head>
<body>
<div>
  <p id="datetime"></p>
  <h1>home page</h1>
  <a class="nice-link" href="displayFolder?sortBy=name&ascending=true">my notes</a>
</div>
</body>
</html>