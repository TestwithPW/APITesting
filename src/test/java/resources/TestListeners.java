package resources;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListeners implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Test Started : "  + result.getName());;
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Test Passed : "  + result.getName());;
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Test Failed : "  + result.getName());;
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Test Skipped: "  + result.getName());;
	}

	
}
