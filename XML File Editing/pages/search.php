<!DOCTYPE html>
<!--
 This document is the form to query customers.xml:

 Filename: search.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 24/10/2020
-->
<?php
  session_start();
  if (!isset($_SESSION['user'])) {
    header('Location: login.php');
  }

  $wasError = false;
  $errorMsg = '';
  if (isset($_POST['submit'])) {
    $xmlDoc = simplexml_load_file('../xml/customers.xml');

    if (empty($_POST['custID']) && empty($_POST['sName']) && empty($_POST['licence'])) {
      $wasError = true;
      $errorMsg = "Please make sure one field is entered";
    } else {
      if (!empty($_POST['custID'])) {
        $query = $xmlDoc->xpath("//customer[contains(@custNum, '". $_POST['custID'] ."')]");
      } else if (!empty($_POST['sName'])) {
        $query = $xmlDoc->xpath("//customer/name/lastName[contains(text(), '". $_POST['sName'] ."')]/ancestor::customer");
      } else if (!empty($_POST['licence'])) {
        $query = $xmlDoc->xpath("//customer/licence[contains(text(), '". $_POST['licence'] ."')]/ancestor::customer");
      }
    }
  }
?>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8"/>
    <title>Create a New Rental</title>
    <link rel="stylesheet" href="bootstrap.css"/>
  </head>
  <body>
    <nav class="navbar navbar-expand-lg text-light bg-dark border-bottom">
      <div class="container">
        <a class="navbar-brand pl-3 pr-3 border-right text-light" href="default.php">
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
      <?php
        if($wasError && isset($_POST['submit'])) {
          echo "<div class='card border-danger'>";
          echo "<div class='card-header bg-danger text-light'><h4>Error</h4></div>";
          echo "<div class='card-body'>";
          echo "<p class='card-text'>".$errorMsg."</p>";
          echo "</div>";
          echo "</div>";
        }
      ?>

      <h1>Search for a Customer</h1>
      <p>
        Search for a customer by their customer ID, surname or licence.
      </p>
      <p>
      	Please note that the search will only use the first entered input it detects
        looking at each field from left to right. The search is case sensative.
      </p>
      <form class="form" action="search.php" method="post">
        <div class="row">
          <div class="col">
            <label for="custID">Customer ID: </label>
            <input type="text" class="form-control" name="custID" id="custID"> <br>
          </div>

          <div class="col">
            <label for="sName">Surname: </label>
            <input type="text" class="form-control" name="sName" id="sName"> <br>
          </div>

          <div class="col">
            <label for="licence">Licence: </label>
            <input type="text" class="form-control" name="licence" id="licence"> <br>
          </div>
        </div>
        <input type="submit" class="btn btn-primary" name="submit">
      </form>
      <hr>
      <?php
        if(isset($_POST['submit']) && !$wasError) {
          if (count($query) > 0) {
            echo "<div class='row'>";
            echo "<div class='col'><strong>Customer ID</strong></div>";
            echo "<div class='col'><strong>Surname</strong></div>";
            echo "<div class='col'><strong>Licence</strong></div>";
            echo "<div class='col'></div>";
            echo "<div class='col'></div>";
            echo "</div>";
            echo "<hr>";
            for ($i=0; $i < count($query); $i++) {
              echo "<div class='row pb-3'>";
              echo "<div class='col'>".$query[$i]['custNum']."</div>";
              echo "<div class='col'>".$query[$i]->name->lastName."</div>";
              echo "<div class='col'>".$query[$i]->licence."</div>";
              echo "<div class='col'>";
              echo "<form action='editCust.php' method='post'>";
              echo "<input type='text' class='d-none' name='custNum' id='custNum' value='".$query[$i]['custNum']."'>";
              echo "<input type='submit' class='btn btn-primary align-middle' name='edit' value='Edit Details'>";
              echo "</form>";
              echo "</div>";
              echo "<div class='col'>";
              echo "<form action='nomDrivers.php' method='post'>";
              echo "<input type='text' class='d-none' name='custNum' id='custNum' value='".$query[$i]['custNum']."'>";
              echo "<input type='submit' class='btn btn-primary align-middle' name='add' value='Add Nominated Drivers'>";
              echo "</form>";
              echo "</div>";
              echo "</div>";
            }
          } else {
            echo "<div>";
            echo "No results were found that matched or were similar to the inputs provided";
            echo "</div>";
          }
        }
      ?>
    </div>
  </body>
</html>
