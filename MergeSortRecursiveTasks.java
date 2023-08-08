package com.example.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class MergeSortRecursiveTasks {
	public static void main(String[] args) {
		Random random = new Random();
		List<Integer> list = random.ints(1000000, 0, 100).boxed().collect(Collectors.toList());

		long startTime = System.nanoTime();
		ForkJoinPool pool = new ForkJoinPool(4);

		RecursiveMS ms = new RecursiveMS(list);
		System.out.println(pool.invoke(ms));

		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Total execution time in millis: " + elapsedTime / 1000000);
	}
}

class RecursiveMS extends RecursiveTask<List<Integer>> {

	List<Integer> arraylist;
	RecursiveMS left;
	RecursiveMS right;

	public RecursiveMS(List<Integer> arraylist) {
		super();
		this.arraylist = arraylist;
	}

	@Override
	protected List<Integer> compute() {
		int beginIndex = 0, endIndex = this.arraylist.size() - 1;
		if (beginIndex < endIndex && (endIndex - beginIndex) >= 1) {
			Integer mid = (endIndex - beginIndex) / 2;
			left = new RecursiveMS(this.arraylist.subList(beginIndex, mid + 1));
			right = new RecursiveMS(this.arraylist.subList(mid + 1, endIndex + 1));
			left.fork();
			right.fork();
			List<Integer> leftList = left.join();
			List<Integer> rightList = right.join();
			return merge(leftList, rightList);
		}
		return this.arraylist;
	}

	public static List<Integer> merge(List<Integer> list1, List<Integer> list2) {
		List<Integer> sortedList = new ArrayList<>(list1.size() + list2.size());
		int i = 0, j = 0;
		while (i < list1.size() && j < list2.size()) {

			if (list1.get(i) < list2.get(j)) {
				sortedList.add(list1.get(i));
				i++;
			} else {
				sortedList.add(list2.get(j));
				j++;
			}
		}

		while (j < list2.size()) {
			sortedList.add(list2.get(j));
			j++;
		}

		while (i < list1.size()) {
			sortedList.add(list1.get(i));
			i++;
		}
		return sortedList;
	}

}
