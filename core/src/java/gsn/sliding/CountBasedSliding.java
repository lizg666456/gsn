package gsn.sliding;

import gsn.beans.StreamElement;
import gsn.utils.EasyParamWrapper;

public class CountBasedSliding implements SlidingInterface {
    private int size;
    private SlidingListener listener;
    private int tempCounter = 0;

    public boolean initialize(EasyParamWrapper easyParamWrapper, SlidingListener listener) {
        this.size = easyParamWrapper.getPredicateValueAsIntWithException("size");
        this.listener = listener;
        return true;
    }

    public void postData(StreamElement se) {
        tempCounter++;
        if (tempCounter % size == 0)
            listener.slide(se.getTimeStamp());
    }

    public void reset() {
        tempCounter = 0;
    }
}
