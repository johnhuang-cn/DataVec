/*-
 *  * Copyright 2016 Skymind, Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 */

package org.datavec.spark.transform.analysis.aggregate;

import org.datavec.api.transform.ColumnType;
import org.datavec.api.transform.schema.Schema;
import org.datavec.spark.transform.analysis.AnalysisCounter;
import org.datavec.spark.transform.analysis.columns.CategoricalAnalysisCounter;
import org.datavec.spark.transform.analysis.columns.DoubleAnalysisCounter;
import org.datavec.spark.transform.analysis.columns.IntegerAnalysisCounter;
import org.datavec.spark.transform.analysis.columns.LongAnalysisCounter;
import org.datavec.spark.transform.analysis.string.StringAnalysisCounter;
import org.datavec.spark.transform.analysis.columns.BytesAnalysisCounter;
import lombok.AllArgsConstructor;
import org.apache.spark.api.java.function.Function2;
import org.datavec.api.writable.Writable;

import java.util.ArrayList;
import java.util.List;

/**
 * Add function used for undertaking analysis of a data set via Spark
 *
 * @author Alex Black
 */
@AllArgsConstructor
public class AnalysisAddFunction implements Function2<List<AnalysisCounter>, List<Writable>, List<AnalysisCounter>> {

    private Schema schema;

    @Override
    public List<AnalysisCounter> call(List<AnalysisCounter> analysisCounters, List<Writable> writables)
                    throws Exception {
        if (analysisCounters == null) {
            analysisCounters = new ArrayList<>();
            List<ColumnType> columnTypes = schema.getColumnTypes();
            for (ColumnType ct : columnTypes) {
                switch (ct) {
                    case String:
                        analysisCounters.add(new StringAnalysisCounter());
                        break;
                    case Integer:
                        analysisCounters.add(new IntegerAnalysisCounter());
                        break;
                    case Long:
                        analysisCounters.add(new LongAnalysisCounter());
                        break;
                    case Double:
                        analysisCounters.add(new DoubleAnalysisCounter());
                        break;
                    case Categorical:
                        analysisCounters.add(new CategoricalAnalysisCounter());
                        break;
                    case Time:
                        analysisCounters.add(new LongAnalysisCounter());
                        break;
                    case Bytes:
                        analysisCounters.add(new BytesAnalysisCounter());
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown column type: " + ct);
                }
            }
        }

        int size = analysisCounters.size();
        if (size != writables.size())
            throw new IllegalStateException("Writables list and number of counters does not match (" + writables.size()
                            + " vs " + size + ")");
        for (int i = 0; i < size; i++) {
            analysisCounters.get(i).add(writables.get(i));
        }

        return analysisCounters;
    }
}
