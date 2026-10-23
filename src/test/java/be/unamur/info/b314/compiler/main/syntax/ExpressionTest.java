package be.unamur.info.b314.compiler.main.syntax;

import be.unamur.info.b314.compiler.main.CompilerTestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpressionTest {

	private static final Logger LOG = LoggerFactory.getLogger(ExpressionTest.class);

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			LOG.info(String.format("Starting test: %s()...", description.getMethodName()));
		}
	};

	// ========================================================================
	// TEST PASSED
	// ========================================================================

	/*
	 * Author: Timothy Lefebvre
	 */
	@Test
	public void test_comparator_Tim() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/expressions/passed/test_comparator_Tim.moj",
				testFolder.newFile(),
				true,
				"Comparator test - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Jérémy Dehaye
	 */
	@Test
	public void unit_test_boolean_OK() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/expressions/failed/unit_test_boolean_OK.moj",
				testFolder.newFile(),
				false,
				"Boolean test - KO test that should fail");
	}

	/*
	 * Author: Jérémy Dehaye
	 */
	@Test
	public void unit_test_complex_expression_OK() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/expressions/failed/unit_test_complex_expression_OK.moj",
				testFolder.newFile(),
				false,
				"Complex expression test - KO test that should fail");
	}
}
