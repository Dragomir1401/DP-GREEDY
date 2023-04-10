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
		void multiply_matrix(long A[][], long B[][], long C[][]) {
			long tmp[][] = new long[KMAX][KMAX];

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

			for (int i = 0; i < KMAX; ++i) {
				for (int j = 0; j < KMAX; ++j) {
					C[i][j] = tmp[i][j];
				}
			}

		}

		// R = C^p
		void power_matrix(long C[][], long p, long R[][]) {
			// tmp = I (matricea identitate)
			long tmp[][] = new long[KMAX][KMAX];
			for (int i = 0; i < KMAX; ++i) {
				for (int j = 0; j < KMAX; ++j) {
					tmp[i][j] = (i == j) ? 1 : 0;
				}
			}

			while (p != 1) {
				if  (p % 2 == 0) {
					multiply_matrix(C, C, C);     // C = C*C
					p /= 2;                       // rămâne de calculat C^(p/2)
				} else {
					// reduc la cazul anterior:
					multiply_matrix(tmp, C, tmp); // tmp = tmp*C
					--p;                          // rămâne de calculat C^(p-1)
				}
			}

			// avem o parte din rezultat în C și o parte în tmp
			multiply_matrix(C, tmp, R);           // rezultat = tmp * C
		}

		private long calculateFibonacci(long n, PrintWriter pw) {
			try {
				// Generate first matrix in recursion
				long M0[][] = new long[KMAX][KMAX];
				M0[0][0] = 0;
				M0[0][1] = 1;
				M0[1][0] = 0;
				M0[1][1] = 0;

				// Generate matrix for computing next step in multiplication
				long[][] trans = new long[KMAX][KMAX];
				trans[0][0] = 0;
				trans[0][1] = 1;
				trans[1][0] = 1;
				trans[1][1] = 1;

				// We want to compute trans ^ (n - 1)
				long[][] powered = new long[KMAX][KMAX];
				power_matrix(trans, n, powered);

				return powered[1][1] % MOD;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		private long getResult() {
			try {
				long result = 1;
				boolean interiorOfSequence = false;
				long number = 0;
				PrintWriter pw = new PrintWriter(new File("debug.out"));

				for (int i = 0; i < input.length(); i++){
					char c = input.charAt(i);

					if (Character.isAlphabetic(c) == true) {
						interiorOfSequence = false;

						if (number > 1) {
							result = (result * calculateFibonacci(number, pw)) % MOD;
						}

						number = 0;
					}

					if (interiorOfSequence == true) {
						long a = c - 48;
						number = number * 10 + a;
					}

					if (c == 'n' || c == 'u') {
						interiorOfSequence = true;
					}

				}

				if (number > 1) {
					result = (result * calculateFibonacci(number, pw)) % MOD;
				}


				pw.close();
				return result;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
