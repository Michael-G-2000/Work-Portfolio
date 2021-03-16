<!DOCTYPE html>
<!--
 This document is the form to add a nominated driver to a customer:

 Filename: nomDrivers.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 24/10/2020
-->
<?php
  session_start();
  if (!isset($_SESSION['user'])) {
    header('Location: login.php');
  }

  if (!isset($_POST['custNum']) || empty($_POST['custNum'])) {
    header('Location: search.php');
  }

  $wasError = false;
  $errorMsg = '';

  $_SESSION['custNum'] = $_POST['custNum'];

  if (isset($_POST['submit'])) {
    $xmlDoc = new DOMDocument();
    $xmlDoc->load("../xml/customers.xml");

    if(!empty($_POST['fName']) && !empty($_POST['sName']) && !empty($_POST['num']) && !empty($_POST['street']) &&
      !empty($_POST['suburb']) && !empty($_POST['state']) && !empty($_POST['pcode']) && !empty($_POST['licence'])) {

        $customers = $xmlDoc->getElementsByTagName('customer');

        foreach ($customers as $customer) {
          if (strcmp($customer->getAttribute('custNum'), $_POST['custNum']) == 0) {
            $fName = $xmlDoc->createElement('firstName');
            $fName->nodeValue = $_POST['fName'];

            $lName = $xmlDoc->createElement('lastName');
            $lName->nodeValue = $_POST['sName'];

            $street = $xmlDoc->createElement('street');
            $street->nodeValue = $_POST['street'];

            $suburb = $xmlDoc->createElement('suburb');
            $suburb->nodeValue = $_POST['suburb'];

            $state = $xmlDoc->createElement('state');
            $state->nodeValue = $_POST['state'];

            $pcode = $xmlDoc->createElement('postcode');
            $pcode->nodeValue = $_POST['pcode'];

            $licence = $xmlDoc->createElement('licence');
            $licence->nodeValue = $_POST['licence'];

            $num = $xmlDoc->createElement('number');
            $num->nodeValue = $_POST['num'];

            $name = $xmlDoc->createElement('name');
            $name->appendChild($fName);
            $name->appendChild($lName);

            $address = $xmlDoc->createElement('address');
            $address->appendChild($street);
            $address->appendChild($suburb);
            $address->appendChild($state);
            $address->appendChild($pcode);

            $driver = $xmlDoc->createElement('driver');
            $driver->appendChild($name);
            $driver->appendChild($address);
            $driver->appendChild($licence);
            $driver->appendChild($num);

            if ($customer->getElementsByTagName('nominatedDrivers')->length == 0) {
              $nomDriver = $xmlDoc->createElement('nominatedDrivers');
              $nomDriver->appendChild($driver);

              $customer->appendChild($nomDriver);
            } else {
              $nomDriver = $customer->getElementsByTagName('nominatedDrivers')->item(0);
              $nomDriver->appendChild($driver);
            }
          }
        }

        $xmlDoc->save(realpath('../xml/customers.xml'));

    } else {
      $wasError = true;
      $errorMsg = "Please make sure every field is filled in.";
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
          echo "<p class='card-text'>The new nominated driver was successfully added to customer <strong>".$_SESSION['custNum']."</strong>.</p>";
          echo "<p class='card-text'>Fill out the form again to add another driver.</p>";
          echo "</div>";
          echo "</div>";
        }
      ?>

      <h1>Add a Nominated Driver</h1>
      <form class="form" action="nomDrivers.php" method="post">
        <div class="row">
          <div class="col">
            <label for="custNum">Add driver to customer: </label>
            <input type="text" class="form-control" name="custNum" id="custNum" value="<?php echo $_SESSION['custNum']; ?>" readonly> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="fName">First Name: </label>
            <input type="text" class="form-control" name="fName" id="fName"> <br>
          </div>

          <div class="col">
            <label for="lName">Last Name: </label>
            <input type="text" class="form-control" name="sName" id="sName"> <br>
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
            <label for="num">Phone Number: </label>
            <input type="number" class="form-control" name="num" id="num"> <br>
          </div>

          <div class="col">
            <label for="licence">Licence ID: </label>
            <input type="text" class="form-control" name="licence" id="licence"> <br>
          </div>
        </div>
        <input type="submit" class="btn btn-primary" name="submit">
      </form>
    </div>
  </body>
</html>
