import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Nostory {
	public static void main(final String[] args) throws IOException {
		var scanner = new MyScanner(new FileReader("nostory.in"));

		var task = scanner.nextInt();
		var n = scanner.nextInt();
		var moves = task == 2 ? scanner.nextInt() : 0;

		var a = new long[n];
		for (var i = 0; i < n; i += 1) {
			a[i] = scanner.nextLong();
		}

		var b = new long[n];
		for (var i = 0; i < n; i += 1) {
			b[i] = scanner.nextLong();
		}

		try (var printer = new PrintStream("nostory.out")) {
			if (task == 1) {
				printer.println(solveTask1(a, b));
			} else {
				printer.println(solveTask2(a, b, moves));
			}
		}
	}

	private static long solveTask1(long[] a, long[] b) {
		ArrayList<Long> c = new ArrayList<Long>();

		// Add all values from a to an arbitrary array c
		for (long num : a) {
			c.add(num);
		}

		// Add all values from b to an arbitrary array c
		for (long num : b) {
			c.add(num);
		}

		// Sort the resulting c
		Collections.sort(c);

		// Get first n / 2 maximum values to result
		long sum = 0;
		for (int i = c.size() - 1; i >= c.size() / 2; i--) {
			sum += c.get(i);
		}

		return sum;
	}

	static TreeMap<Long, Integer> createMap(long[] a, long[] b, int n) {
		// Add values to Tree Map so we have them sorted descending
		TreeMap<Long, Integer> visited = new TreeMap<Long, Integer>(Collections.reverseOrder());

		// Add to visited map with 1 as value(meaning visited) the bigger
		// element in pair (a[i], b[i])
		for (int i = 0; i < n; i++) {
			if (a[i] > b[i]) {
				visited.put(a[i], 1);
				visited.put(b[i], 0);
			} else {
				visited.put(b[i], 1);
				visited.put(a[i], 0);
			}
		}

		return visited;
	}

	static long computeResult(TreeMap<Long, Integer> visited, int n, int moves) {
		// Initialise result and counter
		long sum = 0;
		long cnt = 0;

		// Add all visited values and non visited while we still have moves
		for (Map.Entry<Long, Integer> entry : visited.entrySet()) {
			// If we reached target size in best solution we stop
			if (cnt == n) {
				break;
			}

			// If entry is visited we add it to result and increase counter
			if (entry.getValue() == 1) {
				sum += entry.getKey();
				cnt++;
			} else {
				// If it is not visited but we can make a move to shift it into
				// a pair where it would be
				// taken in consideration we do that and decrease number of available moves
				if (moves > 0) {
					sum += entry.getKey();
					moves--;
					cnt++;
				}
			}
		}

		return sum;
	}

	private static long solveTask2(long[] a, long[] b, int moves) {
		try {
			// Count elements in array b(or a would be equivalent)
			int n = 0;
			for (long num : b) {
				n++;
			}

			// Add values to Tree Map so we have them sorted descending
			TreeMap<Long, Integer> visited = createMap(a, b, n);

			// Compute the answer
			return computeResult(visited, n, moves);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A class for buffering read operations, inspired from here:
	 * https://pastebin.com/XGUjEyMN.
	 */
	private static class MyScanner {
		private BufferedReader br;
		private StringTokenizer st;

		public MyScanner(Reader reader) {
			br = new BufferedReader(reader);
		}

		public String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String nextLine() {
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
	}
}
