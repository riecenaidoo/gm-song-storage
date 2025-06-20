package com.bobo.semantic;

@SuppressWarnings("unused")
public interface Test {

	// --- Test Targets ---

	// Test Utilities

	// Mock Dependencies

	// --- Constructor ---

	// --- Test Setup ---

}

@SuppressWarnings("unused")
class ExampleTarget {

	public void method() {
		// ...
	}
}

@SuppressWarnings("unused")
@UnitTest(ExampleTarget.class)
class ExampleUnitTest {

	private ExampleTarget target;

	/**
	 * @see ExampleTarget#method()
	 */
	void method() {
		// ...
	}

	// OR

	/**
	 * @see ExampleTarget#method()
	 */
	@SuppressWarnings("InnerClassMayBeStatic")
	class Method {

		void method() {
			// ...
		}

		void negativePath() {
			// ...
		}
	}
}
