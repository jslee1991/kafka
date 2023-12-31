/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.cluster.operator.resource.cruisecontrol;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PathBuilderTest {

    private static final String DEFAULT_QUERY = "?" +
            CruiseControlParameters.JSON.key + "=true" + "&" +
            CruiseControlParameters.DRY_RUN.key + "=true" + "&" +
            CruiseControlParameters.VERBOSE.key + "=false";

    private static final List<String> GOALS = Arrays.asList("goal.one", "goal.two", "goal.three", "goal.four", "goal.five");

    private String getExpectedRebalanceString() throws UnsupportedEncodingException {

        StringBuilder expectedQuery = new StringBuilder(
                CruiseControlEndpoints.REBALANCE.path + "?" +
                        CruiseControlParameters.JSON.key + "=true&" +
                        CruiseControlParameters.DRY_RUN.key + "=false&" +
                        CruiseControlParameters.VERBOSE.key + "=true&" +
                        CruiseControlParameters.SKIP_HARD_GOAL_CHECK.key + "=false&" +
                        CruiseControlParameters.EXCLUDED_TOPICS.key + "=test-.*&" +
                        CruiseControlParameters.GOALS.key + "=");

        StringBuilder goalStringBuilder = new StringBuilder();
        for (int i = 0; i < GOALS.size(); i++) {
            goalStringBuilder.append(GOALS.get(i));
            if (i < GOALS.size() - 1) {
                goalStringBuilder.append(",");
            }
        }

        expectedQuery.append(URLEncoder.encode(goalStringBuilder.toString(), StandardCharsets.UTF_8.toString()) + "&");
        expectedQuery.append(CruiseControlParameters.REBALANCE_DISK.key + "=false");

        return expectedQuery.toString();
    }
    @Test
    public void testQueryStringPair() {

        String path = new PathBuilder(CruiseControlEndpoints.STATE)
                .withParameter(CruiseControlParameters.JSON, "true")
                .withParameter(CruiseControlParameters.DRY_RUN, "true")
                .withParameter(CruiseControlParameters.VERBOSE, "false")
                .withParameter(CruiseControlParameters.REBALANCE_DISK, "false")
                .build();

        assertThat(path, containsString(DEFAULT_QUERY));

    }

    @Test
    public void testQueryStringList() throws UnsupportedEncodingException {

        String path = new PathBuilder(CruiseControlEndpoints.REBALANCE)
                .withParameter(CruiseControlParameters.JSON, "true")
                .withParameter(CruiseControlParameters.DRY_RUN, "false")
                .withParameter(CruiseControlParameters.VERBOSE, "true")
                .withParameter(CruiseControlParameters.SKIP_HARD_GOAL_CHECK, "false")
                .withParameter(CruiseControlParameters.EXCLUDED_TOPICS, "test-.*")
                .withParameter(CruiseControlParameters.GOALS, GOALS)
                .withParameter(CruiseControlParameters.REBALANCE_DISK, "false")
                .build();


        assertThat(path, is(getExpectedRebalanceString()));

    }

    @Test
    public void testQueryRebalanceOptions() throws UnsupportedEncodingException {

        RebalanceOptions options = new RebalanceOptions.RebalanceOptionsBuilder()
                .withVerboseResponse()
                .withFullRun()
                .withExcludedTopics("test-.*")
                .withGoals(GOALS)
                .build();

        String path = new PathBuilder(CruiseControlEndpoints.REBALANCE)
                .withParameter(CruiseControlParameters.JSON, "true")
                .withRebalanceParameters(options).build();

        assertThat(path, is(getExpectedRebalanceString()));
    }

}
