<h1 align="center">
  <p align="center">
    <img src="https://github.com/user-attachments/assets/89c0e841-a321-43b4-b8fa-cdd9cd2cd7c4" >
  </p>
  <p align="center"> PEOPLE 
  </p>
</h1>
<div>
  <p>
    This desktop application is an academic example of the use of the Model-View-Controller (MVC) code organization system and uses Java as an object-oriented programming language. The application allows the user to do CRUD operations of the defined entity (Person :raising_hand:) and works with different storage systems, both temporary and persistent.
  </p>
</div>
<div>
  <p align="center">
    <img src="https://img.shields.io/badge/License-GPL v3.0-blue">
    <img src="https://img.shields.io/badge/JDK->=17-red">
    <img src="https://img.shields.io/badge/Maven-4.0.0-green">
    <img src="https://img.shields.io/badge/OS-Windows, Linux-yellow">
    <img src="https://img.shields.io/badge/ObjectDB-2.9.0-orange">
    <img src="https://img.shields.io/badge/MySQL_Connector-8.0.25-purple">
    <img src="https://img.shields.io/badge/JDatePicker-2.0.3-white">
    <img src="https://img.shields.io/badge/Apache Commons_IO-2.5-brown">
    <img src="https://img.shields.io/badge/release-Latest version-black">
  </p>
</div>
<h2>🛠️How it works</h2>
<div>
  <p>The application code is distributed in the following packages: </p>
    <p><b>📦 Model.Class</b></p>
      <p><b>🗒️ Person.java</b></p> 
        <p><span>Class that defines the type of object with which the application works. In our case it structures the person :raising_hand: entity. The key attribute that   
                  distinguishes one person from another is their NIF. People with the same NIF cannot exist in the applicationThe three main MVC packages do not exchange     
                  basic data but rather exchange objects of this class.</span></p>
      <p><b>🗒️ PersonException.java</b></p>
        <p><span>Own exception thrown from the Controller package when an attempt is made to insert or delete a person who has not been previously registered. The management                   of the exception is also carried out within the Controller package by displaying an alert message to the user. It is not necessary to include it for the   
                  code to function, but since it is an academic activity it has been deemed appropriate to include it.</span></p>
    <p><b>📦 Controller</b></p>
      <p><b>🗒️ IController.java</b></p> 
        <p><span>Interface that defines the operations that the controller, and therefore the application, can perform regardless of the type of storage selected. These   
                  operations are: read one or all people, register a person, update a person, and delete one or all people. The "start()" method allows you to start the 
                  application. The use of an interface isolates the Controller package code from the rest of the application packages, minimizing the impact on future     
                  software updates.</span></p>
      <p><b>🗒️ ControllerImplementation.java</b></p> 
        <p><span>Class that implements the IController.java interface. It is responsible for scheduling and managing the events of the visual part. Thanks to these events, 
                  it is able to prepare the necessary structure for data storage, operate with these structures and show the information to the user if necessary. This block                   also manages exceptions that are mainly related to information access problems.</span></p>
</div>
<h2>🚀Getting started</h2>


