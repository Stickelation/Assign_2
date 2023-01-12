import BasicIO.ASCIIOutputFile;

import java.util.List;
import java.util.Random;
import java.lang.Math;

public class SalesmanChromosome
{
    double fitness = 0.00;
    double cost; // Using Euclidean distance
    double xCord;
    double yCord;
    int startingCity;
    int numCities;
    int[] chromosome; // Stores route in a 1d Array


    public SalesmanChromosome(int numCities)
    {
        this.numCities=numCities;
        chromosome = new int[numCities];

    }
    public double calcFitness(double[][]dataSet) // Calculates the fitness of the chromosome by checking travelling costs
    {
        double tempFitness=0.00;
        double holder1;
        double holder2;
        double holder3;
        double holder4;
        double xdiff;
        double ydiff;
        double addDiffs;
        double distance;
        int k=0;
        for (int i = 0; i <numCities-1; i++)
        {
            while (true)
            {

                if (chromosome[i] == (int) dataSet[k][0])
                {
                    holder1=dataSet[k][1]; // Stores x cord for current city
                    holder2=dataSet[k][2]; // Stores y cord for current city
                    k=0;
                    break;
                }
                else
                    k++;
            }
            while (true)
            {
                if (chromosome[i+1] == (int) dataSet[k][0])
                {
                    holder3=dataSet[k][1]; // Stores x cord for next city
                    holder4=dataSet[k][2]; // Stores y cord for next city
                    k=0;
                    break;
                }
                else
                    k++;
            }
           xdiff = holder3-holder1;
           ydiff = holder4- holder2;
           xdiff = xdiff*xdiff;
           ydiff = ydiff*ydiff;
           addDiffs= xdiff+ydiff;
           if (addDiffs<0)
           {
               addDiffs=addDiffs*-1;
           }
           distance = Math.sqrt(addDiffs);
           tempFitness+=distance;
        }
        k=0;
        while (true) // The below loops are for the salesman's 'return home' from the last city to the first(starting) city
        {

            if (chromosome[numCities-1] == (int) dataSet[k][0]) // Stores cords for last city
            {
                holder1=dataSet[k][1];
                holder2=dataSet[k][2];
                k=0;
                break;
            }
            else
                k++;
        }
        while (true)
        {
            if (chromosome[0] == (int) dataSet[k][0]) // Stores cords for first city
            {
                holder3=dataSet[k][1];
                holder4=dataSet[k][2];
                k=0;
                break;
            }
            else
                k++;
        }
        xdiff = holder3-holder1;
        ydiff = holder4- holder2;
        xdiff = xdiff*xdiff;
        ydiff = ydiff*ydiff;
        addDiffs= xdiff+ydiff;
        if (addDiffs<0)
        {
            addDiffs=addDiffs*-1;
        }
        distance = Math.sqrt(addDiffs);
        tempFitness+=distance;

        return tempFitness;
    }
    public double getFitness() // Returns fitness of the current chromosome
    {
        return fitness;
    }
    public void setFitness(double newFit) // Sets passed fitness
    {
        this.fitness=newFit;
    }
    public int getStartingCity() //  Gets the starting city
    {
        return startingCity;
    }
    public void setStartingCity(int start) //  Sets the starting city
    {
        this.startingCity=start;
    }
    public void output()
    {
        System.out.print("This chromosome has the path: ");
        for (int i = 0; i < numCities; i++)
        {
            System.out.print(chromosome[i]);
            System.out.print(", ");
        }
    }
    public void outputFile(ASCIIOutputFile file)
    {
        file.writeString("This chromosome has the path: ");
        for (int i = 0; i < numCities; i++)
        {
            file.writeInt(chromosome[i]);
            file.writeString(", ");
        }
    }
    public void fillRandom(int[] cityList, Random random,int counter) // Randomly adds the cities from the cityList into its own Chromosome
    {
        int[]tempList = new int[numCities];
        for (int i = 0; i < numCities; i++)
        {
            tempList[i]=cityList[i];
        }
        shuffleArray(tempList,random,counter);
        for (int i = 0; i < numCities; i++)
        {
            chromosome[i]=tempList[i];
        }

    }
    private void shuffleArray(int[] array, Random rando, int counter)
    {
        int index, temp;
        //Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = rando.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

}
