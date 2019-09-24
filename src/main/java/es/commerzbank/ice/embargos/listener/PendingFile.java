package es.commerzbank.ice.embargos.listener;

import java.io.File;

public class PendingFile
{
    private File file;
    private long snapshotTime;
    private long lastsize;
    private String pendingMove;

    public String getPendingMove() {
        return pendingMove;
    }

    public void setPendingMove(String pendingMove) {
        this.pendingMove = pendingMove;
    }

    public long getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(long snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public long getLastsize() {
        return lastsize;
    }

    public void setLastsize(long lastsize) {
        this.lastsize = lastsize;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}