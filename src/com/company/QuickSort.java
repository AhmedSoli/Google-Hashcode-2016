package com.company;

/**
 * Created by Soli on 26/03/2017.
 */
public class QuickSort {

public static void quickSort(Product[] products, int low, int high) {
        if (products == null || products.length == 0)
            return;

        if (low >= high)
            return;

        // pick the pivot
        int middle = low + (high - low) / 2;
        int pivot = products[middle].productWeight;

        // make left < pivot and right > pivot
        int i = low, j = high;
        while (i <= j) {
            while (products[i].productWeight < pivot) {
                i++;
            }

            while (products[j].productWeight > pivot) {
                j--;
            }

            if (i <= j) {
                Product temp = products[i];
                products[i] = products[j];
                products[j] = temp;
                i++;
                j--;
            }
        }

        // recursively sort two sub parts
        if (low < j)
            quickSort(products, low, j);

        if (high > i)
            quickSort(products, i, high);
    }
}