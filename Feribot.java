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
				long carry = 0;
				long noIntervals = 0;

				for (long num : v) {
					carry += num;
					if (carry > target) {
						carry = num;
						noIntervals++;
					}
				}

				if (carry <= target) {
					noIntervals++;
				}

				return noIntervals;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private long getResult() {
			try {
				long left = calcMax(v);
				long right = calcSum(v);
				long sum = left;
				long pos = -1;

				// Binary search from max to total sum to find the optimum target min sum
				while (left <= right) {
					long mid = (left + right) / 2;
					long noIntervals = spread(v, n, mid);

					if (noIntervals <= k) {
						right = mid - 1;
						pos = mid;
					} else if (noIntervals > k) {
						left = mid + 1;
					}
				}


				// Return min target sum reached
				if (pos == -1) {
					return sum;
				}

				return pos;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
