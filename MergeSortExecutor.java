package com.example.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MergeSortExecutor {
	public static void main(String[] args) {

		Random random = new Random();
		List<Integer> list = random.ints(10000, 0, 100).boxed().collect(Collectors.toList());
		long startTime = System.nanoTime();
		final ExecutorService executor = Executors.newFixedThreadPool(10);
//		final ExecutorService executor = Executors.newWorkStealingPool(10);

		list = mergeSort(list);
//		list = mergeSort(list, executor);
		executor.shutdown();
		long elapsedTime = System.nanoTime() - startTime;

		System.out.println("Total execution time in millis: " + elapsedTime / 1000000);
//		System.out.println("finalList -> " + list);
	}

//	private static List<Integer> mergeSort(List<Integer> list, Executor executor) {
	private static List<Integer> mergeSort(List<Integer> list) {
		int beginIndex = 0, endIndex = list.size() - 1;
		if (beginIndex < endIndex && (endIndex - beginIndex) >= 1) {
			final ExecutorService executor = Executors.newFixedThreadPool(10);
//			final Executor executor = Executors.newWorkStealingPool(10);
			List<CompletableFuture<List<Integer>>> mergeFutures = new ArrayList<>();
			int mid = beginIndex + (endIndex - beginIndex) / 2;
			mergeFutures
					.add(CompletableFuture.supplyAsync(() -> mergeSort(list.subList(beginIndex, mid + 1)), executor));
			mergeFutures
					.add(CompletableFuture.supplyAsync(() -> mergeSort(list.subList(mid + 1, endIndex + 1)), executor));
			CompletableFuture.allOf(mergeFutures.toArray(new CompletableFuture[0])).join();
			try {
				return merge(mergeFutures.get(0).get(), mergeFutures.get(1).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	private static List<Integer> merge(List<Integer> list1, List<Integer> list2) {
		List<Integer> sortedList = new ArrayList<>();
		int i = 0, j = 0;
		while (i < list1.size() && j < list2.size()) {
			if (list1.get(i) <= list2.get(j)) {
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
