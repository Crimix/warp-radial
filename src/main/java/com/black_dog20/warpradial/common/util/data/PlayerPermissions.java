package com.black_dog20.warpradial.common.util.data;

public class PlayerPermissions {
    private String uuid;
    private String displayName;
    private boolean createServerWarps;
    private boolean deleteServerWarps;

    public PlayerPermissions(String uuid, String displayName, boolean createServerWarps, boolean deleteServerWarps) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.createServerWarps = createServerWarps;
        this.deleteServerWarps = deleteServerWarps;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canCreateServerWarps() {
        return createServerWarps;
    }

    public boolean canDeleteServerWarps() {
        return deleteServerWarps;
    }

    public void setCreateServerWarps(boolean createServerWarps) {
        this.createServerWarps = createServerWarps;
    }

    public void setDeleteServerWarps(boolean deleteServerWarps) {
        this.deleteServerWarps = deleteServerWarps;
    }
}
