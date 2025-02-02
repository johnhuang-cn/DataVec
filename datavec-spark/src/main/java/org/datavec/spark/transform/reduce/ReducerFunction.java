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

package org.datavec.spark.transform.reduce;

import lombok.AllArgsConstructor;
import org.apache.spark.api.java.function.Function;
import org.datavec.api.writable.Writable;
import org.datavec.api.transform.reduce.IReducer;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Spark function for executing
 * a reduction of a set of examples by key
 *
 * @author Alex Black
 */
@AllArgsConstructor
public class ReducerFunction implements Function<Tuple2<String, Iterable<List<Writable>>>, List<Writable>> {

    private final IReducer reducer;

    @Override
    public List<Writable> call(Tuple2<String, Iterable<List<Writable>>> t2) throws Exception {
        List<List<Writable>> list = new ArrayList<>();
        for (List<Writable> c : t2._2()) {
            list.add(c);
        }
        return reducer.reduce(list);
    }
}
