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

package org.datavec.api.transform.join;

import org.datavec.api.writable.*;
import org.datavec.api.transform.ColumnType;
import org.datavec.api.transform.schema.Schema;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Alex on 18/04/2016.
 */
public class TestJoin {

    @Test
    public void testJoin() {

        Schema firstSchema =
                        new Schema.Builder().addColumnString("keyColumn").addColumnsInteger("first0", "first1").build();

        Schema secondSchema = new Schema.Builder().addColumnString("keyColumn").addColumnsInteger("second0").build();

        List<List<Writable>> first = new ArrayList<>();
        first.add(Arrays.asList((Writable) new Text("key0"), new IntWritable(0), new IntWritable(1)));
        first.add(Arrays.asList((Writable) new Text("key1"), new IntWritable(10), new IntWritable(11)));

        List<List<Writable>> second = new ArrayList<>();
        second.add(Arrays.asList((Writable) new Text("key0"), new IntWritable(100)));
        second.add(Arrays.asList((Writable) new Text("key1"), new IntWritable(110)));

        Join join = new Join.Builder(Join.JoinType.Inner).setJoinColumns("keyColumn")
                        .setSchemas(firstSchema, secondSchema).build();

        List<List<Writable>> expected = new ArrayList<>();
        expected.add(Arrays.asList((Writable) new Text("key0"), new IntWritable(0), new IntWritable(1),
                        new IntWritable(100)));
        expected.add(Arrays.asList((Writable) new Text("key1"), new IntWritable(10), new IntWritable(11),
                        new IntWritable(110)));


        //Check schema:
        Schema joinedSchema = join.getOutputSchema();
        assertEquals(4, joinedSchema.numColumns());
        assertEquals(Arrays.asList("keyColumn", "first0", "first1", "second0"), joinedSchema.getColumnNames());
        assertEquals(Arrays.asList(ColumnType.String, ColumnType.Integer, ColumnType.Integer, ColumnType.Integer),
                        joinedSchema.getColumnTypes());


        //Check joining with null values:
        expected = new ArrayList<>();
        expected.add(Arrays.asList((Writable) new Text("key0"), new IntWritable(0), new IntWritable(1),
                        NullWritable.INSTANCE));
        expected.add(Arrays.asList((Writable) new Text("key1"), new IntWritable(10), new IntWritable(11),
                        NullWritable.INSTANCE));
        for (int i = 0; i < first.size(); i++) {
            List<Writable> out = join.joinExamples(first.get(i), null);
            assertEquals(expected.get(i), out);
        }

        expected = new ArrayList<>();
        expected.add(Arrays.asList((Writable) new Text("key0"), NullWritable.INSTANCE, NullWritable.INSTANCE,
                        new IntWritable(100)));
        expected.add(Arrays.asList((Writable) new Text("key1"), NullWritable.INSTANCE, NullWritable.INSTANCE,
                        new IntWritable(110)));
        for (int i = 0; i < first.size(); i++) {
            List<Writable> out = join.joinExamples(null, second.get(i));
            assertEquals(expected.get(i), out);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void testJoinValidation() {

        Schema firstSchema = new Schema.Builder().addColumnString("keyColumn1").addColumnsInteger("first0", "first1")
                        .build();

        Schema secondSchema = new Schema.Builder().addColumnString("keyColumn2").addColumnsInteger("second0").build();

        new Join.Builder(Join.JoinType.Inner).setJoinColumns("keyColumn1", "thisDoesntExist")
                        .setSchemas(firstSchema, secondSchema).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinValidation2() {

        Schema firstSchema = new Schema.Builder().addColumnString("keyColumn1").addColumnsInteger("first0", "first1")
                        .build();

        Schema secondSchema = new Schema.Builder().addColumnString("keyColumn2").addColumnsInteger("second0").build();

        new Join.Builder(Join.JoinType.Inner).setJoinColumns("keyColumn1").setSchemas(firstSchema, secondSchema)
                        .build();
    }
}
