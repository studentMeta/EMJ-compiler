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

public class StructureTest {

	private static final Logger LOG = LoggerFactory.getLogger(StructureTest.class);

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
	public void main_function() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/passed/main_function.moj",
				testFolder.newFile(),
				true,
				"Main function - OK test that should pass");
	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_multi_ok() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/passed/test_charles_multi_ok.moj",
				testFolder.newFile(),
				true,
				"Main function complete - OK test that should pass");
	}

	/*
	 * Author: Timothy Lefebvre
	 */
	@Test
	public void test_01_tim() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/passed/test_01_tim.moj",
				testFolder.newFile(),
				true,
				"Main function complete - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void main_missing_import() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/main_missing_import.moj",
				testFolder.newFile(),
				false,
				"Main function with missing import - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void main_missing_return() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/main_missing_return.moj",
				testFolder.newFile(),
				false,
				"Main function with missing return - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void main_missing_right_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/main_missing_right_brace.moj",
				testFolder.newFile(),
				false,
				"Main function with missing right brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void main_no_main() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/main_no_main.moj",
				testFolder.newFile(),
				false,
				"Main function with no main - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void main_wrong_return() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/main_wrong_return.moj",
				testFolder.newFile(),
				false,
				"Main function with wrong return - KO test that should fail");
	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_no_main_ko() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/test_charles_no_main_ko.moj",
				testFolder.newFile(),
				false,
				"Main function with no main - KO test that should fail");
	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_no_map_import_ko() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/structure/failed/test_charles_no_map_import_ko.moj",
				testFolder.newFile(),
				false,
				"Main function with missing map import - KO test that should fail");
	}
}
