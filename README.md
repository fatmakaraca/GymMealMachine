# GymMealMachine

**Project Description**

This project is developed for the software of the Gym Meal Machine (GMM), a vending machine that allows gym members to select meals based on their nutritional values. The software is implemented using Java 8, following Object-Oriented Programming (OOP) principles.

**Programming Language**

Java 8

**Input and Output Files**

1. Product.txt
2. 
This file contains information about the products loaded into the machine.

Format:
ProductName[tab]Price[tab]Protein[g] Carbohydrate[g] Fat[g]

Example:

ChickenBreast    50  30 0 2  
Oatmeal         20  5 27 3  

2. Purchase.txt

This file records users' purchase transactions from the machine.

Format:
PaymentType[tab]Amount1 Amount2 ... AmountN[tab]SelectionCriteria[tab]Value

Example:

CASH    50 20    PROTEIN    30  

CARD    100       NUMBER     2  

3. GMMOutput.txt

This file includes:

The machine’s status after loading,

Purchase transactions and remaining balance,

Error messages related to invalid operations.
