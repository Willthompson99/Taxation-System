/**********************************************************************************************************************************************
* This program is a tax calculation system designed to handle different tax rates for different states
* and calculate various tax-related figures such as income tax, property tax, total tax, tax return, 
* and tax withholding. It reads data from an input CSV file, processes it, and writes the results 
* to an output CSV file.
*
* Classes:
* 1. TaxCalculator:
*    - Represents the base class for calculating various types of taxes.
*    - Attributes:
*      - stateName: Name of the state.
*      - incomeTaxRate: Rate of income tax.
*      - propertyTaxRate: Rate of property tax.
*      - deductionRate: Rate of deductions.
*    - Methods:
*      - getStateName(): Returns the state name.
*      - getIncomeTaxRate(): Returns the income tax rate.
*      - getPropertyTaxRate(): Returns the property tax rate.
*      - getDeductionRate(): Returns the deduction rate.
*      - calculateIncomeTax(double income): Calculates the income tax based on the income.
*      - calculatePropertyTax(double propertyValue): Calculates the property tax based on the property value.
*      - calculateTotalTax(double income, double propertyValue): Calculates the total tax based on the income and property values.
*      - calculateTaxReturn(double totalTaxPaid, double deductions): Calculates the tax return based on the total tax paid and deductions.
*
* 2. StateTax:
*    - Inherits from TaxCalculator.
*    - Used to handle state-specific tax calculations.
*    - Methods:
*      - calculateTaxWithholding(double grossIncome): Calculates the tax withholding based on the gross income and specific state rules.
*
* Main Class (TaxNavigator):
*    - The main driver class to run the tax calculation process.
*    - Reads input data from "TaxData.csv".
*    - Processes the data by calculating various tax-related figures.
*    - Writes the output data to "OutputTaxData.csv".
*
* Example Usage:
*    - Ensure "TaxData.csv" is present in the program's directory.
*    - Run the program to generate "OutputTaxData.csv" with the calculated tax information.
*
* Input File (TaxData.csv) Format:
*    - Header: StateOption, Income, PropertyValue, Expenses
*
* Output File (OutputTaxData.csv) Format:
*    - Header: StateOption, Income, PropertyValue, Expenses, IncomeTax, PropertyTax, TotalTax, Deductions, TaxReturn, TaxWithholding
*
* Notes:
*    - Ensure appropriate tax rates and deduction rates are defined in the StateTax constructor based on the state.
*    - This implementation is a simplified version and may need enhancements for handling real-world tax scenarios.
*
* Exceptions:
*    - The program handles IOExceptions related to file operations.
*
*@auther Will Thompson
**********************************************************************************************************************************************/

import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner; 
import java.nio.file.Files; 
import java.nio.file.Path; 
import java.io.IOException; 

// TaxCalculator class to calculate various taxes and returns
class TaxCalculator 
{
    // Declare Instance Fields
    private String stateName;
    private double incomeTaxRate;
    private double propertyTaxRate;
    private double deductionRate;

    // A Constructor to initialize TaxCalculator object with state name and tax rates
    public TaxCalculator(String stateName, double incomeTaxRate, double propertyTaxRate, double deductionRate) {
        this.stateName = stateName;
        this.incomeTaxRate = incomeTaxRate;
        this.propertyTaxRate = propertyTaxRate;
        this.deductionRate = deductionRate;
    }

    // Getter for state name
    public String getStateName() {
        return stateName;
    }

    // Getter for income tax rate
    public double getIncomeTaxRate() {
        return incomeTaxRate;
    }

    // Getter for property tax rate
    public double getPropertyTaxRate() {
        return propertyTaxRate;
    }

    // Getter for deduction rate
    public double getDeductionRate() {
        return deductionRate;
    }

    // Method to calculate income tax based on income
    public double calculateIncomeTax(double income) {
        return income*incomeTaxRate;
    }

    // Method to calculate property tax based on property value
    public double calculatePropertyTax(double propertyValue) {
        return propertyValue * propertyTaxRate;
    }

    // Method to calculate total tax based on income and property values
    public double calculateTotalTax(double income, double propertyValue) {
        return calculateIncomeTax(income) * calculatePropertyTax(propertyValue);
    }

    // Method to calculate tax return based on total tax paid and deductions
    public double calculateTaxReturn(double totalTaxPaid, double deductions) {
        return totalTaxPaid - deductions;
    }
}

// StateTax class extending TaxCalculator to handle state-specific tax calculations
class StateTax extends TaxCalculator
{
    // A Constructor initializing StateTax objects with predefined tax rates
    public StateTax(String stateName, double incomeTaxRate, double propertyTaxRate, double deductionRate) {
        super(stateName, incomeTaxRate, propertyTaxRate, deductionRate);
    }

    // Method to calculate tax withholding based on gross income
    public double calculateTaxWithholding(double grossIncome) {
        if (getStateName().equals("California")) {
            return (getIncomeTaxRate() * grossIncome) - (2 * 100);
        } else if (getStateName().equals("New York")) {
            return (getIncomeTaxRate() * grossIncome) - 500;
        }
        return 0;
    }
}

// TaxNavigator class to run the program
public class TaxNavigator
{
    public static void main(String[] args) throws IOException
    {
        // Define file paths for input and output
        Path inputPath = Path.of("lib\\TaxData.csv");
        Path outputPath = Path.of("lib\\OutputTaxData.csv");

        // Create output file if it doesn't exist and write the header line
        if (!Files.exists(outputPath)) {
            Files.createFile(outputPath);
            Files.writeString(outputPath, "StateOption,Income,PropertyValue,Expenses,IncomeTax,PropertyTax,TotalTax,Deductions,TaxReturn,TaxWithholding\n", StandardOpenOption.APPEND);
        } else {
            Files.delete(outputPath);
            Files.createFile(outputPath);
            Files.writeString(outputPath, "StateOption,Income,PropertyValue,Expenses,IncomeTax,PropertyTax,TotalTax,Deductions,TaxReturn,TaxWithholding\n", StandardOpenOption.APPEND);
        }
        
        // Scanner to read input file
        Scanner scanner = new Scanner(inputPath);

        // Skip header line
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        // A loop to read the input CSV data, process it, and write results to the output CSV file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] data = line.split(",");

            String state = data[0];
            double income = Double.parseDouble(data[1]);
            double propertyValue = Double.parseDouble(data[2]);
            double expenses = Double.parseDouble(data[3]);

            StateTax stateTax;

            // Create StateTax object based on state and predefined tax rates
            if (state.equals("California")) {
                stateTax = new StateTax("California", 0.093, 0.007, 0.175);
            } else if (state.equals("New York")) {
                stateTax = new StateTax("New York", 0.0685, 0.04, 0.2);
            } else {
                continue;  // Skip unknown states
            }

            // Calculate taxes and deductions
            double incomeTax = stateTax.calculateIncomeTax(income);
            double propertyTax = stateTax.calculatePropertyTax(propertyValue);
            double totalTax = incomeTax + propertyTax;
            double deductions = expenses * stateTax.getDeductionRate();
            double taxReturn = stateTax.calculateTaxReturn(totalTax, deductions);
            double taxWithholding = stateTax.calculateTaxWithholding(income);

            // Write the results to the output file
            String resultLine = String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                    state, income, propertyValue, expenses, incomeTax, propertyTax, totalTax, deductions, taxReturn, taxWithholding);
            Files.writeString(outputPath, resultLine, StandardOpenOption.APPEND);
        }

        // Close the file scanner
        scanner.close();
    }
}
