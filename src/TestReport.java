import java.util.ArrayList;

public class TestReport {
	private String title = new String();

	private ArrayList<TestCase> testCases = new ArrayList<TestCase>();

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void addTestCase(TestCase testCase) {
		this.testCases.add(testCase);
	}

	public ArrayList<TestCase> getTestCases() {
		return this.testCases;
	}

	public int getTestCasesNumber() {
		return this.testCases.size();
	}

	public TestCase getTestCaseIndex(int index) {
		return this.testCases.get(index);
	}

	public void printTestReport() {
		System.out.println("Test Report Title: " + this.getTitle());
		for (int i = 0; i < this.testCases.size(); i++) {
			TestCase testCase = testCases.get(i);
			System.out.println("Test case " + testCase.getNumber() + ": " + testCase.getTitle());
		}
	}
}
