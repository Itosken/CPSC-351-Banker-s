/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dustin
 */
import java.io.*;
import java.util.*;

public class BankImpl implements Bank {
    private int n;			// the number of threads in the system
    private int m;			// the number of resources
    private int cust = 0;
    
    private int[] available; 	// the amount available of each resource
    private int[][] maximum; 	// the maximum demand of each thread
    private int[][] allocation;	// the amount currently allocated to each thread
    private int[][] need;		// the remaining needs of each thread
    
    private boolean[] exit;
    private boolean safe;
    
    private boolean AoI;

    private void showAllMatrices(int[][] alloc, int[][] max, int[][] need, String msg) {
//        System.out.println("Alloc: " + alloc.length + " " +"maxDemand: " + maximum.length + " " +"need: " + need.length);
        System.out.println();
        System.out.println("\t\tALLOCATED\t\t\tMAXIMUM\t\t\t\t  NEED");
        for(int i = 0;i < alloc.length;i++){
            for(int j = 0; j < 3;j++){
                if(j == 0){
                    System.out.print("\t\t[");
                }  
                if(exit[i]!= true){
                    System.out.print(alloc[i][j]);
                }
                else{
                    System.out.print("-");
                }
                if (j == 2){
                    System.out.print("]\t\t");
                }
                else{
                    System.out.print(" ");
                }
        }
        for(int j = 0; j < 3;j++){
            if(j == 0){
                System.out.print("\t\t[");
            }  
                if(exit[i]!= true){
                    System.out.print(max[i][j]);
                }
                else{
                    System.out.print("-");
                }
            if (j == 2){
                System.out.print("]\t\t");
            }
            else{
                System.out.print(" ");
            }
        }
        for(int j = 0; j < 3;j++){
            if(j == 0){
                System.out.print("\t\t[");
            }  
            if(exit[i]!= true){
                System.out.print(need[i][j]);
            }
            else{
                System.out.print("-");
            }
            if (j == 2){
                System.out.print("]\t\t");
            }
            else{
                System.out.print(" ");
            }
        }
            System.out.println();
        }
    }
 
    private void showMatrix(int[][] matrix, String title, String rowTitle) {
        // todo
        for(int i = 0; i < matrix.length;i++){
            for(int j = 0; j < 3;j++){
                if(j == 0){
                    System.out.print("[");
                }  
                System.out.print(matrix[i][j]);
                if (j == 2){
                    System.out.print("]");
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private void showVector(int[] vect, String msg) {
        System.out.print(msg + "=");
        System.out.print("[");
        for(int i = 0; i < vect.length;i++){
                if(i != (vect.length - 1)){
                    System.out.print(vect[i] + " ");
                }  
                else if (i == (vect.length - 1)){
                    System.out.print(vect[i] + "]");
                }
            }
    }

    public BankImpl(int[] resources) {      // create a new bank (with resources)
		// todo
                m = resources.length;
                available = new int[resources.length];
                for(int i = 0; i < m; i++){
                    available[i] = 10;
                }
                allocation = new int[5][resources.length];
                maximum = new int[5][resources.length];
                need = new int[5][resources.length];
                exit = new boolean[5];
                for(int i = 0; i < 5; i++){
                    exit[i] = false;
                }
//                for( int i = 0; i < resources.length; i++){
//                    available[i] = 0;
//                }
//                for(int i = 0; i < n; i++){
//                    for(int j = 0; j < m; j++){
//                        allocation[i][j] = 0;
//                        maximum[i][j] = 0;
//                        need[i][j] = maximum[i][j] - allocation[i][j];
//                    }
//                }
    }
                               // invoked by a thread when it enters the system;  also records max demand
    @Override
    public void addCustomer(int threadNum, int[] allocated, int[] maxDemand) {
		for(int i = 0; i < m; i++){
                    allocation[threadNum][i] = allocated[i];
                    maximum[threadNum][i] = maxDemand[i];
                    need[threadNum][i] = maximum[threadNum][i] - allocation[threadNum][i];
                }
                cust++;
   }

    @Override
    public void getState() {        // output state for each thread
		// todo
                boolean unsafe = false;
//                System.out.print("available=[");
//                for(int i = 0; i < 3; i++){
//                    if(i != 2){
//                        System.out.print(available[i] + " ");
//                    }
//                    else{
//                        System.out.println(available[i] + "]");
//                    }
//                }
                for(int i = 0; i < cust;i++){
                    int j = 0;
                        if(available[j] <= need[i][j] || available[j + 1] <= need[i][j+1] || available[j+2] <= need[i][j+2]){
                            unsafe = true;
                        }
                }
                if(unsafe){
                    safe = false;
                }
                else{
                    safe = true;
                }
                      
    }

    private boolean isSafeState (int threadNum, int[] request) {
		// todo -- actual banker's algorithm
                for(int i = 0; i < 3; i++){
                    if((available[0] - request[0]) < need[i][0]){
                        safe = false;
                    }
                }
                for(int i = 0; i < 3; i++){
                    if((available[1] - request[1]) < need[i][1]){
                        safe = false;
                    }
                }
                for(int i = 0; i < 3; i++){
                    if((available[2] - request[2]) < need[i][2]){
                        safe = false;
                    }
                }
                return true;
    }
                                // make request for resources. will block until request is satisfied safely
    public synchronized boolean requestResources(int threadNum, int[] request)  {
 		// todo
                String appen = "";
                String appen2 = "";
                appen = appen.concat("[");
                appen2 = appen2.concat("[");
                boolean notERe = false;
                boolean maxCap = false;
                int[] dummy = new int[3];
                if(!(maximum[threadNum][0] == allocation[threadNum][0] && maximum[threadNum][1] == allocation[threadNum][1] 
                        && maximum[threadNum][2] == allocation[threadNum][2])){      
                    for(int i = 0; i < 3; i++){
                        if(i != 2){
                            appen = appen.concat(request[i] + " ");
                        }
                        else{
                            appen = appen.concat(request[i] + "]");
                        }
                    }
                    for(int i = 0; i < 3; i++){
                        if(i != 2){
                            appen2 = appen2.concat(need[threadNum][i] + " ");
                            dummy[i] = need[threadNum][i];
                        }
                        else{
                            appen2 = appen2.concat(need[threadNum][i] + "]");
                            dummy[i] = need[threadNum][i];
                        }
                    }
                    int j = 0;
                    if(request[j] > need[threadNum][j] || request[j + 1] > need[threadNum][j + 1] || request[j+2] > need[threadNum][j+2]){
                        maxCap = true;
                    }
                    else if(request[j] > available[j] || request[j + 1] > available[j + 1] || request[j + 2] > available[j + 2]){
                        notERe = true;
                    }
                    else if(((request[j] + allocation[threadNum][j]) > maximum[threadNum][j]) ||
                            ((request[j+1] + allocation[threadNum][j+1]) > maximum[threadNum][j+1])||
                            ((request[j+2] + allocation[threadNum][j+2]) > maximum[threadNum][j+2])){
                        maxCap = true;
                    }
                    System.out.print("#P" + threadNum + " RQ: " + appen + ",needs: " +
                    appen2 + ",");
                    this.showVector(available, "available");
                    if(maxCap || notERe){
                        System.out.println(" DENIED");
                        return false;
                    }
                    else if(!maxCap && !notERe){
                        int[] dummy2 = new int[3];
                        for(int i = 0; i < 3; i++){
                            dummy2[i] = available[i] - request[i];
                        }
                            System.out.print(" APPROVED, #P" + threadNum + " now at");
                            for(int i = 0; i < m; i++){
                                allocation[threadNum][i] += request[i];
                                available[i] -= request[i];
                                need[threadNum][i] -= request[i];
                            }
                            appen2 = "";
                            for(int i = 0; i < 3; i++){
                                dummy[i] = allocation[threadNum][i];

                            }
                            this.showVector(dummy, appen2);
                            System.out.println();
                            this.showMatrices();
                            if((maximum[threadNum][0] == allocation[threadNum][0] && maximum[threadNum][1] == allocation[threadNum][1] 
                                && maximum[threadNum][2] == allocation[threadNum][2])){  
                                this.releaseResources(threadNum, request);
                            }                            
                            return true;

                    }
                }
                return false;
     }

    public synchronized void releaseResources(int threadNum, int[] release)  {
		// todo
                int j = 0;
                String appen = "";
                String appen2 = "";
                for(int i = 0; i < 3; i++){
                    if(i != 2){
                        appen = appen.concat(release[i] + " ");
                    }
                    else{
                        appen = appen.concat(release[i] + "]");
                    }
                }
                for(int i = 0; i < 3; i++){
                    if(i != 2){
                        appen2 = appen2.concat(need[threadNum][i] + " ");
                    }
                    else{
                        appen2 = appen2.concat(need[threadNum][i] + "]");
                    }
                }
                
                if(release[0] != 0 && release[1] != 0 && release[2] != 0 ){
                    if((release[j] <= allocation[threadNum][j] && release[j+1] <= allocation[threadNum][j+1] && 
                            release[j+2] <= allocation[threadNum][j+2]) && 
                            (((allocation[threadNum][j] - release[j]) >= 0) &&
                            ((allocation[threadNum][j+1] - release[j+1]) >= 0) &&
                            (allocation[threadNum][j+2] - release[j+2]) >= 0)){
                        for(int i = 0; i < 3; i++){
                            allocation[threadNum][i] -= release[i];
                            available[i] += release[i];
                            need[threadNum][i] += release[i];
                        }
                        System.out.print("#P" + threadNum + " RL: [" + appen + ",needs: [" +
                        appen2 + ",");
                        this.showVector(available, "available");
                        System.out.println(" APPROVED");
                        this.showMatrices();
                        if(maximum[threadNum][j] == allocation[threadNum][j] && maximum[threadNum][j + 1] == allocation[threadNum][j + 1] 
                                && maximum[threadNum][j+2] == allocation[threadNum][j+ 2] ){
                            if(exit[threadNum] == false){
                                System.out.println("#P" + threadNum + " does not need anymore resources\n"
                                    + "RELEASING & SHUTTING DOWN...");
                                for(int i = 0; i < m; i++){
                                    available[i] += allocation[threadNum][i];
                                }
                                exit[threadNum] = true;
                            }
                        }                        
                    }
                    else if(exit[threadNum] == true){
                        System.out.println("Customer does not exist.");
                    }
                    else{
                        System.out.print("#P" + threadNum + " RL: [" + appen + ",needs: [" +
                        appen2 + ",");
                        this.showVector(available, "available");
                        System.out.println(" DENIED"); 
                    }                    
                }
                else if(maximum[threadNum][j] == allocation[threadNum][j] && maximum[threadNum][j + 1] == allocation[threadNum][j + 1] 
                        && maximum[threadNum][j+2] == allocation[threadNum][j+ 2] ){
                    if(exit[threadNum] == false){
                        System.out.println("#P" + threadNum + " does not need anymore resources\n"
                            + "RELEASING & SHUTTING DOWN...");
                        for(int i = 0; i < m; i++){
                            available[i] += allocation[threadNum][i];
                        }
                        exit[threadNum] = true;
                    }
                }
                if(exit[0] && exit[1] && exit[2] && exit[3] && exit[4]){
                    System.exit(0);
                }
    }
    

    @Override
    public void showMatrices() {
        this.showVector(available, "available");
        this.showAllMatrices(allocation, maximum, need, "");
    }
}
//!(((release[j] + allocation[threadNum][j]) > maximum[threadNum][j]) ||
//                            ((release[j+1] + allocation[threadNum][j+1]) > maximum[threadNum][j+1])||
//                            ((release[j+2] + allocation[threadNum][j+2]) > maximum[threadNum][j+2])) && (
//                            release[j] <= available[j] && release[j+1] <= available[j+1] && release[j+2] <= available[j+2])