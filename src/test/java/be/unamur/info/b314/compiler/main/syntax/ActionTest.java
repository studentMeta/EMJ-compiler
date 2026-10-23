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

public class ActionTest {

	private static final Logger LOG = LoggerFactory.getLogger(ActionTest.class);

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
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_cutebot_basic_actions_ok() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/passed/test_charles_cutebot_basic_actions_ok.moj",
				testFolder.newFile(),
				true,
				"Cutebot basic actions - OK test that should pass");
	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_cutebot_actions_with_parameters_ok() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/passed/test_charles_cutebot_actions_with_parameters_ok.moj",
				testFolder.newFile(),
				true,
				"Cutebot actions with parameters - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void predefined_actions() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/passed/predefined_actions.moj",
				testFolder.newFile(),
				true,
				"Cutebot predefined actions - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void predefined_actions_expressions() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/passed/predefined_actions_expressions.moj",
				testFolder.newFile(),
				true,
				"Cutebot predefined actions with expressions - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_cutebot_basic_actions_ko() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/failed/test_charles_cutebot_basic_actions_ko.moj",
				testFolder.newFile(),
				false,
				"Cutebot basic actions - KO test that should fail");
	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_cutebot_actions_with_parameters_ko() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/failed/test_charles_cutebot_actions_with_parameters_ko.moj",
				testFolder.newFile(),
				false,
				"Cutebot actions with parameters - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void predefined_action_missing_argument() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/failed/predefined_action_missing_argument.moj",
				testFolder.newFile(),
				false,
				"Cutebot predefined actions with missing argument - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void predefined_action_missing_left_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/actions/failed/predefined_action_missing_left_paren.moj",
				testFolder.newFile(),
				false,
				"Cutebot predefined actions with missing left parenthesis - KO test that should fail");
	}
}
