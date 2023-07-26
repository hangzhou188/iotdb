#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

if [ -z "${IOTDB_HOME}" ]; then
  export IOTDB_HOME="`dirname "$0"`/.."
fi


force=""

while true; do
    case "$1" in
        -f)
            force="yes"
            break
        ;;
        "")
            #if we do not use getopt, we then have to process the case that there is no argument.
            #in some systems, when there is no argument, shift command may throw error, so we skip directly
            #all others are args to the program
            PARAMS=$*
            break
        ;;
    esac
done

if [ -f "$IOTDB_HOME/sbin/stop-confignode.sh" ]; then
  export CONFIGNODE_STOP_PATH="$IOTDB_HOME/sbin/stop-confignode.sh"
else
  echo "Can't find stop-confignode.sh"
  exit 0
fi

if [ -f "$IOTDB_HOME/sbin/stop-datanode.sh" ]; then
  export DATANODE_STOP_PATH="$IOTDB_HOME/sbin/stop-datanode.sh"
else
  echo "Can't find stop-datanode.sh"
  exit 0
fi



if [[ "${force}" == "yes" ]]; then
  echo "Force stop all nodes"
  bash "$CONFIGNODE_STOP_PATH" -f
  bash "$DATANODE_STOP_PATH" -f
else
  bash "$CONFIGNODE_STOP_PATH"
  bash "$DATANODE_STOP_PATH"
fi
