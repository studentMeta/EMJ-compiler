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

public class LoopTest {

	private static final Logger LOG = LoggerFactory.getLogger(LoopTest.class);

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
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_loop_condition() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/for_loop_condition.moj",
				testFolder.newFile(),
				true,
				"For loop with condition - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_loop_constant() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/for_loop_constant.moj",
				testFolder.newFile(),
				true,
				"For loop with constant - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_loop_expression() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/for_loop_expression.moj",
				testFolder.newFile(),
				true,
				"For loop with expression - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_loop_function() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/for_loop_function.moj",
				testFolder.newFile(),
				true,
				"For loop with function - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_loop_variable() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/for_loop_variable.moj",
				testFolder.newFile(),
				true,
				"For loop with variable - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_loop_condition() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/while_loop_condition.moj",
				testFolder.newFile(),
				true,
				"While loop with condition - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_loop_constant() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/while_loop_constant.moj",
				testFolder.newFile(),
				true,
				"While loop with constant - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_loop_expression() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/while_loop_expression.moj",
				testFolder.newFile(),
				true,
				"While loop with expression - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_loop_function() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/while_loop_function.moj",
				testFolder.newFile(),
				true,
				"While loop with function - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_loop_nested() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/while_loop_nested.moj",
				testFolder.newFile(),
				true,
				"While loop with nested loops - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_loop_variable() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/while_loop_variable.moj",
				testFolder.newFile(),
				true,
				"While loop with variable - OK test that should pass");
	}

	/*
	 * Author: Timothy Lefebvre
	 */
	@Test
	public void test_loops_Tim() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/passed/test_loops_Tim.moj",
				testFolder.newFile(),
				true,
				"For/while loop complete - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_missing_left_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/failed/for_missing_left_brace.moj",
				testFolder.newFile(),
				false,
				"For loop with missing left brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void for_missing_left_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/failed/for_missing_left_paren.moj",
				testFolder.newFile(),
				false,
				"For loop with missing left parenthesis - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_missing_left_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/failed/while_missing_left_brace.moj",
				testFolder.newFile(),
				false,
				"While loop with missing left brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_missing_left_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/failed/while_missing_left_paren.moj",
				testFolder.newFile(),
				false,
				"While loop with missing left parenthesis - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_missing_right_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/failed/while_missing_right_brace.moj",
				testFolder.newFile(),
				false,
				"While loop with missing right brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void while_missing_right_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/loops/failed/while_missing_right_paren.moj",
				testFolder.newFile(),
				false,
				"While loop with missing right parenthesis - KO test that should fail");
	}
}
