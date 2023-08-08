package com.example.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleMergeSort {
	public static void main(String[] args) {

		Random random = new Random();
		List<Integer> list = random.ints(1000000, 0, 500).boxed().collect(Collectors.toList());

		long startTime = System.nanoTime();
		mergeSort(list, 0, list.size() - 1);
		long elapsedTime = System.nanoTime() - startTime;

		System.out.println("Total execution time in millis: " + elapsedTime / 1000000);
//		System.out.println("finalList->" + list);

	}

	private static void mergeSort(List<Integer> list, int beginIndex, int endIndex) {

		if (beginIndex < endIndex && (endIndex - beginIndex) >= 1) {
			int mid = beginIndex + (endIndex - beginIndex) / 2;
			mergeSort(list, beginIndex, mid);
			mergeSort(list, mid + 1, endIndex);
			merge(list, beginIndex, endIndex);
		}

	}

	private static void merge(List<Integer> list, int beginIndex, int endIndex) {

		int i = beginIndex;
		int mid = beginIndex + (endIndex - beginIndex) / 2;
		int j = mid + 1;
		int temp = 0;
		ArrayList<Integer> tempList = new ArrayList<>(endIndex - beginIndex + 1);

		while (i <= mid && j <= endIndex) {
			if (list.get(i) <= list.get(j)) {
				tempList.add(temp, list.get(i));
				i++;
			} else {
				tempList.add(temp, list.get(j));
				j++;
			}
			temp++;
		}

		while (j <= endIndex) {
			tempList.add(temp, list.get(j));
			j++;
			temp++;
		}

		while (i <= mid) {
			tempList.add(temp, list.get(i));
			i++;
			temp++;
		}

		for (i = beginIndex, temp = 0; i <= endIndex; i++, temp++) {
			list.set(i, tempList.get(temp));
		}
	}
}
