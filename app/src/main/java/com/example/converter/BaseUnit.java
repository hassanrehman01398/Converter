package com.example.converter;

import android.util.Log;

import java.util.ArrayList;

/**
 Created by Roger Carvalho for RDC Media Ltd. on 13/07/15. This code can freely be used, amended,
 distributed and sold for any purpose desired, but should credit RDC Media Ltd. within the notes.
 */
public class BaseUnit {

    private double value;
    private double baseNumber;
    private final ArrayList<String> unitNames = new ArrayList<>();
    private final ArrayList<Double> unitValues = new ArrayList<>();

    public BaseUnit(String[] unitNames, double[] unitValues, double value, double baseNumber)
    /*
    Constructor: to instantiate a base unit, you need to provide 2 arrays, that contain all the
    potential measurements units that the base unit can be converted to. unitNames contains the
    names of al the units, unitValues contain the multipliers, there should always be a unit
    provided with a unitValue (multiplier) of 1 within the arrays to ensure proper calculation. You
    should also provide a base number (which will be added before and subtracted after any
    multiplier calculation (to support i.e. Celcius to Fahrenheit conversion). If there is no base
    number, simply provide 0.
    */
    {
        /*
        Set the value contained in this object, that is the value which belongs to the unit
        of measurement with a multiplier of 1.
        */
        this.value = value;
        this.baseNumber = baseNumber;


        for (String unitName : unitNames)
        {
            //load up the object unit names reference array
            this.unitNames.add(unitName);

        }

        for (double unitValue : unitValues)
        {
            //load up the object unit multiplier reference array
            this.unitValues.add(unitValue);
        }

    }

    public double convert(int unitIndex)
    //This method returns the value of this object's amount in a given measurement unit
    {
        /*
        Check if the provided unitIndex is supported, i.e. if a reference to a unit exists with
        that index
        */

        if (this.unitValues.size() < unitIndex || unitIndex < 0)
        {
            //the unitIndex is out of bounds for the available units. Return 0
            return 0;
        }
        else
        {
            /*
            Return the required conversion value. First add the base number to the value,
            then multiply that with the multiplier requested. Lastly, subtract the base number
            from the result.
             */
            return ((value + baseNumber) * unitValues.get(unitIndex)) - baseNumber;
        }
    }

    public int getIndex(String requestedUnit)
    /*
    This method returns the index of a given unit for conversion. This method can be used to
    search what int to provide to convert to a desired unit
    */
    {
        for (int i = 0; i < this.unitNames.size(); i++)
        {
            if (unitNames.get(i).equals(requestedUnit))
            {
                return i;
            }
        }

        //If the requestedUnit was not found, return a negative value
        return -1;

    }

    public String[] getUnitNames()
    //This method returns a string array containing all unit names supported in this instance
    {
        return unitNames.toArray(new String[unitNames.size()]);
    }

    public void setValue(double value, int unitIndex)
    //This method sets the value of this object based on a given unit
    {
        this.value = ((value + baseNumber) * 1/this.unitValues.get(unitIndex)) - baseNumber;
    }

}