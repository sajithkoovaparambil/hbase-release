/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.mob;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.classification.InterfaceAudience;
import org.apache.hadoop.hbase.classification.InterfaceStability;
import org.apache.hadoop.hbase.util.AbstractHBaseTool;
import org.apache.hadoop.hbase.util.LogUtils;
import org.apache.hadoop.util.ToolRunner;

@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MobStressTool extends AbstractHBaseTool
{
  private static final Log LOG = LogFactory.getLog(MobStressTool.class);
  private CommandLine cmd;

  public MobStressTool() throws IOException {
    init();
  }

  protected void init() throws IOException {
    // define supported options

    addOptWithArg("n", "Number of MOB key-values to insert, default - 10000000");
    // disable irrelevant loggers to avoid it mess up command output
    LogUtils.disableUselessLoggers(LOG);
  }

  @Override
  protected void addOptions() {
  }

  @Override
  protected void processOptions(CommandLine cmd) {
    this.cmd = cmd;
  }

  @Override
  protected int doWork() throws Exception {
    long numRowsToInsert = 10000000;
    if (cmd.hasOption("n")) {
      numRowsToInsert = Long.parseLong(cmd.getOptionValue("n"));
      if (numRowsToInsert < 0) {
        LOG.warn("Ignore wrong option '-n'");
        numRowsToInsert = 10000000;
      }
    }

    TestMobCompaction test = new TestMobCompaction(getConf(), numRowsToInsert);
    test.testMobCompaction();
    return 0;
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = HBaseConfiguration.create();
    int ret = ToolRunner.run(conf, new MobStressTool(), args);
    System.exit(ret);
  }
}
