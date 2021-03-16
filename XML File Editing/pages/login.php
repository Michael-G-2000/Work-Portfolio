<!DOCTYPE html>
<!--
 This document is the login page for Rent-a-Car:

 Filename: login.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 15/10/2020
-->
<?php
  $error = '';
  session_start();
  if (isset($_SESSION['user'])) {
    header("Location: default.php");
  }

  if(isset($_POST['submit'])) {
    $xmlDoc = simplexml_load_file('../xml/staff.xml');

    if(!empty($_POST['name']) && !empty($_POST['pass'])) {
      foreach ($xmlDoc->credential as $credential) {
        if (strcmp($credential->name, $_POST['name']) == 0 && strcmp($credential->password, $_POST['pass']) == 0) {
          $_SESSION['user'] = $_POST['name'];
          header("Location: default.php");
        }
        $error = "Inputs do not match existing records";
      }
    } else {
      $error = "Please make sure each field is filled in. <br/>";
    }
  }
?>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>
    <title>Login</title>
    <link rel="stylesheet" href="bootstrap.css"/>
  </head>
  <body>
    <nav class="navbar navbar-expand-lg text-light bg-dark border-bottom">
      <div class="container">
        <a class="navbar-brand pl-3 pr-3 border-right text-light" href="default.php">
          <img src="../images/car.jpg" width="30" height="30" class="d-inline-block align-top">
          Rent-a-Car
        </a>
        <div class="flex-right">
          <ul class="navbar-nav d-flex fex-row-reverse">
            <li>
              <a class='nav-link' href='login.php'>Login</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container pt-5">
      <h1>Login</h1>
      <form class="form pb-4" action="login.php" method="post">
        <label for="name">Name: </label>
        <input type="text" class="form-control" name="name" id="name"> <br>
        <label for="pass">Password: </label>
        <input type="text" class="form-control" name="pass" id="pass"> <br>
        <input type="submit" class="btn btn-primary" name="submit">
      </form>
      <?php
        if(!strcmp($error, '') == 0) {
          echo "<div class='card border-danger'>";
          echo "<div class='card-header bg-danger text-light'><h4>Error</h4></div>";
          echo "<div class='card-body'>";
          echo "<p class='card-text'>".$error."</p>";
          echo "</div>";
          echo "</div>";
        }
      ?>
    </div>
  </body>
</html>
