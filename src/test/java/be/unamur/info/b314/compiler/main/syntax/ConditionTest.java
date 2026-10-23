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

public class ConditionTest {

	private static final Logger LOG = LoggerFactory.getLogger(ConditionTest.class);

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
	public void if_else_condition() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_condition.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with condition - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_constant() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_constant.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with constant - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_expression() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_expression.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with expression - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_function() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_function.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with function - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_nested() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_nested.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with nested - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_no_instruction() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_no_instruction.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with no instruction - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_skip() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/if_else_skip.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with skip - OK test that should pass");
	}

	/*
	 * Author: Timothy Lefebvre
	 */
	@Test
	public void test_il_else_tim() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/passed/test_il_else_tim.moj",
				testFolder.newFile(),
				true,
				"IF-ELSE with complete - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_else_skip_missing_semicolon() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_else_skip_missing_semicolon.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with skip missing semicolon - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_missing_condition() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_missing_condition.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with missing condition - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_missing_else() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_missing_else.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with missing else - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_missing_left_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_missing_left_brace.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with missing left brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_missing_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_missing_paren.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with missing parenthesis - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_missing_right_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_missing_right_brace.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with missing right brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void if_missing_right_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/conditions/failed/if_missing_right_paren.moj",
				testFolder.newFile(),
				false,
				"IF-ELSE with missing right parenthesis - KO test that should fail");
	}
}
