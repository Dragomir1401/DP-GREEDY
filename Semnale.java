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
		dp[1][1][0] = 1;
		dp[1][0][1] = 1;
		// dp[i][j][k] = Number of posibilities to have a correct
		// sequence with i elements, using j 0 values and finsihing in value k

		for (int i = 2; i <= x + y; i++) {
			for (int j = 1; j <= min(i, x); j++) {
				// When I add an 0 I can add it over a 1 or over a 0
				// I chose j - 1 because at this step number of zeros change
				dp[i][j][0] = (dp[i - 1][j - 1][0] + dp[i - 1][j - 1][1]) % mod;

				// When I add an 1 I can only add it over a 0, not over a 1
				// And I chose j because number of zeros dont change
				dp[i][j][1] = dp[i - 1][j][0] % mod;

			}
		}

		// Answer is of length x + y, with x zeros, finishing in either 1 or 0
		return (dp[x + y][x][1] % mod + dp[x + y][x][0] % mod) % mod;
	}

	static long type2() {
		// Create dp array
		long[][][] dp = new long[x + y + 1][x + 1][2];
		dp[1][1][0] = 1;
		dp[1][0][1] = 1;
		dp[2][2][0] = 1;
		dp[2][1][0] = 1;
		dp[2][1][1] = 1;
		dp[2][0][1] = 1;

		// dp[i][j][k] = Number of posibilities to have a correct
		// sequence with i elements, using j 0 values and finsihing in value k

		for (int i = 3; i <= x + y; i++) {
			for (int j = 1; j <= min(i, x); j++) {
				// When I add an 0 I can add it over a 1 or over a 0
				// I chose j - 1 because at this step number of zeros change
				dp[i][j][0] = (dp[i - 1][j - 1][0] + dp[i - 1][j - 1][1]) % mod;

				// When I add an 1 I can only add it over a 0, not over a 1
				// And I chose j because number of zeros dont change
				dp[i][j][1] = (dp[i - 1][j][0] % mod + dp[i - 2][j][0] % mod) % mod;

			}
		}

		// Answer is of length x + y, with x zeros, finishing in either 1 or 0
		return (dp[x + y][x][1] % mod + dp[x + y][x][0] % mod) % mod;
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
