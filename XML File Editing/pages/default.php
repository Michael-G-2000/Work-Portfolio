<!DOCTYPE html>
<!--
 This document is the homepage for Rent-a-Car:

 Filename: default.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 15/10/2020
-->
<?php
  session_start();
?>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>
    <title>Homepage</title>
    <link rel="stylesheet" href="bootstrap.css"/>
  </head>
  <body>
    <nav class="navbar navbar-expand-lg text-light bg-dark border-bottom">
      <div class="container">
        <a class="navbar-brand pl-3 pr-3 border-right text-light" href="default.html">
          <img src="../images/car.jpg" width="30" height="30" class="d-inline-block align-top">
          Rent-a-Car
        </a>
        <div class="collapse navbar-collapse">
          <?php
            if (isset($_SESSION['user'])) {
              echo "<ul class='navbar-nav'>";
              echo "<li>";
              echo "<a class='nav-link' href='./custManage.php'>Manage Customers</a>";
              echo "</li>";
              echo "<li>";
              echo "<a class='nav-link' href='./newRental.php'>Manage Rentals</a>";
              echo "</li>";
              echo "</ul>";
            }
          ?>
        </div>
        <div class="flex-right">
          <ul class="navbar-nav d-flex fex-row-reverse">
            <?php
              if (isset($_SESSION['user'])) {
                echo "<div class='nav-link text-light'>".$_SESSION['user']."</div>";
                echo "<a class='nav-link' href='logout.php'>Logout</a>";
              } else {
                echo "<a class='nav-link' href='login.php'>Login</a>";
              }
            ?>
          </ul>
        </div>
      </div>
    </nav>

    <div class="bg-dark text-light pt-5 pb-5">
      <div class="container text-center pt-5 pb-5">
        <h1 class="display-4">Rent-a-Car</h1>
        <p>Service so fast we have been fined for speeding <strong>487</strong> times with court costs upwards of <strong>$234,895</strong>!</p>
      </div>
    </div>

    <div class="container pt-5">
      <h3>This System provides the following functionality.  Login to begin.</h3>
      <div class="row text-center">
        <div class="col-md-4">
          <h2>View Records</h2>
          <p>Examine all past and current rental orders.</p>
          <img src="../images/coolCar1.jpg" width="200" height="200" class="rounded float-center">
        </div>
        <div class="col-md-4">
          <h2>Manage Customers</h2>
          <p>Add a new customer, add a nominated driver to a customer or update existing customer details</p>
          <img src="../images/coolCar2.jpg" width="200" height="200" class="rounded float-center">
        </div>
        <div class="col-md-4">
          <h2>Manage Rentals</h2>
          <p>Create a new rental record.</p>
          <img src="../images/coolCar3.jpg" width="200" height="200" class="rounded float-center">
        </div>
      </div>
      <hr>
    </div>
  </body>
</html>
