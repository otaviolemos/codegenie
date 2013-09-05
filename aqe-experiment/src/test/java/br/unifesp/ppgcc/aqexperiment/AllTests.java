package br.unifesp.ppgcc.aqexperiment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.unifesp.ppgcc.aqexperiment.application.AQEServiceTest;
import br.unifesp.ppgcc.aqexperiment.infrastructure.sourcereraqe.SourcererQueryBuilderTest;

@RunWith(Suite.class)
@SuiteClasses({ SourcererQueryBuilderTest.class, AQEServiceTest.class })
public class AllTests {

} 