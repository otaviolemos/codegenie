package br.unifesp.ppgcc.aqexperiment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.unifesp.ppgcc.aqexperiment.application.AQEServiceTest;
import br.unifesp.ppgcc.aqexperiment.infrastructure.sourcereraqe.SourcererQueryBuilderTest;
import br.unifesp.ppgcc.aqexperiment.infrastructure.util.TagCloudHelperTest;

@RunWith(Suite.class)
@SuiteClasses({ SourcererQueryBuilderTest.class, AQEServiceTest.class, TagCloudHelperTest.class })
public class AllTests {

} 