package com.ex.calculate;

import java.io.Serializable;

public class ExpensesCompute implements Serializable{
    //Serializable = can be transferred as a file
    private static final long serialVersionUID=1L;
    
    //noArg constructor
    public ExpensesCompute(){}
    
    //Calculate yearly total expenses
    public int yearlyTotalExpenses(int[] amounts){
        int total=0;
        for(int amount:amounts){
            total+=amount;
        }
        return total;
    }
    
    //Calculate monthly total expenses
    public int monthlyTotalExpenses(int[] amounts){
        int total=0;
        for(int amount:amounts){
            total+=amount;
        }
        return total;
    }
    
    //Calculate yearly total expenses according to category
    public int yearlyTotalByCategory(int[] amounts){
        int total=0;
        for(int amount:amounts){
            total+=amount;
        }
        return total;
    }
    
    //Calculate average monthly expenses
    public double avgMonthlyExpenses(int yearlyTotal, int numberOfMonths){
        if (numberOfMonths==0) return 0;
        return (double) yearlyTotal / numberOfMonths;
    }
    
    //Calculate month-month comparison
    public int monthToMonthComparison(int currentMonth, int previousMonth){
        return currentMonth-previousMonth;
    }
    
    //Calculate percentage change between months
    public double percentageChangeBetweenMonths(int currentMonth, int previousMonth){
        if (previousMonth==0) return 0;
        return((double)(currentMonth-previousMonth)/previousMonth)*100;
    }
}