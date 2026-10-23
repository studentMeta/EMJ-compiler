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

public class VariableTest {

	private static final Logger LOG = LoggerFactory.getLogger(VariableTest.class);

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
	public void variable_assign_expressions_add() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_add.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions add - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_comparison() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_comparison.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions comparison - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_complex() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_complex.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions complex - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_div() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_div.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions div - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_minus() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_minus.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions minus - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_mult() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_mult.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions mult - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_not() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_not.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions not - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_expressions_other_variable() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_expressions_other_variable.moj",
				testFolder.newFile(),
				true,
				"Variable assignment expressions other variable - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_function_call_expression() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_assign_function_call_expression.moj",
				testFolder.newFile(),
				true,
				"Variable assignment function call expression - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_bool() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_declaration_bool.moj",
				testFolder.newFile(),
				true,
				"Variable declaration bool - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_char() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_declaration_char.moj",
				testFolder.newFile(),
				true,
				"Variable declaration char - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_int() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_declaration_int.moj",
				testFolder.newFile(),
				true,
				"Variable declaration int - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_minus() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_declaration_minus.moj",
				testFolder.newFile(),
				true,
				"Variable declaration minus - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_string() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_declaration_string.moj",
				testFolder.newFile(),
				true,
				"Variable declaration string - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_tuple() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_declaration_tuple.moj",
				testFolder.newFile(),
				true,
				"Variable declaration tuple - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_reassign() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/variable_reassign.moj",
				testFolder.newFile(),
				true,
				"Variable reassignment - OK test that should pass");
	}

	/*
	 * Author: Timothy Lefebvre
	 */
	@Test
	public void assignation_test_ok_Tim() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/passed/assignation_test_ok_Tim.moj",
				testFolder.newFile(),
				true,
				"Variable assignment complete - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_missing_semicolon() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_assign_missing_semicolon.moj",
				testFolder.newFile(),
				false,
				"Variable assignment with missing semicolon - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_assign_missing_value() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_assign_missing_value.moj",
				testFolder.newFile(),
				false,
				"Variable assignment with missing value - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_missing_name() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_declaration_missing_name.moj",
				testFolder.newFile(),
				false,
				"Variable declaration with missing name - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_missing_semicolon() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_declaration_missing_semicolon.moj",
				testFolder.newFile(),
				false,
				"Variable declaration with missing semicolon - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_missing_type() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_declaration_missing_type.moj",
				testFolder.newFile(),
				false,
				"Variable declaration with missing type - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_tuple_missing_inner_type() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_declaration_tuple_missing_inner_type.moj",
				testFolder.newFile(),
				false,
				"Variable declaration tuple with missing inner type - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void variable_declaration_tuple_missing_paren() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/variable_declaration_tuple_missing_paren.moj",
				testFolder.newFile(),
				false,
				"Variable declaration tuple with missing parentheses - KO test that should fail");
	}

	/*
	 * Author: Jérémy Dehaye
	 */
	@Test
	public void unit_test_tuple_components_KO() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/unit_test_tuple_components_KO.moj",
				testFolder.newFile(),
				false,
				"Tuple components - KO test that should fail");
	}

	/*
	 * Author: Jérémy Dehaye
	 */
	@Test
	public void unit_test_tuple_components_OK() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/unit_test_tuple_components_OK.moj",
				testFolder.newFile(),
				false,
				"Tuple components - OK test that should pass");
	}

	/*
	 * Author: Jérémy Dehaye
	 */
	@Test
	public void unit_test_tuple_KO() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/unit_test_tuple_KO.moj",
				testFolder.newFile(),
				false,
				"Tuple - KO test that should fail");
	}

	/*
	 * Author: Jérémy Dehaye
	 */
	@Test
	public void unit_test_tuple_OK() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/variables/failed/unit_test_tuple_OK.moj",
				testFolder.newFile(),
				false,
				"Tuple - KO test that should fail");
	}

}
