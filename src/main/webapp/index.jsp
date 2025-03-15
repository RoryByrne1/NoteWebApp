<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>home page</title>
  <style>
    body {
      margin-left: 5%;
    }
  </style>
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
  <a href="displayFolder">my notes</a>
</div>
</body>
</html>