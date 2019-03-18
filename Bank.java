/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dustin
 */
public interface Bank {
    public void addCustomer(int threadNum, int[] maxDemand, int[] allocated);    // add customer to Bank

    public void getState();     // outputs available, allocation, max, and need matrices

           // request resources; specify number of customer being added, maxDemand for customer
           //      returns if request is grant
    public boolean requestResources(int threadNum, int[] request);

           // release resources
    public  void releaseResources(int threadNum, int[] release);
    
    public void showMatrices();
    
}
