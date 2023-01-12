// THIS PROGRAM WAS MADE IN JAVA USING INTELLIJ
// THIS PROGRAM UTILISES THE BASICIO LIBRARY PROVIDED BY BROCK
import BasicIO.*;

import java.util.Scanner;
// Date: 2021/11/10

/*
This program should:
1. Create an initial population of random individuals (Solutions/Chromosomes), each which have genes of length x (# cities)
2. Assigns a fitness score to the initial population using a fitness function (Will be the path cost)
3. Selects the two individuals (parents) based on their fitness scores using a Tournament Selection (Select K individuals from the total population - then choose the one with the best score)
4. Performs crossover on the parents to create children - This also must ensure that no duplicates are found in the chromosome of the child
5. Mutate the new offspring by swapping two cities in the chromosome based off of a % chance
6. Repeat x until the desired amount of children/generations are made
7. From the final generation, select the chromosome with the best fitness score and output the total chromosome + the fitness score (path cost)
 */
public class GeneticTSPMain
{
    ASCIIDataFile file;
    double[][]storedData;
    int numCities;
    int startingPopSize;
    static int MAXGEN; // Max generation span
    double crossoverChance; // THIS IS CHANGED THROUGHOUT TESTING/DATA COLLECTION
    double elitePercent;
    double mutationChance; // THIS IS CHANGED THROUGHOUT TESTING/DATA COLLECTION
    int seed;

    // NOTE TO TA: INVALID INPUTS WILL BREAK CODE HOWEVER THERE IS NOTHING IN THE ASSIGNMENT PDF THAT STATES THIS IS A REQUIREMENT!
    GeneticTSPMain()
    {
        System.out.println("Please Enter the Maximum Generations (Ex. 50)");
        Scanner temp = new Scanner(System.in);
        MAXGEN = temp.nextInt();
        System.out.println("Please Enter the Population Size (Ex. 100)");
        Scanner temp1 = new Scanner(System.in);
        startingPopSize=temp1.nextInt();
        System.out.println("Please Enter the Crossover Chance (Ex. 0.9)");
        Scanner temp2 = new Scanner(System.in);
        crossoverChance = temp2.nextDouble();
        System.out.println("Please Enter the Mutation Chance (Ex. 0.1)");
        Scanner temp3 = new Scanner(System.in);
        mutationChance=temp3.nextDouble();
        System.out.println("Please Enter the Seed Number");
        Scanner temp4 = new Scanner(System.in);
        seed = temp4.nextInt();
        System.out.println("Please Enter which Crossover Method you want to use ('1' for UOX, '2' for Single-point)");
        Scanner temp5 = new Scanner(System.in);
        int crossoverNum = temp5.nextInt();
        System.out.println("Please Enter the Percentage of Elites (Ex. 0.1)");
        Scanner temp6 = new Scanner(System.in);
        elitePercent = temp6.nextDouble();

        file = new ASCIIDataFile();
        numCities=file.readInt();
        storedData = new double[numCities][3]; // Stores all original data in a 2-d array [x][0] stores the city # [x][1-2] stores co-ords
        storeData();
        SalesmanPopulation startPop = new SalesmanPopulation(storedData,numCities,seed);
        // displayData(); // For testing purposes
        for (int i = 0; i < startingPopSize; i++) // creates x chromosomes for the starting population
        {
            createChromosome(startPop);
        }
        SalesmanPopulation tempPop = startPop;
        for (int i = 1; i <= MAXGEN; i++)
        {
          //  System.out.println("For Generation " +i);
            evaluatePop(tempPop); // Evaluates the population and assigns the scores internally
            tempPop = selectPop(tempPop); // tempPop is now replaced
            if (crossoverNum==1)
            crossover(tempPop); // UOX Crossover is performed on the new population
            else
            crossover2(tempPop); // Alternate crossover method
            mutation(tempPop); // mutation is performed on the new population
        }
         tempPop.displayPop(); // FOR TESTING PURPOSES
        //evaluatePop(tempPop);
        System.out.println("DONE");
        file.close();

    }


    void storeData()
    {
        for (int i = 0; i < numCities; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (j==0)
                {
                    int temp = file.readInt();
                    storedData[i][j]=temp;
                }
                else
                storedData[i][j]=file.readDouble();
            }
        }
    }

    void displayData()// Used for testing only
    {
        for (int i = 0; i < numCities; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (j==0)
                    System.out.print((int)storedData[i][j]);
                else
                System.out.print(storedData[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    void createChromosome(SalesmanPopulation passedPop)
    {
        passedPop.addRandomChromosome(startingPopSize);
    }
    void evaluatePop(SalesmanPopulation tempPopulation)
    {
        tempPopulation.evaluateAll();
    }
    SalesmanPopulation selectPop (SalesmanPopulation tempPopulation) // (Reproduction) Utilising Tournament Selection
    {
        tempPopulation.eliteAdd(elitePercent);
        tempPopulation.selectNewPop();
        return tempPopulation;
    }

    void crossover(SalesmanPopulation tempPopulation) // Performs a UOX crossover on the chromosome, Will also ensure that there are no duplications
    {
        tempPopulation.crossOver(crossoverChance);
    }
    void crossover2(SalesmanPopulation tempPopulation)
    {
        tempPopulation.crossOver2(crossoverChance);
    }

    void mutation(SalesmanPopulation tempPopulation) // Swaps two cities within the chromosome a variable number of times
    {
        tempPopulation.mutate(mutationChance);
    }

    public static void main(String[] args){ new GeneticTSPMain(); }
}
