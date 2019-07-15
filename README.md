# Cell Phone Usage Report
This creates a cell phone usage for the current year from employee usage CSV data, and print the report to the local JVM default printer.

### Data Model
Reporting data is from comma separated files with the header in the first row. 

**CellPhone.csv**
*	employeeId
*	employeeName
*	purchaseDate
*	model

**CellPhoneUsageByMonth.csv** ( there may be multiple records for an employee on a single date )
*	employeeId
*	year
*	month
*	minutesUsed
*	dataUsed

### Report  

The usage report contains the following information

**Header Section**

*	Report Run Date
*	Number of Phones
*	Total Minutes
*	Total Data
*	Average Minutes
*	Average Data

**Details Section**

For each assigned cell phone, we show the following information
*	Employee Id
*	Employee Name
*	Model
*	Purchase Date
*	Minutes Usage
    *	one column for each month of the report year
*	Data Usage
    *	one column for each month of the report year


# Caveats
* This will be very simple reporting ( for now ), just ASCII text instead of PDF.
* Command line options will let the user specify the import phone and usage CSV file names/paths, and the year to report usages for.
* The default system print ( or maybe a command line value for printer name to match ) gets the print output of the report.

# Design
* Collection classes hold the known cell phones ( by employee id ) and the monthly usage rows.


