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

package org.datavec.api.transform.condition.column;

import lombok.Data;
import org.datavec.api.transform.schema.Schema;
import org.nd4j.shade.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import org.datavec.api.transform.condition.SequenceConditionMode;
import org.datavec.api.writable.Writable;
import org.datavec.api.transform.condition.ConditionOp;

import java.util.Set;

/**
 * Condition that applies to the values in a Long column, using a {@link ConditionOp}
 *
 * @author Alex Black
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LongColumnCondition extends BaseColumnCondition {

    private final ConditionOp op;
    private final Long value;
    private final Set<Long> set;

    /**
     * Constructor for operations such as less than, equal to, greater than, etc.
     * Uses default sequence condition mode, {@link BaseColumnCondition#DEFAULT_SEQUENCE_CONDITION_MODE}
     *
     * @param columnName Column to check for the condition
     * @param op         Operation (<, >=, !=, etc)
     * @param value      Value to use in the condition
     */
    public LongColumnCondition(String columnName, ConditionOp op, long value) {
        this(columnName, DEFAULT_SEQUENCE_CONDITION_MODE, op, value);
    }

    /**
     * Constructor for operations such as less than, equal to, greater than, etc.
     *
     * @param column                Column to check for the condition
     * @param sequenceConditionMode Mode for handling sequence data
     * @param op                    Operation (<, >=, !=, etc)
     * @param value                 Value to use in the condition
     */
    public LongColumnCondition(String column, SequenceConditionMode sequenceConditionMode, ConditionOp op, long value) {
        super(column, sequenceConditionMode);
        if (op == ConditionOp.InSet || op == ConditionOp.NotInSet) {
            throw new IllegalArgumentException(
                            "Invalid condition op: cannot use this constructor with InSet or NotInSet ops");
        }
        this.op = op;
        this.value = value;
        this.set = null;
    }

    /**
     * Constructor for operations: ConditionOp.InSet, ConditionOp.NotInSet
     * Uses default sequence condition mode, {@link BaseColumnCondition#DEFAULT_SEQUENCE_CONDITION_MODE}
     *
     * @param column Column to check for the condition
     * @param op     Operation. Must be either ConditionOp.InSet, ConditionOp.NotInSet
     * @param set    Set to use in the condition
     */
    public LongColumnCondition(String column, ConditionOp op, Set<Long> set) {
        this(column, DEFAULT_SEQUENCE_CONDITION_MODE, op, set);
    }

    /**
     * Constructor for operations: ConditionOp.InSet, ConditionOp.NotInSet
     *
     * @param column                Column to check for the condition
     * @param sequenceConditionMode Mode for handling sequence data
     * @param op                    Operation. Must be either ConditionOp.InSet, ConditionOp.NotInSet
     * @param set                   Set to use in the condition
     */
    public LongColumnCondition(String column, SequenceConditionMode sequenceConditionMode, ConditionOp op,
                    Set<Long> set) {
        super(column, sequenceConditionMode);
        if (op != ConditionOp.InSet && op != ConditionOp.NotInSet) {
            throw new IllegalArgumentException(
                            "Invalid condition op: can ONLY use this constructor with InSet or NotInSet ops");
        }
        this.op = op;
        this.value = null;
        this.set = set;
    }

    //Private constructor for Jackson deserialization only
    private LongColumnCondition(@JsonProperty("columnName") String columnName, @JsonProperty("op") ConditionOp op,
                    @JsonProperty("value") long value, @JsonProperty("set") Set<Long> set) {
        super(columnName, DEFAULT_SEQUENCE_CONDITION_MODE);
        this.op = op;
        this.value = (set == null ? value : null);
        this.set = set;
    }

    @Override
    public boolean columnCondition(Writable writable) {
        switch (op) {
            case LessThan:
                return writable.toLong() < value;
            case LessOrEqual:
                return writable.toLong() <= value;
            case GreaterThan:
                return writable.toLong() > value;
            case GreaterOrEqual:
                return writable.toLong() >= value;
            case Equal:
                return writable.toLong() == value;
            case NotEqual:
                return writable.toLong() != value;
            case InSet:
                return set.contains(writable.toLong());
            case NotInSet:
                return !set.contains(writable.toLong());
            default:
                throw new RuntimeException("Unknown or not implemented op: " + op);
        }
    }

    @Override
    public String toString() {
        return "LongColumnCondition(columnName=\"" + columnName + "\"," + op + ","
                        + (op == ConditionOp.NotInSet || op == ConditionOp.InSet ? set : value) + ")";
    }

    /**
     * Condition on arbitrary input
     *
     * @param input the input to return
     *              the condition for
     * @return true if the condition is met
     * false otherwise
     */
    @Override
    public boolean condition(Object input) {
        Number n = (Number) input;
        switch (op) {
            case LessThan:
                return n.longValue() < value;
            case LessOrEqual:
                return n.longValue() <= value;
            case GreaterThan:
                return n.longValue() > value;
            case GreaterOrEqual:
                return n.longValue() >= value;
            case Equal:
                return n.longValue() == value;
            case NotEqual:
                return n.longValue() != value;
            case InSet:
                return set.contains(n.longValue());
            case NotInSet:
                return !set.contains(n.longValue());
            default:
                throw new RuntimeException("Unknown or not implemented op: " + op);
        }
    }


}
