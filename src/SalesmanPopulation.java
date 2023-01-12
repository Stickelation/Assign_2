import BasicIO.ASCIIOutputFile;

import java.util.*;

public class SalesmanPopulation
{
    int seed1;
    int numCities;
    int genCounter=0;
    int eliteAmount; // Amount of elites we move to the next generation without mutating
    private int reproductionSize=100;
    double crossoverChance; // THIS IS CHANGED THROUGHOUT TESTING/DATA COLLECTION
    double mutationChance;
    double[][]originalData;
    List<SalesmanChromosome> chromList = new ArrayList<SalesmanChromosome>();
    int[] cityList;
    List<SalesmanChromosome> selected = new ArrayList<SalesmanChromosome>();
    List<SalesmanChromosome> children = new ArrayList<SalesmanChromosome>();
    List<SalesmanChromosome> duplicateList = new ArrayList<SalesmanChromosome>();
    double totalFitness;
    Random random;
    int counter=0;
    ASCIIOutputFile output = new ASCIIOutputFile();
    SalesmanPopulation(double[][] passedData, int maxCities,int passedSeed)
    {
        this.seed1 = passedSeed;
        originalData = passedData;
        numCities = maxCities;
        this.cityList= createCityList(maxCities);
        this.random = new Random(seed1);
    }
    private int[] createCityList(int maxCities) // Stores all cities in a 1d array (excluding the coordinates)
    {
        int[]cityList = new int[maxCities];
        for (int i = 0; i < numCities; i++)
        {
            cityList[i]= (int)originalData[i][0];
        }
        return cityList;
    }
    public void addRandomChromosome(int passedPop)
    {
        this.counter++;
        this.reproductionSize=passedPop;
        SalesmanChromosome newChrom= new SalesmanChromosome(numCities);
        newChrom.fillRandom(cityList,random, counter);
        this.chromList.add(newChrom); // Adds the newly filled chromosome to this population
    }
    public void displayPop() // USED FOR TESTING - outputs all chromosomes to the console (without costs)
    {
        int temp=0;
        output.newLine();
      /*  for (int i = 0; i <chromList.size(); i++)
        {
            temp = i+1;
            output.newLine();
            output.writeLine("Chromosome: " +temp);
            System.out.println("Chromosome:"+ temp); // To console
            chromList.get(i).output(); // To console
            chromList.get(i).outputFile(output);
            System.out.println(); // To console
        }
        */

        output.newLine();
        output.newLine();
        output.writeString("Best Solution Fitness: ");
        output.writeDouble(chromList.get(0).fitness);
        output.newLine();
        output.writeLine("Best Solution Chromosome: ");
        chromList.get(0).outputFile(output);
        output.newLine();
        output.writeString("Seed Number: ");
        output.writeInt(seed1);
        output.newLine();
        output.writeString("Parameters: ");
        output.newLine();
        output.writeLine("K for Tournament Selection: 3");
        output.writeLine("Crossover Rate = " +crossoverChance);
        output.writeLine("Mutation Rate = " +mutationChance);
        output.close();
    }
    public void evaluateAll()
    {
        genCounter++;
        double temp;
        double tempFirst=0;
        for (int i = 0; i < chromList.size(); i++)
        {
            temp = chromList.get(i).calcFitness(originalData);
            if (i==0)
            {
                tempFirst=temp; // Stores best fitness value for each generation
            }
            totalFitness+=temp;
            chromList.get(i).setFitness(temp);
       //    System.out.println(chromList.get(i).getFitness()); // USED FOR TESTING PURPOSES
        }

        this.totalFitness= totalFitness/reproductionSize;
        output.newLine();
        output.writeString("Generation "+genCounter);
        output.newLine();
        output.writeString("Average Population Fitness Value: ");
        output.writeDouble(this.totalFitness);
        output.newLine();
        output.writeString("Best Fitness Value: ");
        output.writeDouble(tempFirst);
        output.newLine();
    }
    // Selects K individuals from the parent Population, from those K individuals: chooses the one with the best eval score and repeats this process to find a second different parent. Parent population is reset, and this repeats until reproductionSize pairs are chosen
    public void selectNewPop() //Selects the new population via Tournament Selection | Parents are stored in the 'selected' list, with each set of parents being the next 2 elements in the list
    {
     //   System.out.println("Chromlist size: " +chromList.size());
        SalesmanChromosome chosenChrom; // First chosen
        SalesmanChromosome chosenChrom2; // Second chosen
        SalesmanChromosome chosenChrom3; // Third chosen
        SalesmanChromosome winnerChrom; // First parent
        SalesmanChromosome winnerChrom2; // Second parent
        int temp=0;
        int holder=0;
        int reproduceAmount = reproductionSize;
        reproduceAmount-=eliteAmount; // Reduces the reproduction size by the amount of elites added already

        for (int i = 0; i < reproduceAmount; i++) // Runs until reproductionSize is met
        {
            shuffleChroms();
            chosenChrom=chromList.get(0);
            chosenChrom2=chromList.get(1);
            chosenChrom3=chromList.get(2);
            if (chosenChrom.getFitness()<=chosenChrom2.getFitness()&&chosenChrom.getFitness()<=chosenChrom3.getFitness())
            {
                winnerChrom=chosenChrom;
                selected.add(winnerChrom); // Adds the winner of the tournament to the new list
            }
           else if (chosenChrom2.getFitness()<=chosenChrom.getFitness()&&chosenChrom2.getFitness()<=chosenChrom3.getFitness())
            {
                winnerChrom=chosenChrom2;
                selected.add(winnerChrom); // Adds the winner of the tournament to the new list
            }
           else
            {
                winnerChrom=chosenChrom3;
                selected.add(winnerChrom); // Adds the winner of the tournament to the new list
            }
           // Repeats for the second parent
            chosenChrom=chromList.get(3);
            chosenChrom2=chromList.get(4);
            chosenChrom3=chromList.get(5);
            if (chosenChrom.getFitness()<=chosenChrom2.getFitness()&&chosenChrom.getFitness()<=chosenChrom3.getFitness())
            {
                winnerChrom2=chosenChrom;
                selected.add(winnerChrom2); // Adds the winner of the tournament to the new list
            }
            else if (chosenChrom2.getFitness()<=chosenChrom.getFitness()&&chosenChrom2.getFitness()<=chosenChrom3.getFitness())
            {
                winnerChrom2=chosenChrom2;
                selected.add(winnerChrom2); // Adds the winner of the tournament to the new list
            }
            else
            {
                winnerChrom2=chosenChrom3;
                selected.add(winnerChrom2); // Adds the winner of the tournament to the new list
            }
        }
    }
    private void shuffleChroms() // Shuffles the stored chromosomes so that they can be selected randomly without duplication
    {
        Collections.shuffle(chromList); // TODO: Put a seed after chromlist, ex: (chromList, new Random(*seed*));
    }
    public void crossOver(double passedChance) // Applies crossover on the parents in 'selected', and stores them in the 'children' list | Creates 2 children per parent set
    {
        this.crossoverChance=passedChance;
        int eliteNumber = this.eliteAmount;
        int[] mask = new int[numCities];
        boolean notIncluded = true;
        for (int n = 0; n < (reproductionSize-eliteNumber)/2; n++)
        {
            for (int i = 0; i < numCities; i++) // Randomly fills the mask with 0s and 1s | If the mask has a 1, C1 copies P1's allele and C2 copies P2's allele (Vice-Versa for a 0)
            {
                Random random = new Random();
                if (random.nextInt()%2==0) //50% chance to add a 0 or a 1
                {
                    mask[i]=0;
                }
                else
                    mask[i]=1;
            }
            // Does UoX as normal, unless a duplicate is about to be inserted - if it does, then we choose the next element in the parent we are choosing from that isn't a duplicate
            SalesmanChromosome child1 = new SalesmanChromosome(numCities);
            SalesmanChromosome child2 = new SalesmanChromosome(numCities);
            SalesmanChromosome parent1 = selected.get(0);
            selected.remove(0);
            SalesmanChromosome parent2 = selected.get(0);
            selected.remove(0);
            Random rand = new Random();
            double randInt = rand.nextInt(101);
            randInt+=1;
            double randChance = randInt/100;
            if (randChance>crossoverChance) // Checks if crossover failed
            {
                children.add(parent1);
                children.add(parent2);
            }
            else
            {
                for (int i = 0; i < mask.length; i++)
                {
                    int counter = i;
                    if (mask[i] == 1)
                    {
                        while (true) // Loops until a non-included city is found in the parent
                        {
                            int tempParentVal1 = parent1.chromosome[counter];
                            for (int j = 0; j < child1.chromosome.length; j++)
                            {
                                if (child1.chromosome[j] == tempParentVal1) // Check if the child already contains the city SOMEWHERE in its chromosome
                                {
                                    notIncluded = false;
                                    break;
                                }
                            }
                            if (notIncluded) // If the city from the parent is NOT already included in the child, it is added
                            {
                                child1.chromosome[i] = tempParentVal1;
                                break;
                            }
                            notIncluded = true;
                            counter++;
                            if (counter >= mask.length)
                                counter = 0;
                        }
                        counter = i;
                        while (true) // Loops until a non-included city is found in the parent
                        {
                            int tempParentVal2 = parent2.chromosome[counter];
                            for (int j = 0; j < child1.chromosome.length; j++)
                            {
                                if (child2.chromosome[j] == tempParentVal2)  // Check if the child already contains the city SOMEWHERE in its chromosome
                                {
                                    notIncluded = false;
                                    break;
                                }
                            }
                            if (notIncluded) // If the city from the parent is NOT already included in the child, it is added
                            {
                                child2.chromosome[i] = tempParentVal2;
                                break;
                            }
                            notIncluded = true;
                            counter++;
                            if (counter >= mask.length)
                                counter = 0;
                        }
                    } else {
                        while (true) // Loops until a non-included city is found in the parent
                        {
                            int tempParentVal2 = parent2.chromosome[counter];
                            for (int j = 0; j < child1.chromosome.length; j++)
                            {
                                if (child1.chromosome[j] == tempParentVal2)  // Check if the child already contains the city SOMEWHERE in its chromosome
                                {
                                    notIncluded = false;
                                    break;
                                }
                            }
                            if (notIncluded) // If the city from the parent is NOT already included in the child, it is added
                            {
                                child1.chromosome[i] = tempParentVal2;
                                break;
                            }
                            notIncluded = true;
                            counter++;
                            if (counter >= mask.length)
                                counter = 0;
                        }
                        counter = i;
                        while (true) // Loops until a non-included city is found in the parent
                        {
                            int tempParentVal1 = parent1.chromosome[counter];
                            for (int j = 0; j < child2.chromosome.length; j++) {
                                if (child2.chromosome[j] == tempParentVal1) // Check if the child already contains the city SOMEWHERE in its chromosome
                                {
                                    notIncluded = false;
                                    break;
                                }
                            }
                            if (notIncluded) // If the city from the parent is NOT already included in the child, it is added
                            {
                                child2.chromosome[i] = tempParentVal1;
                                break;
                            }
                            notIncluded = true;
                            counter++;
                            if (counter >= mask.length)
                                counter = 0;
                        }
                    }
                }
                children.add(child1);
                children.add(child2);
            }
        }
    }
    public void crossOver2(double passedChance) // Single-point Crossover
    {
        this.crossoverChance=passedChance;
        int eliteNumber = this.eliteAmount;
        int[] mask = new int[numCities];
        boolean notIncluded = true;
        for (int n = 0; n < (reproductionSize-eliteNumber)/2; n++) // Runs for each set of parents
        {
            Random random = new Random();
            int counter = random.nextInt(numCities-4);
            counter+=4;
            int[] par1 = selected.get(0).chromosome;
            SalesmanChromosome parent1 = new SalesmanChromosome(numCities);
            parent1.chromosome=par1;
            selected.remove(0);
            int[] par2 = selected.get(0).chromosome;
            SalesmanChromosome parent2 = new SalesmanChromosome(numCities);
            parent2.chromosome=par2;
            selected.remove(0);
            Random rand = new Random();
            double randInt = rand.nextInt(101);
            randInt+=1;
            double randChance = randInt/100;
            if (randChance>crossoverChance) // Checks if crossover failed
            {
                children.add(parent1);
                children.add(parent2);
            }
            else
                {
                int temp = 0;
                SalesmanChromosome child1 = new SalesmanChromosome(numCities);
                SalesmanChromosome child2 = new SalesmanChromosome(numCities);
                for (int i = 0; i < numCities; i++) {
                    if (i < counter) // Mask would = 1
                    {
                        child1.chromosome[i] = par1[i];
                        child2.chromosome[i] = par2[i];
                    } else {
                        child1.chromosome[i] = par2[i];
                        child2.chromosome[i] = par1[i];
                    }
                }
                fixChrom(child1);
                fixChrom(child2);
                children.add(child1);
                children.add(child2);
            }
        }
    }
    public void eliteAdd(double eliteRate) // Adds the best x chromosomes directly to the new population
    {
        this.eliteAmount = (int)(eliteRate*chromList.size());
        if (eliteAmount<2)
            this.eliteAmount=2;
        else if (eliteAmount%2!=0) // ensures there will be an even number of elites
            this.eliteAmount++;

        duplicateList(); // Duplicates the parent list so that elite chromosomes can still participate in creating children
        double holder = 10000000;
        int holder2 =0;
        for (int i = 0; i < eliteAmount; i++)
        {
            for (int j = 0; j < duplicateList.size(); j++)
            {
                if (duplicateList.get(j).getFitness()<holder) //finds fittest chromosome in the duplicate list
                {
                    holder = duplicateList.get(j).getFitness();
                    holder2=j;
                }
            }
            children.add(duplicateList.get(holder2));
            duplicateList.remove(holder2);
            holder2=0;
            holder=100000;
        }
    }
    public void mutate(double chance) // Due to elitism, we do NOT mutate the best x chromosomes (Which are added in the eliteAdd method)
    {
        this.mutationChance=chance;
        // skips over elites - which are guaranteed to be in the first x positions of the list
        for (int i = eliteAmount; i < children.size(); i++)
        {
            Random random = new Random();
            double randInt = random.nextInt(101);
            randInt+=1;
            double randChance = randInt/100;
            if (randChance <= chance) // does mutation by swapping two random positions in the chromosome
            {
                int temp = (int) (Math.random() * ((numCities - 1) + 1)); // Selects a random number within the appropriate range
                int temp2 = (int) (Math.random() * ((numCities - 1) + 1)); // Selects a random number within the appropriate range
                int holder = children.get(i).chromosome[temp];
                children.get(i).chromosome[temp]=children.get(i).chromosome[temp2];
                children.get(i).chromosome[temp2]=holder;
            }
        }

        // Since Mutation is the last step, the new generation is complete and the old generation is replaced
        chromList.clear();
        chromList.addAll(children); // Assigns the children as the new population (Once all children are created)
        children.clear();

    }
    private void duplicateList() // Duplicates chromList
    {
        for (int i = 0; i < chromList.size(); i++)
        {
            duplicateList.add(chromList.get(i));
        }
    }
    private void fixChrom(SalesmanChromosome passedChrom) // Makes illegal chromosomes legal by replacing duplicate cities
    {
        List <Integer> tempArray = new ArrayList();
        List <Integer>missingNums = new ArrayList();
        for (int i = 0; i <numCities ; i++)
        {
            tempArray.add(passedChrom.chromosome[i]);
        }
        for (int i = 1; i <=numCities ; i++) // Checks what numbers the chromosome is missing and adds them to a list
        {
            if (!tempArray.contains(i))
            {
                missingNums.add(i);
            }
        }
        for (int i = 0; i < numCities; i++)
        {
            for (int j = 0; j < numCities; j++)
            {
                if (j!=i)
                {
                    if (tempArray.get(i) == tempArray.get(j))
                    {
                        tempArray.remove(j);
                        tempArray.add(missingNums.get(0));
                        missingNums.remove(0);
                    }
                }
            }
        }
        for (int i = 0; i < numCities; i++) // Updates chromosome
        {
            passedChrom.chromosome[i]=tempArray.get(i);
           // System.out.print(tempArray.get(i) +", ");
        }
      //  System.out.println();
    }


}