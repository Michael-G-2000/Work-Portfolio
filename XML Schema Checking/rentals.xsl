<?xml version="1.0" encoding="UTF-8"?>
<!--
 This document displays information from the following files:
  - customers.xml
  - vehicles.xml
  - rentalRecords.xml

 Filename: rentals.xsl
 Author: Michael Giglio
 Student ID: 19399805
 Date: 17/09/2020
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <head>
        <meta charset="utf-8"/>
        <title>Current Rental Records</title>
        <link rel="stylesheet" href="bootstrap.css"/>
      </head>
      <body class="bg-light">
        <nav class="navbar navbar-expand-lg text-light bg-dark shadow-sm border-bottom">
          <div class="container">
            <a class="navbar-brand pl-3 pr-3 border-right text-light" href="default.html">
              <img src="./images/car.jpg" width="30" height="30" class="d-inline-block align-top" alt=""/>
                Rent-a-Car
            </a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav mr-auto">
                <li>
                  <a class="nav-link" href="rentalRecords.xml">View Records</a>
                </li>
                <li>
                  <a class="nav-link disabled" href="">Manage Customers</a>
                </li>
                <li>
                  <a class="nav-link disabled" href="">Manage Rentals</a>
                </li>
                <li>
                  <a class="nav-link disabled" href="">New Rental</a>
                </li>
              </ul>
            </div>
          </div>
        </nav>
        <h1 class="text-center bg-dark text-light">Current Rentals</h1>
        <hr/>
        <xsl:apply-templates select="rentals/booking[not(boolean(./fuel))]">
          <xsl:sort select="./date/pickUp"/>
        </xsl:apply-templates>
        <h1 class="text-center bg-dark text-light">Past Rentals</h1>
        <hr/>
        <xsl:apply-templates select="rentals/booking[boolean(./fuel)]">
          <xsl:sort select="./date/pickUp"/>
        </xsl:apply-templates>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="booking">
    <xsl:variable name="regoNumVar" select="./regoNum"/>
    <xsl:variable name="custNumVar" select="./customerNum"/>

    <div class="container">
      <div class="row pl-5">
        <div class="col-4">
          <h2>Rental details</h2>
          <p><strong>Rental ID: </strong><xsl:value-of select="./@recordNum"/></p>
          <p><strong>Date Booked: </strong><xsl:value-of select="./date/booked"/></p>
          <p><strong>Pick Up Date: </strong><xsl:value-of select="./date/pickUp"/></p>
          <p><strong>Number of Days Rented: </strong><xsl:value-of select="./daysRented"/></p>
          <p><strong>Daily Cost: $</strong><xsl:value-of select="./dailyCost"/></p>
          <p><strong>Total Cost: $</strong><xsl:value-of select="format-number((daysRented*dailyCost),'0.00')"/></p>
          <xsl:if test="./fuel">
            <xsl:if test="./fuel/@full">
        	     <xsl:if test="./fuel/@full='false'">
                 <p><strong>Fuel Charge: $</strong><xsl:value-of select="./fuel/charge"/></p>
               </xsl:if>
             </xsl:if>
           </xsl:if>
         </div>
         <div class="col-4">
           <h2>Vehicle Details</h2>
           <xsl:apply-templates select="document('vehicles.xml')/vehicles/vehicle[@rego=$regoNumVar]"/>
         </div>
         <div class="col-4">
           <h2>Customer Details</h2>
           <xsl:apply-templates select="document('customers.xml')/customers/customer[@custNum=$custNumVar]"/>
         </div>
       </div>
    </div>
    <hr/>
  </xsl:template>

  <xsl:template match="vehicle">
    <p><strong>Registration Number: </strong><xsl:value-of select="./@rego"/></p>
    <p><strong>Make: </strong><xsl:value-of select="./make"/></p>
    <p><strong>Model: </strong><xsl:value-of select="./model"/></p>
    <p><strong>Year: </strong><xsl:value-of select="./year"/></p>
  </xsl:template>

  <xsl:template match="customer">
    <p><xsl:apply-templates select="./name"/></p>
  <p><xsl:apply-templates select="./address"/></p>
    <p><strong>Email: </strong><xsl:value-of select="./email"/></p>
    <p>
      <strong>Numbers: </strong> <br/>
      <xsl:for-each select="./phoneNumbers/number">
        <xsl:value-of select="."/> <br/>
      </xsl:for-each>
    </p>
  </xsl:template>

  <xsl:template match="name">
    <p>
      <strong>Name: </strong>
      <xsl:if test="./title">
        <xsl:value-of select="concat(./title, ' ')"/>
      </xsl:if>
      <xsl:value-of select="concat(./firstName, ' ')"/>
      <xsl:if test="./middleName">
        <xsl:value-of select="concat(./middleName, ' ')"/>
      </xsl:if>
      <xsl:value-of select="./lastName"/>
    </p>
  </xsl:template>

  <xsl:template match="address">
    <p>
      <strong>Address: </strong>
      <xsl:value-of select="concat(./street, ' ')"/>
      <xsl:value-of select="concat(./subrub, ' ')"/>
      <xsl:value-of select="concat(./state, ' ')"/>
      <xsl:value-of select="./postcode"/>
    </p>
  </xsl:template>

</xsl:stylesheet>
