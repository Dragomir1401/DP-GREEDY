import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Semnale {

	static int sig_type, x, y;
	static final int  mod = 1000000007;

	Semnale(){}

	static int min(int x, int y) {
		if (x > y) {
			return y;
		}
		return x;
	}

	static long type1() {
		// Create dp array
		long[][][] dp = new long[x + y + 1][x + 1][2];
		// dp[i][j][k] = Number of possibilities to have a correct
		// sequence with i elements, using j 0 values and finishing in value k

		// Intialise dp
		dp[1][1][0] = 1; // 1 element, 1 zero, finishing in 0 -> 0
		dp[1][0][1] = 1; // 1 element, 0 zeros, finishing in 1 -> 1

		// For size of sequence 2 to max size x + y
		for (int seqSize = 2; seqSize <= x + y; seqSize++) {
			// From using 1 zero to using max x zeros(seqSize zeros if lower than max x)
			for (int zeroCount = 1; zeroCount <= min(seqSize, x); zeroCount++) {
				// When we add an 0 we can add it over a 1 or over a 0
				// We choose zeroCount - 1 because at this step number of zeros change
				// from zeroCount - 1 to zeroCount
				dp[seqSize][zeroCount][0] = (dp[seqSize - 1][zeroCount - 1][0]
						+ dp[seqSize - 1][zeroCount - 1][1]) % mod;

				// When we add an 1 we can only add it over a 0, not over a 1 because the
				// sequence would break
				// And we chose zeroCount because number of zeros dont change since we add a 1
				dp[seqSize][zeroCount][1] = dp[seqSize - 1][zeroCount][0] % mod;
			}
		}

		// Answer is of length x + y, with x zeros, finishing in either 1 or 0
		return (dp[x + y][x][1] + dp[x + y][x][0]) % mod;
	}

	static long type2() {
		// Create dp array
		long[][][] dp = new long[x + y + 1][x + 1][2];
		// dp[i][j][k] = Number of posibilities to have a correct
		// sequence with i elements, using j 0 values and finsihing in value k

		// Initialise dp array
		dp[1][1][0] = 1; // 1 element, 1 zero, finishing in 0 -> 0
		dp[1][0][1] = 1; // 1 element, 0 zeros, finishing in 1 -> 1
		dp[2][2][0] = 1; // 2 elements, 2 zeros, finishing in 0 -> 00
		dp[2][1][0] = 1; // 2 element2, 1 zero, finishing in 0 -> 10
		dp[2][1][1] = 1; // 2 element2, 1 zero, finishing in 1 -> 01
		dp[2][0][1] = 1; // 2 element2, 0 zeros, finishing in 1 -> 11 still correct

		// Start after initialisation
		for (int seqSize = 3; seqSize <= x + y; seqSize++) {
			for (int zeroCount = 1; zeroCount <= min(seqSize, x); zeroCount++) {
				// When we add an 0 we can add it over an 1 or over a 0
				// We choose zeroCount - 1 because at this step number of zeros change
				// since we add a 0
				dp[seqSize][zeroCount][0] = (dp[seqSize - 1][zeroCount - 1][0]
						+ dp[seqSize - 1][zeroCount - 1][1]) % mod;

				// When we add an 1 we can only add it over an 1, but not over 2 ones
				// And we choose j because number of zeros dont change since we add an 1
				dp[seqSize][zeroCount][1] = (dp[seqSize - 1][zeroCount][0]
						+ dp[seqSize - 2][zeroCount][0]) % mod;
				// dp when adding 1       = anterior dp with a 0 to make sure it
				// 							didnt contain 11_ before  +
				// 							dp 2 steps back that finished in 0
				// 								meaning it could have been 00_ or 10_
			}
		}

		// Answer is of length x + y, with x zeros, finishing in either 1 or 0
		return (dp[x + y][x][1]+ dp[x + y][x][0]) % mod;
	}

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("semnale.in"));

			sig_type = sc.nextInt();
			x = sc.nextInt();
			y = sc.nextInt();

			long ans;
			switch (sig_type) {
				case 1:
					ans = Semnale.type1();
					break;
				case 2:
					ans = Semnale.type2();
					break;
				default:
					ans = -1;
					System.out.println("wrong task number");
			}

			try {
				FileWriter fw = new FileWriter("semnale.out");
				fw.write(Long.toString(ans));
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
