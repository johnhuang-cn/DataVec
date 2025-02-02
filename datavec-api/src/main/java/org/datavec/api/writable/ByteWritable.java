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


import org.nd4j.shade.jackson.annotation.JsonProperty;
import org.datavec.api.io.WritableComparable;
import org.datavec.api.io.WritableComparator;

import java.io.*;

/** A WritableComparable for a single byte. */
public class ByteWritable implements WritableComparable {
    private byte value;

    public ByteWritable() {}

    public ByteWritable(@JsonProperty("value") byte value) {
        set(value);
    }

    /** Set the value of this ByteWritable. */
    public void set(byte value) {
        this.value = value;
    }

    /** Return the value of this ByteWritable. */
    public byte get() {
        return value;
    }

    public void readFields(DataInput in) throws IOException {
        value = in.readByte();
    }

    public void write(DataOutput out) throws IOException {
        out.writeByte(value);
    }

    /** Returns true iff <code>o</code> is a ByteWritable with the same value. */
    public boolean equals(Object o) {
        if (!(o instanceof ByteWritable)) {
            return false;
        }
        ByteWritable other = (ByteWritable) o;
        return this.value == other.value;
    }

    public int hashCode() {
        return (int) value;
    }

    /** Compares two ByteWritables. */
    public int compareTo(Object o) {
        int thisValue = this.value;
        int thatValue = ((ByteWritable) o).value;
        return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
    }

    public String toString() {
        return Byte.toString(value);
    }

    /** A Comparator optimized for ByteWritable. */
    public static class Comparator extends WritableComparator {
        public Comparator() {
            super(ByteWritable.class);
        }

        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            byte thisValue = b1[s1];
            byte thatValue = b2[s2];
            return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
        }
    }

    static { // register this comparator
        WritableComparator.define(ByteWritable.class, new Comparator());
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public float toFloat() {
        return value;
    }

    @Override
    public int toInt() {
        return value;
    }

    @Override
    public long toLong() {
        return value;
    }
}
