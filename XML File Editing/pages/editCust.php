<!DOCTYPE html>
<!--
 This document is the form to edit an existing customer for Rent-a-Car:

 Filename: editCust.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 25/10/2020
-->
<?php
  session_start();
  if (!isset($_SESSION['user'])) {
    header('Location: login.php');
  }

  if (!isset($_POST['custNum']) || empty($_POST['custNum'])) {
    header('Location: search.php');
  }

  $_SESSION['custNum'] = $_POST['custNum'];

  $wasError = false;
  $errorMsg = '';

  if (isset($_POST['submit'])) {
    $xmlDom = new DOMDocument();
    $xmlDom->load("../xml/customers.xml");

    $customers = $xmlDom->getElementsByTagName('customer');

    foreach ($customers as $customer) {
      if (strcmp($customer->getAttribute('custNum'), $_POST['custNum']) == 0) {

        if (!empty($_POST['title'])) {
          if ($customer->getElementsByTagName('title')->length == 0) {
            $title = $xmlDom->createElement('title');
            $title->nodeValue = $_POST['title'];

            $name = $customer->getElementsByTagName('name')[0];
            $fname = $customer->getElementsByTagName('firstName')[0];

            $name->insertBefore($title, $fname);
          } else {
            $customer->getElementsByTagName('title')[0]->nodeValue = $_POST['title'];
          }
        } else {
          if ($customer->getElementsByTagName('title')->length == 1) {
            $title = $customer->getElementsByTagName('title')[0];
            $customer->getElementsByTagName('name')[0]->removeChild($title);
          }
        }

        if (!empty($_POST['fName'])) {
          $customer->getElementsByTagName('firstName')[0]->nodeValue = $_POST['fName'];
        } else {
          $wasError = true;
          $errorMsg = 'First name must not be empty';
        }

        if (!empty($_POST['mName'])) {
          if ($customer->getElementsByTagName('middleName')->length == 0) {
            $mName = $xmlDom->createElement('middleName');
            $mName->nodeValue = $_POST['mName'];

            $name = $customer->getElementsByTagName('name')[0];
            $lName = $customer->getElementsByTagName('lastName')[0];

            $name->insertBefore($mName, $lName);
          } else {
            $customer->getElementsByTagName('middleName')[0]->nodeValue = $_POST['mName'];
          }
        } else {
          if ($customer->getElementsByTagName('middleName')->length == 1) {
            $mName = $customer->getElementsByTagName('middleName')[0];
            $customer->getElementsByTagName('name')[0]->removeChild($mName);
          }
        }

        if (!empty($_POST['sName'])) {
          $customer->getElementsByTagName('lastName')[0]->nodeValue = $_POST['sName'];
        } else {
          $wasError = true;
          $errorMsg = 'Last name must not be empty';
        }

        if (!empty($_POST['num1'])) {
          $customer->getElementsByTagName('number')[0]->nodeValue = $_POST['num1'];
        } else {
          $wasError = true;
          $errorMsg = 'Number 1 name must not be empty';
        }

        if (!empty($_POST['num2'])) {
          if ($customer->getElementsByTagName('number')->length < 2) {
            $num2 = $xmlDom->createElement('number');
            $num2->nodeValue = $_POST['num2'];

            $customer->getElementsByTagName('phoneNumbers')->item(0)->appendChild($num2);
          } else {
            $customer->getElementsByTagName('number')[1]->nodeValue = $_POST['num2'];
          }
        }

        if (!empty($_POST['num3'])) {
          if ($customer->getElementsByTagName('number')->length < 3) {
            $num3 = $xmlDom->createElement('number');
            $num3->nodeValue = $_POST['num3'];

            $customer->getElementsByTagName('phoneNumbers')->item(0)->appendChild($num3);
          } else {
            $customer->getElementsByTagName('number')[2]->nodeValue = $_POST['num3'];
          }
        } else {
          if ($customer->getElementsByTagName('number')->length == 3) {
            $num3 = $customer->getElementsByTagName('number')[2];
            $customer->getElementsByTagName('phoneNumbers')[0]->removeChild($num3);
          }
        }

        if (empty($_POST['num2'])) {
          if ($customer->getElementsByTagName('number')->length > 1) {
            $num2 = $customer->getElementsByTagName('number')[1];
            $customer->getElementsByTagName('phoneNumbers')[0]->removeChild($num2);
          }
        }

        if (!empty($_POST['street'])) {
          $customer->getElementsByTagName('street')[0]->nodeValue = $_POST['street'];
        } else {
          $wasError = true;
          $errorMsg = 'Street name must not be empty';
        }

        if (!empty($_POST['suburb'])) {
          $customer->getElementsByTagName('suburb')[0]->nodeValue = $_POST['suburb'];
        } else {
          $wasError = true;
          $errorMsg = 'Suburb name must not be empty';
        }

        if (!empty($_POST['state'])) {
          $customer->getElementsByTagName('state')[0]->nodeValue = $_POST['state'];
        }

        if (!empty($_POST['pcode'])) {
          $customer->getElementsByTagName('postcode')[0]->nodeValue = $_POST['pcode'];
        } else {
          $wasError = true;
          $errorMsg = 'Postcode must not be empty';
        }
      }
    }

    if (!$wasError) {
      $xmlDom->save(realpath('../xml/customers.xml'));
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
          echo "<p class='card-text'>Changes were <strong>NOT</strong> saved.</p>";
          echo "</div>";
          echo "</div>";
        } else if (!$wasError && isset($_POST['submit'])) {
          echo "<div class='card border-success'>";
          echo "<div class='card-header bg-success text-light'><h4>Success</h4></div>";
          echo "<div class='card-body'>";
          echo "<p class='card-text'>The customer's details were updated.</p>";
          echo "</div>";
          echo "</div>";
        }
      ?>

      <?php
        $xmlDoc = simplexml_load_file('../xml/customers.xml');
        $custDetails = $xmlDoc->xpath("//customer[@custNum = '". $_SESSION['custNum'] ."']");
      ?>
      <h1>Update Customer Details</h1>
      <form class="form" action="editCust.php" method="post">
        <div class="row">
          <div class="col">
            <label for="custNum">Edit customer: </label>
            <input type="text" class="form-control" name="custNum" id="custNum" value="<?php echo $_SESSION['custNum']; ?>" readonly> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="title">Title (Optional): </label>
            <input type="text" class="form-control" name="title" id="title" value="<?php echo $custDetails[0]->name->title; ?>"> <br>
          </div>

          <div class="col">
            <label for="fName">First Name: </label>
            <input type="text" class="form-control" name="fName" id="fName" value="<?php echo $custDetails[0]->name->firstName; ?>"> <br>
          </div>

          <div class="col">
            <label for="mName">Middle Name (Optional): </label>
            <input type="text" class="form-control" name="mName" id="mName" value="<?php echo $custDetails[0]->name->middleName; ?>"> <br>
          </div>

          <div class="col">
            <label for="lName">Last Name: </label>
            <input type="text" class="form-control" name="sName" id="sName" value="<?php echo $custDetails[0]->name->lastName; ?>"> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="num1">Phone Number 1: </label>
            <input type="number" class="form-control" name="num1" id="num1" value="<?php echo $custDetails[0]->phoneNumbers->number[0]; ?>"> <br>
          </div>

          <div class="col">
            <label for="num2">Phone Number 2 (Optional): </label>
            <input type="number" class="form-control" name="num2" id="num2" value="<?php echo $custDetails[0]->phoneNumbers->number[1]; ?>"> <br>
          </div>

          <div class="col">
            <label for="num3">Phone Number 3 (Optional): </label>
            <input type="number" class="form-control" name="num3" id="num3" value="<?php echo $custDetails[0]->phoneNumbers->number[2]; ?>"> <br>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <label for="street">Street Address: </label>
            <input type="text" class="form-control" name="street" id="street" value="<?php echo $custDetails[0]->address->street; ?>"> <br>
          </div>

          <div class="col">
            <label for="suburb">Suburb: </label>
            <input type="text" class="form-control" name="suburb" id="suburb" value="<?php echo $custDetails[0]->address->suburb; ?>"> <br>
          </div>

          <?php
            $selectedState = (string)$custDetails[0]->address->state;
          ?>
          <div class="col">
            <label for="state">State: </label>
            <select class="form-control" name="state" id="state">
              <option value="NSW" <?= ($selectedState == 'NSW') ? "selected='selected'" : '' ;?>>NSW</option>
              <option value="QLD" <?= ($selectedState == 'QLD') ? "selected='selected'" : '' ;?>>QLD</option>
              <option value="TAS" <?= ($selectedState == 'TAS') ? "selected='selected'" : '' ;?>>TAS</option>
              <option value="VIC" <?= ($selectedState == 'VIC') ? "selected='selected'" : '' ;?>>VIC</option>
              <option value="ACT" <?= ($selectedState == 'ACT') ? "selected='selected'" : '' ;?>>ACT</option>
              <option value="NT" <?= ($selectedState == 'NT') ? "selected='selected'" : '' ;?>>NT</option>
              <option value="WA" <?= ($selectedState == 'WA') ? "selected='selected'" : '' ;?>>WA</option>
              <option value="SA" <?= ($selectedState == 'SA') ? "selected='selected'" : '' ;?>>SA</option>
            </select> <br>
          </div>

          <div class="col">
            <label for="pcode">Postcode: </label>
            <input type="number" class="form-control" name="pcode" id="pcode" value="<?php echo $custDetails[0]->address->postcode; ?>"> <br>
          </div>
        </div>

        <input type="submit" class="btn btn-primary" name="submit">
      </form>
    </div>
  </body>
</html>
