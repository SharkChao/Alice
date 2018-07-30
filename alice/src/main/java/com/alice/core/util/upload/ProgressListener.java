package com.alice.core.util.upload;

public interface ProgressListener {
        /**

         */
        void onProgress(long progress, long total, boolean done);
}
