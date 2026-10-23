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

public class MapTest {

    private static final Logger LOG = LoggerFactory.getLogger(MapTest.class);

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
    public void map_5x5_complete() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/map_5x5_complete.map",
                testFolder.newFile(),
                true,
                "5x5 complete map - OK test that should pass");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_5x5_down() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/map_5x5_down.map",
                testFolder.newFile(),
                true,
                "5x5 map with down direction - OK test that should pass");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_5x5_left() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/map_5x5_left.map",
                testFolder.newFile(),
                true,
                "5x5 map with left direction - OK test that should pass");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_5x5_right() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/map_5x5_right.map",
                testFolder.newFile(),
                true,
                "5x5 map with right direction - OK test that should pass");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_5x5_up() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/map_5x5_up.map",
                testFolder.newFile(),
                true,
                "5x5 map with up direction - OK test that should pass");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_with_comments() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/map_with_comments.map",
                testFolder.newFile(),
                true,
                "Map with comments - OK test that should pass");
    }

    /*
     * Author: Charles Eykerman
     */
    @Test
    public void test_charles_all_map_elements_ok() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/test_charles_all_map_elements_ok.moj",
                testFolder.newFile(),
                true,
                "All map elements - OK test that should pass");
    }

    /*
     * Author: Charles Eykerman
     */
    @Test
    public void test_charles_simple_map_ok() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/passed/test_charles_simple_map_ok.moj",
                testFolder.newFile(),
                true,
                "Simple map - OK test that should pass");
    }

    // ========================================================================
    // TEST FAILED
    // ========================================================================

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_empty_body() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_empty_body.map",
                testFolder.newFile(),
                false,
                "Map with empty body - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_invalid_element() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_invalid_element.map",
                testFolder.newFile(),
                false,
                "Map with invalid element - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_missing_semicolon() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_missing_semicolon.map",
                testFolder.newFile(),
                false,
                "Map with missing semicolon - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_no_column() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_no_column.map",
                testFolder.newFile(),
                false,
                "Map with no column - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_no_direction() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_no_direction.map",
                testFolder.newFile(),
                false,
                "Map with no direction - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_no_header() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_no_header.map",
                testFolder.newFile(),
                false,
                "Map with no header - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_no_row() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_no_row.map",
                testFolder.newFile(),
                false,
                "Map with no row - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_wrong_header() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_wrong_header.map",
                testFolder.newFile(),
                false,
                "Map with wrong header - KO test that should fail");
    }

    /*
     * Author: Sacha Pauwels
     */
    @Test
    public void map_wrong_direction() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/map_wrong_direction.map",
                testFolder.newFile(),
                false,
                "Map with wrong direction - KO test that should fail");
    }

    /*
     * Author: Charles Eykerman
     */
    @Test
    public void test_charles_all_with_unknown_elements_ko() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/test_charles_all_with_unknown_elements_ko.map",
                testFolder.newFile(),
                false,
                "Map with unknown elements - KO test that should fail");
    }

    /*
     * Author: Charles Eykerman
     */
    @Test
    public void test_charles_simple_map_ko() throws Exception {
        CompilerTestHelper.launchCompilation(
				"/syntax/map/failed/test_charles_simple_map_ko.map",
                testFolder.newFile(),
                false,
                "Simple map - KO test that should fail");
    }
}