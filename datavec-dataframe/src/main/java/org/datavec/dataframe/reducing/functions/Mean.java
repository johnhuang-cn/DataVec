package org.datavec.dataframe.reducing.functions;

import org.datavec.dataframe.api.Table;
import org.datavec.dataframe.reducing.NumericReduceFunction;
import org.datavec.dataframe.reducing.NumericReduceUtils;

/**
 *
 */
public class Mean extends SummaryFunction {

    public Mean(Table original, String summarizedColumnName) {
        super(original, summarizedColumnName);
    }

    @Override
    public NumericReduceFunction function() {
        return NumericReduceUtils.mean;
    }
}
