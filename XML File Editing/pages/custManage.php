<!DOCTYPE html>
<!--
 This document is a menu page that takes users to either the add a customer page
 or the search page:

 Filename: custManage.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 22/10/2020
-->
<?php
  session_start();
  if (!isset($_SESSION['user'])) {
    header('Location: login.php');
  }
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
          <ul class='navbar-nav'>
            <li>
              <a class='nav-link' href='./custManage.php'>Manage Customers</a>
            </li>
            <li>
              <a class='nav-link' href='./newRental.php'>Manage Rentals</a>
            </li>
          </ul>
        </div>
        <div class="flex-right">
          <ul class="navbar-nav d-flex fex-row-reverse">
            <?php
              echo "<div class='nav-link text-light'>".$_SESSION['user']."</div>";
              echo "<a class='nav-link' href='logout.php'>Logout</a>";
            ?>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container pt-5">
      <div class="row text-center">
        <div class="col-md-6">
          <h2>Create a New Customer Entry</h2>
          <p>Add a new customer entry to the xml file</p>
          <a href="./newCust.php" class="btn btn-primary">Create an Entry</a>
        </div>
        <div class="col-md-6">
          <h2>Edit an Existing Entry</h2>
          <p>Edit an existing customer's details or add a nominated driver</p>
          <a href="./search.php" class="btn btn-primary">Edit an Entry</a>
        </div>
      </div>
      <hr>
    </div>
  </body>
</html>
