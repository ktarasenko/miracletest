package com.ktarasenko.miracletest.test;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import com.ktarasenko.miracletest.test.model.ModelTest;
import junit.framework.TestSuite;


public class TestRunner extends InstrumentationTestRunner {

        @Override
        public TestSuite getAllTests() {
            InstrumentationTestSuite suite = new InstrumentationTestSuite(this);

            suite.addTestSuite(ModelTest.class);

            return suite;
        }

        @Override
        public ClassLoader getLoader() {
            return TestRunner.class.getClassLoader();
        }
}
