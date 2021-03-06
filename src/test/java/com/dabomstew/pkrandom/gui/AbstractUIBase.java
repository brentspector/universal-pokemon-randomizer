package com.dabomstew.pkrandom.gui;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JCheckBoxFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JRadioButtonFixture;
import org.assertj.swing.fixture.JSliderFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.testing.AssertJSwingTestCaseTemplate;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

public class AbstractUIBase extends AssertJSwingTestCaseTemplate {
   /**
 * Base class for all my UI tests taking care of the basic setup.
 */

    /**
     * The main entry point for any tests: the wrapped MainWindow.
     */
    protected FrameFixture frame;
    protected RandomizerGUI mainWindow;

    /**
     * Installs a {@link FailOnThreadViolationRepaintManager} to catch violations of Swing threading rules.
     */
    @BeforeClass
    public static final void setUpOnce() {
        // avoid UI test execution in a headless environment (e.g. when building in CI environment like Jenkins or TravisCI)
        Assume.assumeFalse("Automated UI Test cannot be executed in headless environment", GraphicsEnvironment.isHeadless());
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        } catch (java.awt.AWTError | NoClassDefFoundError exc) {
            Assume.assumeTrue("Automated UI Test cannot reach the X11 DISPLAY variable set. " +
                "Unable to verify functionality. Passing tests.", false);
        }
        FailOnThreadViolationRepaintManager.install();
    }

    /**
     * Sets up this test's fixture, starting from creation of a new <code>{@link Robot}</code>.
     *
     * @see #setUpRobot()
     * @see #onSetUp()
     */
    @Before
    public final void setUp() {
        // call provided AssertJSwingTestCaseTemplate.setUpRobot()
        this.setUpRobot();
        // initialize the graphical user interface
        mainWindow = GuiActionRunner.execute(new GuiQuery<RandomizerGUI>() {

            @Override
            protected RandomizerGUI executeInEDT() throws Exception {
                return new RandomizerGUI(false, false, true);
            }
        });
        this.frame = new FrameFixture(this.robot(), mainWindow);
        this.frame.show();
        this.frame.moveTo(new Point(10, 10));
        onSetUp();
    }

    /**
     * Subclasses that need to set up their own test fixtures in this method. Called as <strong>last action</strong> during {@link #setUp()}.
     */
    protected void onSetUp() {
        // default: everything is already set up
    }

    /*****************************************************************************************
     * Here you could insert further helper methods, e.g. frequently used component matchers *
     *****************************************************************************************/
    /**
     * Returns a JCheckBoxFixture where the JCheckBox has the requested component name
     * @param name - Name of the JCheckBox (different than variable name)
     * @return JCheckBoxFixture with the given name
     */
     public JCheckBoxFixture getCheckBoxByName(String name) {
        JCheckBoxFixture jCheckBoxFixture = this.frame.checkBox(new GenericTypeMatcher(JCheckBox.class, true) {
            @Override
            protected boolean isMatching(Component component) {
                if (component.getName() != null && component.getName().equals(name)) {
                    return true;
                }
                return false;
            }
        });
        return jCheckBoxFixture;
    }

    /**
     * Returns a JRadioButtonFixture where the JRadioButton has the requested component name
     * @param name - Name of the JRadioButton (different than variable name)
     * @return JRadioButtonFixture with the given name
     */
    public JRadioButtonFixture getRadioButtonByName(String name) {
        JRadioButtonFixture jRadioButtonFixture = this.frame.radioButton(new GenericTypeMatcher(JRadioButton.class, true) {
            @Override
            protected boolean isMatching(Component component) {
                if (component.getName() != null && component.getName().equals(name)) {
                    return true;
                }
                return false;
            }
        });
        return jRadioButtonFixture;
    }

    /**
     * Returns a JSliderFixture where the JSlider has the requested component name
     * @param name - Name of the JSlider (different than variable name)
     * @return JSlider with the given name
     */
    public JSliderFixture getSliderByName(String name) {
        JSliderFixture jSliderFixture = this.frame.slider(new GenericTypeMatcher(JSlider.class, true) {
            @Override
            protected boolean isMatching(Component component) {
                if (component.getName() != null && component.getName().equals(name)) {
                    return true;
                }
                return false;
            }
        });
        return jSliderFixture;
    }

    /**
     * Returns a JButtonFixture where the JButton has the requested component name
     * @param name - Name of the JButton (different than variable name)
     * @return JButtonFixture with the given name
     */
    public JButtonFixture getButtonByName(String name) {
        JButtonFixture jButtonFixture = this.frame.button(new GenericTypeMatcher(JButton.class, true) {
            @Override
            protected boolean isMatching(Component component) {
                if (component.getName() != null && component.getName().equals(name)) {
                    return true;
                }
                return false;
            }
        });
        return jButtonFixture;
    }

    /**
     * Returns a JListFixture where the JList has the requested component name
     * @param name - Name of the JList (different than variable name)
     * @return JListFixture with the given name
     */
    public JListFixture getListByName(String name) {
        JListFixture jListFixture = this.frame.list(new GenericTypeMatcher(JList.class, true) {
            @Override
            protected boolean isMatching(Component component) {
                if (component.getName() != null && component.getName().equals(name)) {
                    return true;
                }
                return false;
            }
        });
        return jListFixture;
    }

/**
     * Returns a DialogFixture where the JDialog has the requested component class
     * @param clazz - Class type of the JDialog
     * @return DialogFixture with the given name
     */
    public DialogFixture getDialogByClass(Class<?> clazz) {
        DialogFixture dialogFixture = this.frame.dialog(new GenericTypeMatcher(JDialog.class, true) {
            @Override
            protected boolean isMatching(Component component) {
                if (component.getClass().equals(clazz)) {
                    return true;
                }
                return false;
            }
        });
        return dialogFixture;
    }

    /**
     * Cleans up any resources used in this test. After calling <code>{@link #onTearDown()}</code>, this method cleans up resources used by this
     * test's <code>{@link Robot}</code>.
     *
     * @see #cleanUp()
     * @see #onTearDown()
     */
    @After
    public final void tearDown() {
        try {
            onTearDown();
            this.frame = null;
        } finally {
            cleanUp();
        }
    }

    /**
     * Subclasses that need to clean up resources can do so in this method. Called as <strong>first action</strong> during {@link #tearDown()}.
     */
    protected void onTearDown() {
        // default: nothing more to tear down
    }
}
