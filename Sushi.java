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
	static int computeMaxExt(int a, int b, int c) {
		if (a > b && a > c) {
			return a;
		}

		if (b > a && b > c) {
			return b;
		}
		return c;
	}

	static int task1() {
		try {
			int[] totalGrades = new int[m];
			for (int i = 0; i < m; i++) {
				totalGrades[i] = 0;
			}

			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					totalGrades[i] += grades[j][i];
				}
			}

			int[] dp = new int[n * x + 1];

			for (int i = 0; i < m; i++) {
				for (int j = n * x; j >= prices[i]; j--) {
					dp[j] = computeMax(dp[j], dp[j  - prices[i]] + totalGrades[i]);
				}
			}

			int maxim = 0;
			for (int i = 0; i <= n * x; i++) {
				if (dp[i] > maxim) {
					maxim = dp[i];
				}
			}

			return maxim;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static int[] extendPrices() {
		int mCopy = m * 2;
		int[] pricesExtended = new int[mCopy];

		for (int i = 0; i < m; i++) {
			pricesExtended[i] = prices[i];
		}

		for (int i = m; i < mCopy; i++) {
			pricesExtended[i] = prices[i - m];
		}
		return pricesExtended;
	}

	static int[] extendTotalGrades() {
		int mCopy = m * 2;
		int[] totalGrades = new int[mCopy];

		for (int i = 0; i < mCopy; i++) {
			totalGrades[i] = 0;
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				totalGrades[i] += grades[j][i];
			}
		}


		for (int i = m; i < mCopy; i++) {
			for (int j = 0; j < n; j++) {
				totalGrades[i] += grades[j][i - m];
			}
		}

		return totalGrades;
	}

	static int task2() {
		int mCopy = m * 2;
		int[] pricesExtended = extendPrices();

		int[] totalGrades = extendTotalGrades();

		try {


			int[] dp = new int[n * x + 1];

			for (int i = 0; i < mCopy; i++) {
				for (int j = n * x; j >= pricesExtended[i]; j--) {
					dp[j] = computeMax(dp[j], dp[j  - pricesExtended[i]] + totalGrades[i]);
				}
			}


			int maxim = 0;
			for (int i = 0; i <= n * x; i++) {
				if (dp[i] > maxim) {
					maxim = dp[i];
				}
			}

			return maxim;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static int task3() {
		try {

			int mCopy = m * 2;

			PrintWriter pw = new PrintWriter(new File("debug"));
			// dp[i][j][k] = maximum sum for obtaining price i using j total
			// grades using first k dishes
			int[][][] dp = new int[mCopy + 1][n * x + 1][n + 1];

			int[] pricesExtended = extendPrices();
			int[] totalGrades = extendTotalGrades();
			int result = 0;

			dp[0][computeMin(pricesExtended[0], n * x)][1] = totalGrades[0];

			for (int i = 1; i < mCopy; i++) {
				for (int j = 1; j <= n * x; j++) {
					for (int k = 1; k <= computeMin(n, i + 1); k++) {
						if (pricesExtended[i] <= j) {
							dp[i][j][k] = computeMaxExt(dp[i][j][k], dp[i - 1][j - pricesExtended[i]][k - 1] + totalGrades[i], dp[i - 1][j][k]);

						} else {
							dp[i][j][k] = dp[i - 1][j][k];
						}

						result = computeMax(result, dp[i][j][k]);
					}
				}
			}

			pw.close();

			return result;
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
