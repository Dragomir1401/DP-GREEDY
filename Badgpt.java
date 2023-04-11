import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Badgpt {
	static class Task {
		String input;
		static final int  MOD = 1000000007;

		static final int  KMAX = 2;


		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File("badgpt.in"));
				input = sc.next();
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new File("badgpt.out"));
				pw.printf("%d\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// C = A * B
		void multiply_matrix(long[][] A, long[][] B, long[][] C) {
			// Function which multiples the matrices A and B and puts result in C
			// C = A * B
			long[][] tmp = new long[KMAX][KMAX];

			// tmp = A * B
			for (int i = 0; i < KMAX; ++i) {
				for (int j = 0; j < KMAX; ++j) {
					long sum = 0;

					for (int k = 0; k < KMAX; ++k) {
						sum += A[i][k] * B[k][j];
					}

					tmp[i][j] = sum % MOD;
				}
			}

			// Copy tmp in C
			for (int i = 0; i < KMAX; ++i) {
				for (int j = 0; j < KMAX; ++j) {
					C[i][j] = tmp[i][j];
				}
			}

		}

		// R = C^p
		void power_matrix(long[][] C, long p, long[][] R) {
			// Function which retrives C ^ p in the result R

			// tmp = I (Identitty matrix)
			long[][] tmp = new long[KMAX][KMAX];
			for (int i = 0; i < KMAX; ++i) {
				for (int j = 0; j < KMAX; ++j) {
					tmp[i][j] = (i == j) ? 1 : 0;
				}
			}

			while (p != 1) {
				if  (p % 2 == 0) {
					multiply_matrix(C, C, C);     // C = C * C
					p /= 2;                       // remains to be determined C^(p/2)
				} else {
					// reduce to the anterior case
					multiply_matrix(tmp, C, tmp); // tmp = tmp * C
					--p;                          // remains to be determined C^(p-1)
				}
			}

			// We have a part of the result in C and the other in tmp
			multiply_matrix(C, tmp, R);           // result = tmp * C
		}

		private long calculateFibonacci(long n) {
			try {
				// Generate matrix for computing next step in multiplication
				long[][] trans = new long[KMAX][KMAX];
				trans[0][0] = 0;
				trans[0][1] = 1;
				trans[1][0] = 1;
				trans[1][1] = 1;

				// We want to compute trans ^ n = powered
				long[][] powered = new long[KMAX][KMAX];
				power_matrix(trans, n, powered);

				// Answer would be [0 1] * (trans ^ n) = (F_n F_n-1) so we have to take
				// the bottom right corner of the trans matrix since that will be
				// equal to F_n searched
				return powered[1][1] % MOD;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private long parseInput() {
			// Initialise number constructor with 0 and result with 1
			long number = 0;
			long result = 1;

			// Variable for tracking if we are in a sequence of numbers we have to track
			boolean interiorOfSequence = false;

			// For each char in input
			for (int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);

				// If we find a letter we update answer and reset number we formed when
				// we found 'u' or 'n'
				if (Character.isAlphabetic(c) == true) {
					interiorOfSequence = false;
					if (number > 1) {
						result = (result * calculateFibonacci(number)) % MOD;
					}
					number = 0;
				}

				// If we are in a found sequence of numbers then construct a number with
				// those chars
				if (interiorOfSequence == true) {
					long a = c - 48;
					number = number * 10 + a;
				}

				// If letter is target letter then begin sequence
				if (c == 'n' || c == 'u') {
					interiorOfSequence = true;
				}
			}

			// Add to result the last computation if input ended with a
			// sequence of 'u' or 'n'
			if (number > 1) {
				result = (result * calculateFibonacci(number)) % MOD;
			}

			return result;
		}
		private long getResult() {
			try {
				return parseInput();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
