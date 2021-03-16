<!DOCTYPE html>
<!--
 This document is the form to create a new customer for Rent-a-Car:

 Filename: newCust.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 23/10/2020
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
    $xmlDoc->load("../xml/customers.xml");

    $root = $xmlDoc->documentElement;

    if(!empty($_POST['custID']) && !empty($_POST['fName']) && !empty($_POST['sName']) && !empty($_POST['num1']) && !empty($_POST['street']) &&
      !empty($_POST['suburb']) && !empty($_POST['state']) && !empty($_POST['pcode']) && !empty($_POST['email']) && !empty($_POST['licence'])) {
      $newCust = $xmlDoc->createElement('customer');

      $custID = $xmlDoc->createAttribute('custNum');
      $newCust->appendChild($custID);
      $newCust->setAttribute('custNum', $_POST['custID']);

      $name = $xmlDoc->createElement('name');

      if (!empty($_POST['title'])) {
        $title = $xmlDoc->createElement('title');
        $title->nodeValue = $_POST['title'];
        $name->appendChild($title);
      }

      $fName = $xmlDoc->createElement('firstName');
      $fName->nodeValue = $_POST['fName'];
      $name->appendChild($fName);

      if (!empty($_POST['mName'])) {
        $mName = $xmlDoc->createElement('middleName');
        $mName->nodeValue = $_POST['mName'];
        $name->appendChild($mName);
      }

      $lName = $xmlDoc->createElement('lastName');
      $lName->nodeValue = $_POST['sName'];
      $name->appendChild($lName);

      $street = $xmlDoc->createElement('street');
      $street->nodeValue = $_POST['street'];

      $suburb = $xmlDoc->createElement('suburb');
      $suburb->nodeValue = $_POST['suburb'];

      $state = $xmlDoc->createElement('state');
      $state->nodeValue = $_POST['state'];

      $pcode = $xmlDoc->createElement('postcode');
      $pcode->nodeValue = $_POST['pcode'];

      $address = $xmlDoc->createElement('address');
      $address->appendChild($street);
      $address->appendChild($suburb);
      $address->appendChild($state);
      $address->appendChild($pcode);

      $email = $xmlDoc->createElement('email');
      $email->nodeValue = $_POST['email'];

      $licence = $xmlDoc->createElement('licence');
      $licence->nodeValue = $_POST['licence'];

      $numbers = $xmlDoc->createElement('phoneNumbers');

      $num1 = $xmlDoc->createElement('number');
      $num1->nodeValue = $_POST['num1'];
      $numbers->appendChild($num1);

      if (!empty($_POST['num2'])) {
        $num2 = $xmlDoc->createElement('number');
        $num2->nodeValue = $_POST['num2'];
        $numbers->appendChild($num2);
      }

      if (!empty($_POST['num3'])) {
        $num3 = $xmlDoc->createElement('number');
        $num3->nodeValue = $_POST['num3'];
        $numbers->appendChild($num3);
      }

      $newCust->appendChild($name);
      $newCust->appendChild($address);
      $newCust->appendChild($licence);
      $newCust->appendChild($email);
      $newCust->appendChild($numbers);

      $root->appendChild($newCust);
      $xmlDoc->save(realpath('../xml/customers.xml'));

    } else {
      $wasError = true;
      $errorMsg = "Please make sure each non optional field is filled in.";
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
        } else if (!$wasError && isset($_POST['submit'])) {
          echo "<div class='card border-success'>";
          echo "<div class='card-header bg-success text-light'><h4>Success</h4></div>";
          echo "<div class='card-body'>";
          echo "<p class='card-text'>The new customer was successfully added.</p>";
          echo "<form class='pb-3' action='nomDrivers.php' method='post'>";
          echo "<input type='text' class='d-none' name='custNum' id='custNum' value='".$_POST['custID']."'>";
          echo "<input type='submit' class='btn btn-primary align-middle' name='add' value='Add a Nominated Driver'>";
          echo "</form>";
          echo "<a href=default.php class='btn btn-primary align-middle'>Return Home</a>";
          echo "</div>";
          echo "</div>";
        }
      ?>

      <h1>Create a New Customer</h1>
      <form class="form" action="newCust.php" method="post">
        <div class="row">
          <div class="col">
            <label for="custID">Customer ID: </label>
            <input type="text" class="form-control" name="custID" id="custID"> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="title">Title (Optional): </label>
            <input type="text" class="form-control" name="title" id="title"> <br>
          </div>

          <div class="col">
            <label for="fName">First Name: </label>
            <input type="text" class="form-control" name="fName" id="fName"> <br>
          </div>

          <div class="col">
            <label for="mName">Middle Name (Optional): </label>
            <input type="text" class="form-control" name="mName" id="mName"> <br>
          </div>

          <div class="col">
            <label for="lName">Last Name: </label>
            <input type="text" class="form-control" name="sName" id="sName"> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="num1">Phone Number 1: </label>
            <input type="number" class="form-control" name="num1" id="num1"> <br>
          </div>

          <div class="col">
            <label for="num2">Phone Number 2 (Optional): </label>
            <input type="number" class="form-control" name="num2" id="num2"> <br>
          </div>

          <div class="col">
            <label for="num3">Phone Number 3 (Optional): </label>
            <input type="number" class="form-control" name="num3" id="num3"> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="street">Street Address: </label>
            <input type="text" class="form-control" name="street" id="street"> <br>
          </div>

          <div class="col">
            <label for="suburb">Suburb: </label>
            <input type="text" class="form-control" name="suburb" id="suburb"> <br>
          </div>

          <div class="col">
            <label for="state">State: </label>
            <select class="form-control" name="state" id="state">
              <option value="NSW">NSW</option>
              <option value="QLD">QLD</option>
              <option value="TAS">TAS</option>
              <option value="VIC">VIC</option>
              <option value="NT">NT</option>
              <option value="ACT">ACT</option>
              <option value="WA">WA</option>
              <option value="SA">SA</option>
            </select> <br>
          </div>

          <div class="col">
            <label for="pcode">Postcode: </label>
            <input type="number" class="form-control" name="pcode" id="pcode"> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="email">Email: </label>
            <input type="text" class="form-control" name="email" id="email"> <br>
          </div>

          <div class="col">
            <label for="licence">Licence ID: </label>
            <input type="text" class="form-control" name="licence" id="licence"> <br>
          </div>
        </div>

        <p>You will be asked to fill out the nominated driver's details when this form has been filled in correctly, if applicable.</p>
        <input type="submit" class="btn btn-primary" name="submit">
      </form>
    </div>
  </body>
</html>
