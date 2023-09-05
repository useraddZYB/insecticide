package com.programmerartist.insecticide.tunnel.admin.constants;

/**
 * Created by 程序员Artist on 16/3/24.
 */
public class DomainInit {

    private String traceName;
    private boolean stop;

    /**
     *
     * @param traceName
     * @param stop
     */
    public DomainInit(String traceName, boolean stop) {
        this.traceName = traceName;
        this.stop = stop;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    @Override
    public String toString() {
        return "DomainInit{" +
                "traceName='" + traceName + '\'' +
                ", stop=" + stop +
                '}';
    }
}
