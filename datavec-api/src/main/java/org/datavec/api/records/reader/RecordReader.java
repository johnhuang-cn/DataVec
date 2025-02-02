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

package org.datavec.api.records.reader;

import org.datavec.api.conf.Configurable;
import org.datavec.api.records.Record;
import org.datavec.api.records.listener.RecordListener;
import org.datavec.api.records.metadata.RecordMetaData;
import org.datavec.api.split.InputSplit;
import org.datavec.api.writable.Writable;
import org.datavec.api.conf.Configuration;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Record reader
 *
 * @author Adam Gibson
 */
public interface RecordReader extends Closeable, Serializable, Configurable {

    public final static String NAME_SPACE = RecordReader.class.getName();

    public final static String APPEND_LABEL = NAME_SPACE + ".appendlabel";
    public final static String LABELS = NAME_SPACE + ".labels";

    /**
     * Called once at initialization.
     *
     * @param split the split that defines the range of records to read
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    void initialize(InputSplit split) throws IOException, InterruptedException;

    /**
     * Called once at initialization.
     *
     * @param conf  a configuration for initialization
     * @param split the split that defines the range of records to read
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    void initialize(Configuration conf, InputSplit split) throws IOException, InterruptedException;

    /**
     * Get the next record
     *
     * @return
     */
    List<Writable> next();


    /**
     * Whether there are anymore records
     *
     * @return
     */
    boolean hasNext();

    /**
     * List of label strings
     *
     * @return
     */
    List<String> getLabels();

    /**
     * Reset record reader iterator
     *
     * @return
     */
    void reset();

    /**
     * Load the record from the given DataInputStream
     * Unlike {@link #next()} the internal state of the RecordReader is not modified
     * Implementations of this method should not close the DataInputStream
     *
     * @throws IOException if error occurs during reading from the input stream
     */
    List<Writable> record(URI uri, DataInputStream dataInputStream) throws IOException;


    /**
     * Similar to {@link #next()}, but returns a {@link Record} object, that may include metadata such as the source
     * of the data
     *
     * @return next record
     */
    Record nextRecord();

    /**
     * Load a single record from the given {@link RecordMetaData} instance<br>
     * Note: that for data that isn't splittable (i.e., text data that needs to be scanned/split), it is more efficient to
     * load multiple records at once using {@link #loadFromMetaData(List)}
     *
     * @param recordMetaData Metadata for the record that we want to load from
     * @return Single record for the given RecordMetaData instance
     * @throws IOException If I/O error occurs during loading
     */
    Record loadFromMetaData(RecordMetaData recordMetaData) throws IOException;

    /**
     * Load multiple records from the given a list of {@link RecordMetaData} instances<br>
     *
     * @param recordMetaDatas Metadata for the records that we want to load from
     * @return Multiple records for the given RecordMetaData instances
     * @throws IOException If I/O error occurs during loading
     */
    List<Record> loadFromMetaData(List<RecordMetaData> recordMetaDatas) throws IOException;

    /**
     * Get the record listeners for this record reader.
     */
    List<RecordListener> getListeners();

    /**
     * Set the record listeners for this record reader.
     */
    void setListeners(RecordListener... listeners);

    /**
     * Set the record listeners for this record reader.
     */
    void setListeners(Collection<RecordListener> listeners);
}
