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

public class CommentTest {

	private static final Logger LOG = LoggerFactory.getLogger(CommentTest.class);

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
	public void comment_block_anywhere() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/passed/comment_block_anywhere.moj",
				testFolder.newFile(),
				true,
				"Comment block anywhere - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void comment_block_multiple() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/passed/comment_block_multiple.moj",
				testFolder.newFile(),
				true,
				"Comment block multiple - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void comment_block_single() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/passed/comment_block_single.moj",
				testFolder.newFile(),
				true,
				"Comment block single - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void comment_line_anywhere() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/passed/comment_line_anywhere.moj",
				testFolder.newFile(),
				true,
				"Comment line anywhere - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void comment_line_multiple() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/passed/comment_line_multiple.moj",
				testFolder.newFile(),
				true,
				"Comment line multiple - OK test that should pass");
	}

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void comment_line_single() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/passed/comment_line_single.moj",
				testFolder.newFile(),
				true,
				"Comment line single - OK test that should pass");
	}

	// ========================================================================
	// TEST FAILED
	// ========================================================================

	/*
	 * Author: Sacha Pauwels
	 */
	@Test
	public void comment_block_not_closed() throws Exception {
		CompilerTestHelper.launchCompilation(
				"/syntax/comments/failed/comment_block_not_closed.moj",
				testFolder.newFile(),
				false,
				"Comment block not closed - KO test that should fail");
	}

	/*
	 * Author: Sacha Pauwels
	 *
	 * The test is not compatible with the testing framework
	 * 
	 * @Test
	 * public void comment_block_not_opened() throws Exception {
	 * CompilerTestHelper.launchCompilation(
	 * "/comments/failed/comment_block_not_opened.moj",
	 * testFolder.newFile(),
	 * false,
	 * "Comment block not opened - KO test that should fail");
	 * }
	 */

	/*
	 * Author: Sacha Pauwels
	 *
	 * The test is not compatible with the testing framework
	 * 
	 * @Test
	 * public void comment_line_undeclared() throws Exception {
	 * CompilerTestHelper.launchCompilation(
	 * "/comments/failed/comment_line_undeclared.moj",
	 * testFolder.newFile(),
	 * false,
	 * "Comment line undeclared - KO test that should fail");
	 * }
	 */
}
