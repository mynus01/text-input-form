package com.mynus01.textinputform.cucumber

import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    glue = ["com.mynus01.textinputform.cucumber.steps"],
    features = ["features"]
)
class RunnerCucumberTest : CucumberAndroidJUnitRunner()