//-------------------------------------------------------------------------------------------------
// Factory.java
//
// Factory class that creates the bank and each bank customer
// Usage:  java Factory 10 5 7

import java.io.*;
import java.util.*;


public class Factory {
    public static void main(String[] args) {
        int resourceNum = 3;
        int threadNum = 0;
        String filename = "C:\\Users\\Dustin\\Desktop\\infile.txt";
        int nResources = 3;
        int[] resources = new int[nResources];
        File file = new File(filename);
        String numbers;
        boolean interactive = false;
        String choice;
        String response;
        Scanner in = new Scanner(System.in);
        Scanner rqrl = new Scanner(System.in);
        String autoReg = "[A|a]";
        String interReg = "[I|i]";
        String viewMatrices = "[\\*]";
        String reqRel = "((rl|rq) [0-9] [0-9] [0-9] [0-9])";
        int numAccum = 0;
        
        int[] num = new int[30];
        int accum = 0;
        Thread[] workers = new Thread[Customer.COUNT];  // the customers
        do{
            System.out.println("Interactive Mode[I] or Automatic Mode?[A]");
            choice = in.nextLine();
        }while(!(choice.matches(autoReg) || choice.matches(interReg)));
        
        Bank theBank = new BankImpl(resources);
        int[] maxDemand = new int[nResources];
        int[] allocated = new int[nResources];
        
        try{
        Scanner sc = new Scanner(file);
        String dummy;
        while(sc.hasNextLine()){
            numbers = sc.nextLine();
//                        System.out.println("numbers: " + numbers);
            for(int i = 0; i < 6; i++){
                try{
                    dummy = numbers.substring(0,numbers.indexOf(","));
                }catch(StringIndexOutOfBoundsException e){
                    dummy = numbers;
                }
//                            System.out.println("dummy: " + dummy);
                int nums = Integer.parseInt(dummy);
                num[accum] = nums;
                accum++;
//                            System.out.println("numbers: " + numbers);
                try{
                    numbers = numbers.substring(numbers.indexOf(",") + 1,numbers.length());
                }catch(StringIndexOutOfBoundsException e){
                    numbers = numbers;
                }
            }
        }
//                    for(int i = 0; i < num.length; i++){
//                        System.out.println(i + "\t" + num[i]);
//                    }
        for(int i = 0; i < 5;i++){
            for(int z = 0; z < 3; z++){
                allocated[z] = num[numAccum];
//                                    System.out.println("Allocated: " + allocated[z]);
                numAccum++;
            }

            for(int z = 0; z < 3; z++){
                maxDemand[z] = num[numAccum];
                numAccum++;
//                                    System.out.println("maxDemand: " + maxDemand[z]);
            }
//                                System.out.println("numAccum: " + numAccum);
            System.out.println("adding customer "+ threadNum + "...");
            workers[threadNum] = new Thread(new Customer(threadNum, maxDemand, theBank));
//                                System.out.println("threadNum: " + threadNum);
            theBank.addCustomer(threadNum, allocated, maxDemand);

            ++threadNum;        //theBank.getCustomer(threadNum);
            resourceNum = 0;
        }
        numAccum = 0;
        accum = 0;
        System.out.println("FACTORY: created threads");     // start the customers
//                            System.out.print("thread Num: " + threadNum + "\n");

        System.out.println("FACTORY: started threads");
}
catch (FileNotFoundException fnfe) 
{ 
    throw new Error("Unable to find file \"" + filename + "\"");
}
        
        if(choice.matches(interReg)){

            response = "";
            while(!(response.matches(reqRel) || response.matches(viewMatrices))){
            System.out.println("Enter * to get state of system , OR Enter <RQ | RL> "
                + "<customer number> <resource #0> <#1> <#2>...");
                response = rqrl.nextLine();
                response = response.toLowerCase();
                response = response.trim();
            }
                    //for (int i = 0; i < nResources; i++) { resources[i] = Integer.parseInt(args[i].trim()); }
//                    System.out.println("nResource: " + nResources);
//                    System.out.println("Workers: " + workers.length);
            while(response.matches(viewMatrices)||response.matches(reqRel)){
            if(response.matches(reqRel)){
                String rr = response.substring(0,2);
                System.out.println("rr: " + rr);
                response = response.substring(3, response.length());
                Scanner sc1 = new Scanner(response);
                int pNumber = sc1.nextInt();
                int [] dummy = new int[3];
                for(int i = 0; i < 3; i++){
                    dummy[i] = sc1.nextInt();
//                    System.out.println("dummy:" + i + ":" + dummy[i]);
                }
                rr = rr.toLowerCase();
                if(rr.matches("rq")){
                    theBank.requestResources(pNumber, dummy);
                }
                else{
                    theBank.releaseResources(pNumber, dummy);
                }
            }
            else if (response.matches(viewMatrices)){
                theBank.showMatrices();
            }
                System.out.println("Enter * to get state of system , OR Enter <RQ | RL> "
                + "<customer number> <resource #0> <#1> <#2>...");
                response = rqrl.nextLine();
                response = response.toLowerCase();
                response = response.trim();
            }
        }
        else if (choice.matches(autoReg)){
            for (int i = 0; i < Customer.COUNT; i++) { workers[i].start(); }
        }
    }
}

    



//-------------------------------------------------------------------------------------------------
// input file with initial allocations and max demands per process
//
// infile.txt
//
//0,1,0,7,5,3
//2,0,0,3,2,2
//3,0,2,9,0,2
//2,1,1,2,2,2
//0,0,2,4,3,3




