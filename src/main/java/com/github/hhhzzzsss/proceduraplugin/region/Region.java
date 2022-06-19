package com.github.hhhzzzsss.proceduraplugin.region;

import ch.ethz.globis.phtree.PhTreeSolid;

public class Region {
    public final int xpos;
    public final int ypos;
    public final int zpos;
    public final int xdim;
    public final int ydim;
    public final int zdim;
    public String name = "Unnamed Region";

    public Region(int xpos, int ypos, int zpos, int xdim, int ydim, int zdim) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.zpos = zpos;
        this.xdim = xdim;
        this.ydim = ydim;
        this.zdim = zdim;
    }

    public PhTreeSolid.PhEntryS<Region> getPhEntry() {
        return new PhTreeSolid.PhEntryS(
                new long[]{xpos, zpos},
                new long[]{xpos+xdim, zpos+zdim},
                this
        );
    }
}
