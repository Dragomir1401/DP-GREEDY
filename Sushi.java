import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class Sushi {

	static int n, m, x;
	static int[] prices;
	static int[][] grades;

	Sushi(){}

	static int computeMax(int a, int b) {
		if (a > b) {
			return a;
		}
		return b;
	}

	static int computeMin(int a, int b) {
		if (a > b) {
			return b;
		}
		return a;
	}
	static int computeMaxThreeElems(int a, int b, int c) {
		if (a > b && a > c) {
			return a;
		}

		if (b > a && b > c) {
			return b;
		}
		return c;
	}

	static int computeMaxArray(int[] arr, int n) {
		int max = 0;
		for (int i = 0; i < n; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}

	static int[] computeTotalGrades() {
		// Initialise total grades from firend with 0
		int[] totalGrades = new int[m];
		for (int i = 0; i < m; i++) {
			totalGrades[i] = 0;
		}

		// Add each friend grade in the total grades array
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				totalGrades[i] += grades[j][i];
			}
		}

		return totalGrades;
	}
	static int task1() {
		try {
			// Create total grades
			int[] totalGrades = computeTotalGrades();

			// Initialise dp array
			int[] dp = new int[n * x + 1];

			// For each dish
			for (int i = 0; i < m; i++) {
				// For target max price to price of each dish
				for (int j = n * x; j >= prices[i]; j--) {
					// Dp is max between himself or anterior dp plus the new element added now
					dp[j] = computeMax(dp[j], dp[j  - prices[i]] + totalGrades[i]);
				}
			}

			// Return max in the dp array
			return computeMaxArray(dp, n * x + 1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static int[] extendPrices() {
		// Extend the prices array by doubling it so we can take max 2 of each dish
		int mCopy = m * 2;
		int[] pricesExtended = new int[mCopy];

		// Put first m elements
		for (int i = 0; i < m; i++) {
			pricesExtended[i] = prices[i];
		}

		// Put last m elements
		for (int i = m; i < mCopy; i++) {
			pricesExtended[i] = prices[i - m];
		}

		return pricesExtended;
	}

	static int[] extendTotalGrades() {
		// Extend the totalGrades array by doubling it so we can take max 2 of each dish
		int mCopy = m * 2;
		int[] totalGrades = new int[mCopy];

		// Initialise total grades
		for (int i = 0; i < mCopy; i++) {
			totalGrades[i] = 0;
		}

		// Put first m elements
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				totalGrades[i] += grades[j][i];
			}
		}

		// Put last m elements
		for (int i = m; i < mCopy; i++) {
			for (int j = 0; j < n; j++) {
				totalGrades[i] += grades[j][i - m];
			}
		}

		return totalGrades;
	}

	static int task2() {
		try {
			// Extend prices and totalGrades wso we can take up to 2 identic dishes
			int mCopy = m * 2;
			int[] pricesExtended = extendPrices();
			int[] totalGrades = extendTotalGrades();

			// Initialise dp
			int[] dp = new int[n * x + 1];

			// For each dish
			for (int i = 0; i < mCopy; i++) {
				// From target max price to price of each dish
				for (int j = n * x; j >= pricesExtended[i]; j--) {
					// Dp is max between himself or anterior dp plus the new element added now
					dp[j] = computeMax(dp[j], dp[j  - pricesExtended[i]] + totalGrades[i]);
				}
			}

			// Answer is max in dp array
			return computeMaxArray(dp, n * x + 1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static int createDp(int[][][] dp, int[] totalGrades, int[] pricesExtended, int mCopy) {
		int result = 0;

		// For each dish
		for (int dish = 1; dish < mCopy; dish++) {
			// For each price from 1 to max price
			for (int price = 1; price <= n * x; price++) {
				// For using exactly dishCOunt dishes
				for (int dishCount = 1; dishCount <= computeMin(n, dish + 1); dishCount++) {
					// If the price of the dish is lower current dp element(therefore the max
					// price in the end)
					if (pricesExtended[dish] <= price) {
						// Dp is the max between himself, the anterior dp using k - 1
						// elements instead of k
						// and adding the current dish grade and the value of the anterior
						// dp if it is not
						// favorable to take the current dish
						dp[dish][price][dishCount] =
								computeMaxThreeElems(dp[dish][price][dishCount],
								dp[dish - 1][price - pricesExtended[dish]][dishCount - 1]
										+ totalGrades[dish],
								dp[dish - 1][price][dishCount]);

					} else {
						// Dp is the anterior dp without taking the current dish
						dp[dish][price][dishCount] = dp[dish - 1][price][dishCount];
					}

					// Result is max in the dp array iin the end
					result = computeMax(result, dp[dish][price][dishCount]);
				}
			}
		}
		return result;
	}

	static int task3() {
		try {
			int mCopy = m * 2;
			// dp[i][j][k] = maximum grade using the first i dishes with price j
			// and using exactly k dishes
			int[][][] dp = new int[mCopy + 1][n * x + 1][n + 1];

			// Extend prices, totalGrades and initalise result
			int[] pricesExtended = extendPrices();
			int[] totalGrades = extendTotalGrades();

			// Initialise dp for first dish price
			// If it exceeds max price target then initialise that field instead
			// with its corresponding grade
			dp[0][computeMin(pricesExtended[0], n * x)][1] = totalGrades[0];

			// Compute result
			return createDp(dp, totalGrades, pricesExtended, mCopy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("sushi.in"));

			final int task = sc.nextInt(); // task number

			n = sc.nextInt(); // number of friends
			m = sc.nextInt(); // number of sushi types
			x = sc.nextInt(); // how much each of you is willing to spend

			prices = new int[m]; // prices of each sushi type
			grades = new int[n][m]; // the grades you and your friends gave to each sushi type

			// price of each sushi
			for (int i = 0; i < m; ++i) {
				prices[i] = sc.nextInt();
			}

			// each friends rankings of sushi types
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < m; ++j) {
					grades[i][j] = sc.nextInt();
				}
			}

			int ans;
			switch (task) {
				case 1:
					ans = Sushi.task1();
					break;
				case 2:
					ans = Sushi.task2();
					break;
				case 3:
					ans = Sushi.task3();
					break;
				default:
					ans = -1;
					System.out.println("wrong task number");
			}

			try {
				FileWriter fw = new FileWriter("sushi.out");
				fw.write(Integer.toString(ans) + '\n');
				fw.close();

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
