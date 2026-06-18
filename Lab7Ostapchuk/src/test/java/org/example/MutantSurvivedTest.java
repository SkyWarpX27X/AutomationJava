package org.example;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(MoneyTransferTest.class)
@ExcludeTags("killed")
public class MutantSurvivedTest {
}
