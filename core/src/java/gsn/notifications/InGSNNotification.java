package gsn.notifications;

import gsn.Main;
import gsn.beans.StreamElement;
import gsn.storage.DataEnumerator;
import gsn.wrappers.InVMPipeWrapper;
import org.apache.log4j.Logger;

public class InGSNNotification extends NotificationRequest {

    private InVMPipeWrapper inVMPipeWrapper;

    private String remoteVSName;

    private final transient Logger logger = Logger.getLogger(InGSNNotification.class);

    private StringBuilder query;

    public InGSNNotification(InVMPipeWrapper listener, String remoteVSName) {
        this.inVMPipeWrapper = listener;
        this.remoteVSName = remoteVSName;
        query = new StringBuilder("select * from ").append(remoteVSName).append(" order by timed desc limit 1 offset 0");

    }

    private int notificationCode = Main.tableNameGenerator();

    public int getNotificationCode() {
        return notificationCode;
    }

    /**
     * Returning null means select * This is used for optimization, see
     * ContainerImpl for more information.
     */
    public StringBuilder getQuery() {
        return query;
    }

    public boolean send(DataEnumerator data) {
        if (logger.isDebugEnabled())
            logger.debug("InVMPipe received the stream elements.");
        try {
            while (data.hasMoreElements()) {
                StreamElement nextElement = data.nextElement();
                if (logger.isDebugEnabled())
                    logger.debug("InVMPipe submit a stream element.");
                inVMPipeWrapper.remoteDataReceived(nextElement);
            }
            return true;
        } catch (Exception e) {
            logger.warn("Notification Failed !, Error : " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((remoteVSName == null) ? 0 : remoteVSName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final InGSNNotification other = (InGSNNotification) obj;
        if (remoteVSName == null) {
            if (other.remoteVSName != null)
                return false;
        } else if (!remoteVSName.equals(other.remoteVSName))
            return false;
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InGSNNotification, Listener : ").append(inVMPipeWrapper).append(" with Query ").append(query);
        return sb.toString();
    }

    public String getOriginalQuery() {
        return query.toString();
    }
}