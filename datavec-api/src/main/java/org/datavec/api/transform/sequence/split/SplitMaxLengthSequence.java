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

package org.datavec.api.transform.sequence.split;

import org.nd4j.shade.jackson.annotation.JsonIgnoreProperties;
import org.nd4j.shade.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.sequence.SequenceSplit;
import org.datavec.api.writable.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Split a sequence into a number of smaller sequences of length 'maxSequenceLength'.
 * If the sequence length is smaller than maxSequenceLength, the sequence is unchanged
 * Created by Alex on 16/03/2016.
 */
@EqualsAndHashCode(exclude = {"inputSchema"})
@JsonIgnoreProperties({"inputSchema"})
public class SplitMaxLengthSequence implements SequenceSplit {

    private final int maxSequenceLength;
    private final boolean equalSplits;
    private Schema inputSchema;

    /**
     * @param maxSequenceLength max length of sequences
     * @param equalSplits       if true: split larger sequences into equal sized subsequences. If false: split into
     *                          n maxSequenceLength sequences, and (if necessary) 1 with 1 <= length < maxSequenceLength
     */
    public SplitMaxLengthSequence(@JsonProperty("maxSequenceLength") int maxSequenceLength,
                    @JsonProperty("equalSplits") boolean equalSplits) {
        this.maxSequenceLength = maxSequenceLength;
        this.equalSplits = equalSplits;
    }

    public List<List<List<Writable>>> split(List<List<Writable>> sequence) {
        int n = sequence.size();
        if (n <= maxSequenceLength)
            return Collections.singletonList(sequence);
        int splitSize;
        if (equalSplits) {
            if (n % maxSequenceLength == 0) {
                splitSize = n / maxSequenceLength;
            } else {
                splitSize = n / maxSequenceLength + 1;
            }
        } else {
            splitSize = maxSequenceLength;
        }

        List<List<List<Writable>>> out = new ArrayList<>();
        List<List<Writable>> current = new ArrayList<>(splitSize);
        for (List<Writable> step : sequence) {
            if (current.size() >= splitSize) {
                out.add(current);
                current = new ArrayList<>(splitSize);
            }
            current.add(step);
        }
        out.add(current);

        return out;
    }

    @Override
    public void setInputSchema(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    @Override
    public Schema getInputSchema() {
        return inputSchema;
    }

    @Override
    public String toString() {
        return "SplitMaxLengthSequence(maxSequenceLength=" + maxSequenceLength + ",equalSplits=" + equalSplits + ")";
    }
}
