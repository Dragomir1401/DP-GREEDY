import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Feribot {
	static class Task {
		int n;
		long k;
		long[] v;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File("feribot.in"));
				n = sc.nextInt();
				k = sc.nextLong();
				v = new long[n + 1];
				for (int i = 1; i <= n; i++) {
					v[i] = sc.nextLong();
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new File("feribot.out"));
				pw.printf("%d\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private long calcMax(long[] v) {
			long res = 0;
			for (long num : v) {
				if (num > res) {
					res = num;
				}
			}
			return res;
		}

		private long calcSum(long[] v) {
			long res = 0;
			for (long num : v) {
				res += num;
			}
			return res;
		}

		private long spread(long[] v, long n, long target) {
			try {
				// Variable for storing sum
				long carry = 0;

				// Result
				long noIntervals = 0;

				// For each value in array
				for (long num : v) {
					// Add to sum storage
					carry += num;

					// If it surpasses the target sum we reset it to 0 and add current number
					// to it adn increase result
					if (carry > target) {
						carry = 0 + num;
						noIntervals++;
					}
				}

				// Final check to match the last cars left without a feribot
				if (carry <= target) {
					noIntervals++;
				}

				return noIntervals;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		long binarySearch(long pos, long sum) {
			long left = calcMax(v); // Initialize left to the maximum value in v
			long right = calcSum(v); // Initialize right to the sum of all elements in v

			while (left < right) {
				long mid = (left + right) >> 1; // Compute mid using bit shift

				// Find the number of feribots we need for this value
				long noIntervals = spread(v, n, mid);

				// Restrain the interval based on how our current solution states
				if (noIntervals <= k) {
					// If the number of intervals is less than or equal to k, we have
					// a valid solution
					right = mid;
					pos = mid;
				} else {
					// If the number of intervals is greater than k, we need to increase the sum
					left = mid + 1;
				}
			}

			return pos;
		}

		private long getResult() {
			try {
				// Initialise pos for binary search
				long pos = -1;

				// Compute max in array for most favorable case
				long sum = calcMax(v);

				// Update pos using binary search
				pos = binarySearch(pos, sum);

				// If pos was not updated then return most favorable case
				return (pos == -1 ? sum : pos);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
