Introduction:
Code for a mock portfolio manager where:
- It loads and persists a book from CSV
- Mocks some price updates for the underyling
- Mocks publishing and recieving the updates, and calculates the updated prices for the book
- Publishes the updates and prints it out in console

Some assumptions and notes
- We are assuming that all the products in the CSV is correct and valid, the code does not validate if the instrument exists other than some basic syntax checks when loading
- Only supports european style for the vanilla calculations
- Market price updates are generated randomly between -5% to 5% from previous price
- Only publishes "AAPL" and "TELSA" market prices based on a static map in the mockPublisher, therefore only these two's derivatives and stock is usable
- While it stores in BigDecial this uses double when calculating, potential precision issues warning
- Cumulative probability is calculated with apache commons library

Usage
- All outputs are via consoles
- Running the jar/ project via IDE will automatically trigger a import attempt from the project's /resources/input.csv
- The file name can be changed via properties to load another file
- JMX operation available to reload CSV via CSVPositionImporter reloadCSV()
