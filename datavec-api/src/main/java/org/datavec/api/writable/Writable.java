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

package org.datavec.api.writable;

import org.nd4j.shade.jackson.annotation.JsonSubTypes;
import org.nd4j.shade.jackson.annotation.JsonTypeInfo;

import java.io.DataOutput;
import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;

/**
 * A serializable object which implements a simple, efficient, serialization 
 * protocol, based on {@link DataInput} and {@link DataOutput}.
 *
 * <p>Any <code>key</code> or <code>value</code> type in the Hadoop Map-Reduce
 * framework implements this interface.</p>
 *
 * <p>Implementations typically implement a static <code>read(DataInput)</code>
 * method which constructs a new instance, calls {@link #readFields(DataInput)}
 * and returns the instance.</p>
 *
 * <p>Example:</p>
 * <p><blockquote><pre>
 *     public class MyWritable implements Writable {
 *       // Some data     
 *       private int counter;
 *       private long timestamp;
 *
 *       public void write(DataOutput out) throws IOException {
 *         out.writeInt(counter);
 *         out.writeLong(timestamp);
 *       }
 *
 *       public void readFields(DataInput in) throws IOException {
 *         counter = in.readInt();
 *         timestamp = in.readLong();
 *       }
 *
 *       public static MyWritable read(DataInput in) throws IOException {
 *         MyWritable w = new MyWritable();
 *         w.readFields(in);
 *         return w;
 *       }
 *     }
 * </pre></blockquote></p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {@JsonSubTypes.Type(value = ArrayWritable.class, name = "ArrayWritable"),
                @JsonSubTypes.Type(value = BooleanWritable.class, name = "BooleanWritable"),
                @JsonSubTypes.Type(value = ByteWritable.class, name = "ByteWritable"),
                @JsonSubTypes.Type(value = DoubleWritable.class, name = "DoubleWritable"),
                @JsonSubTypes.Type(value = FloatWritable.class, name = "FloatWritable"),
                @JsonSubTypes.Type(value = IntWritable.class, name = "IntWritable"),
                @JsonSubTypes.Type(value = LongWritable.class, name = "LongWritable"),
                @JsonSubTypes.Type(value = NullWritable.class, name = "NullWritable"),
                @JsonSubTypes.Type(value = Text.class, name = "Text"),})
public interface Writable extends Serializable {
    /**
     * Serialize the fields of this object to <code>out</code>.
     *
     * @param out <code>DataOuput</code> to serialize this object into.
     * @throws IOException
     */
    void write(DataOutput out) throws IOException;

    /**
     * Deserialize the fields of this object from <code>in</code>.
     *
     * <p>For efficiency, implementations should attempt to re-use storage in the
     * existing object where possible.</p>
     *
     * @param in <code>DataInput</code> to deseriablize this object from.
     * @throws IOException
     */
    void readFields(DataInput in) throws IOException;

    /** Convert Writable to double. Whether this is supported depends on the specific writable. */
    double toDouble();

    /** Convert Writable to float. Whether this is supported depends on the specific writable. */
    float toFloat();

    /** Convert Writable to int. Whether this is supported depends on the specific writable. */
    int toInt();

    /** Convert Writable to long. Whether this is supported depends on the specific writable. */
    long toLong();


}
