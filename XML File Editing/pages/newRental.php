<!DOCTYPE html>
<!--
 This document is the form to create a new rental for Rent-a-Car:

 Filename: newRental.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 15/10/2020
-->
<?php
  session_start();
  if (!isset($_SESSION['user'])) {
    header('Location: login.php');
  }

  $wasError = false;
  $errorMsg = '';
  if (isset($_POST['submit'])) {
    $xmlDoc = new DOMDocument();
    $xmlDoc->load("../xml/rentalRecords.xml");

    $root = $xmlDoc->documentElement;

    if(!empty($_POST['rentID']) && !empty($_POST['dateBooked']) && !empty($_POST['pickDate']) && !empty($_POST['daysUsed']) && !empty($_POST['dayPrice'])) {
      $newRental = $xmlDoc->createElement('booking');

      $rentID = $xmlDoc->createAttribute('recordNum');
      $newRental->appendChild($rentID);
      $newRental->setAttribute('recordNum', $_POST['rentID']);

      $custID = $xmlDoc->createElement('customerNum');
      $custID->nodeValue = $_POST['custID'];

      $vehID = $xmlDoc->createElement('regoNum');
      $vehID->nodeValue = $_POST['vehID'];

      $bookDate = $xmlDoc->createElement('booked');
      $bookDate->nodeValue = $_POST['dateBooked'];

      $pickDate = $xmlDoc->createElement('pickUp');
      $pickDate->nodeValue = $_POST['pickDate'];

      $daysRented = $xmlDoc->createElement('daysRented');
      $daysRented->nodeValue = $_POST['daysUsed'];

      $dailyCost = $xmlDoc->createElement('dailyCost');
      $dailyCost->nodeValue = $_POST['dayPrice'];

      $dates = $xmlDoc->createElement('date');

      $dates->appendChild($bookDate);
      $dates->appendChild($pickDate);

      $newRental->appendChild($custID);
      $newRental->appendChild($vehID);
      $newRental->appendChild($dates);
      $newRental->appendChild($daysRented);
      $newRental->appendChild($dailyCost);

      $root->appendChild($newRental);
      $xmlDoc->save(realpath('../xml/rentalRecords.xml'));

    } else {
      $wasError = true;
      $errorMsg = "Please make sure each field is filled in.";
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
        } else if (!$wasError && isset($_POST['submit']) && $_POST['']) {
          echo "<div class='card border-success'>";
          echo "<div class='card-header bg-success text-light'><h4>Success</h4></div>";
          echo "<div class='card-body'>";
          echo "<p class='card-text'>The new rental record was successfully added</p>";
          echo "<a href='default.php'>Return to the homepage</a>";
          echo "</div>";
          echo "</div>";
        }
      ?>

      <h1>Create a New Rental</h1>
      <form class="form" action="newRental.php" method="post">
        <div class="row">
          <div class="col">
            <label for="rentID">Rental ID: </label>
            <input type="number" class="form-control" name="rentID" id="rentID"> <br>
          </div>
          <div class="col">
            <label for="custID">Customer ID: </label>
            <select class="form-control" name="custID" id="custID">
              <?php
                $custDoc = simplexml_load_file('../xml/customers.xml');
                foreach ($custDoc->customer as $customer) {
                  echo "<option value='".$customer['custNum']."'>".$customer['custNum']."</option>";
                }
              ?>
            </select> <br>
          </div>
          <div class="col">
            <label for="vehID">Vehicle ID: </label>
            <select class="form-control" name="vehID" id="vehID">
              <?php
                $vehDoc = simplexml_load_file('../xml/vehicles.xml');
                foreach ($vehDoc->vehicle as $vehicle) {
                  echo "<option value='".$vehicle['rego']."'>".$vehicle['rego']."</option>";
                }
              ?>
            </select> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="dateBooked">Date Booked: </label>
            <input type="date" class="form-control" name="dateBooked" id="dateBooked"> <br>
          </div>

          <div class="col">
            <label for="pickDate">Pickup Date: </label>
            <input type="date" class="form-control" name="pickDate" id="pickDate"> <br>
          </div>
        </div>

        <div class="row">
         <div class="col">
           <label for="daysUsed">Days Reserved: </label>
           <input type="number" class="form-control" name="daysUsed" id="daysUsed"> <br>
         </div>
         <div class="col">
           <label for="dayPrice">Price per Day: </label>
           <input type="text" class="form-control" name="dayPrice" id="dayPrice"> <br>
         </div>
       </div>

       <input type="submit" class="btn btn-primary" name="submit">
      </form>
    </div>
  </body>
</html>
