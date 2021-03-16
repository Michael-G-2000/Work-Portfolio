<!--
 This document logs the user out of Rent-a-Car:

 Filename: logout.php
 Author: Michael Giglio
 Student ID: 19399805
 Date: 15/10/2020
-->
<?php
session_start();
session_unset();
session_destroy();
header("Location: default.php");
?>
