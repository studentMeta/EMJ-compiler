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

public class FunctionTest {

	private static final Logger LOG = LoggerFactory.getLogger(FunctionTest.class);

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
	public void function_call_multiple_parameters() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_call_multiple_parameters.moj",
				testFolder.newFile(),
				true,
				"Function call with multiple parameters - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_call_no_parameters() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_call_no_parameters.moj",
				testFolder.newFile(),
				true,
				"Function call with no parameters - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_call_single_parameter() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_call_single_parameter.moj",
				testFolder.newFile(),
				true,
				"Function call with single parameter - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_all_types_parameters() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_declaration_all_types_parameters.moj",
				testFolder.newFile(),
				true,
				"Function declaration with all types of parameters - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_multiple_parameters() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_declaration_multiple_parameters.moj",
				testFolder.newFile(),
				true,
				"Function declaration with multiple parameters - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_no_parameters() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_declaration_no_parameters.moj",
				testFolder.newFile(),
				true,
				"Function declaration with no parameters - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_single_parameter() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_declaration_single_parameter.moj",
				testFolder.newFile(),
				true,
				"Function declaration with single parameter - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_return_bool() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_return_bool.moj",
				testFolder.newFile(),
				true,
				"Function return bool - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_return_char() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_return_char.moj",
				testFolder.newFile(),
				true,
				"Function return char - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_return_int() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_return_int.moj",
				testFolder.newFile(),
				true,
				"Function return int - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_return_string() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_return_string.moj",
				testFolder.newFile(),
				true,
				"Function return string - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_return_tuple() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_return_tuple.moj",
				testFolder.newFile(),
				true,
				"Function return tuple - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_return_void() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/function_return_void.moj",
				testFolder.newFile(),
				true,
				"Function return void - OK test that should pass");

	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_function_call_ok() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/passed/test_charles_function_call_ok.moj",
				testFolder.newFile(),
				true,
				"Function call - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_call_leading_comma() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_call_leading_comma.moj",
				testFolder.newFile(),
				false,
				"Function call with leading comma - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_call_missing_right_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_call_missing_right_paren.moj",
				testFolder.newFile(),
				false,
				"Function call with missing right parenthesis - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_call_missing_semicolon() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_call_missing_semicolon.moj",
				testFolder.newFile(),
				false,
				"Function call with missing semicolon - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_call_trailing_comma() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_call_trailing_comma.moj",
				testFolder.newFile(),
				false,
				"Function call with trailing comma - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_missing_left_brace() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_declaration_missing_left_brace.moj",
				testFolder.newFile(),
				false,
				"Function declaration with missing left brace - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_missing_left_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_declaration_missing_left_paren.moj",
				testFolder.newFile(),
				false,
				"Function declaration with missing left parenthesis - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_nonvoid_missing_return() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_declaration_nonvoid_missing_return.moj",
				testFolder.newFile(),
				false,
				"Function declaration with non-void return type and missing return statement - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_parameter_missing_comma() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_declaration_parameter_missing_comma.moj",
				testFolder.newFile(),
				false,
				"Function declaration with missing comma in parameter list - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_return_missing_value() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_declaration_return_missing_value.moj",
				testFolder.newFile(),
				false,
				"Function declaration with missing return value - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void function_declaration_void_wrong_return() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/function_declaration_void_wrong_return.moj",
				testFolder.newFile(),
				false,
				"Function declaration with void return type and incorrect return statement - KO test that should fail");
	}

	/*
	 * Author: Charles Eykerman
	 */
	@Test
	public void test_charles_function_call_ko() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/functions/failed/test_charles_function_call_ko.moj",
				testFolder.newFile(),
				false,
				"Function call - KO test that should fail");
	}
}
