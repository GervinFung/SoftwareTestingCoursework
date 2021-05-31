package main.backend.item;

public final class Item {
    
    // create immutable object to reduce bug occurrence
    private final int ID;
    private final String name;
    private final float memberPrice, nonMemberPrice;
    private final PROMOTIONAL promotional;
    private final ITEM_TYPE item_type;

    protected Item(final int ID, final String name, final char c, final float memberPrice, final float nonMemberPrice, final boolean promotional) {
        this.ID = ID;
        this.name = name;
        this.item_type = c == 'C' ? ITEM_TYPE.CAKE : ITEM_TYPE.PASTRY;
        this.memberPrice = memberPrice;
        this.nonMemberPrice = nonMemberPrice;
        this.promotional = promotional ? PROMOTIONAL.YES : PROMOTIONAL.NO;
    }

    public float getMemberPrice() { return this.memberPrice; }
    public float getNonMemberPrice() { return this.nonMemberPrice; }
    public PROMOTIONAL getPromotional() { return this.promotional; }
    public int getID() { return ID; }
    public ITEM_TYPE getItemType() { return this.item_type; }

    public enum ITEM_TYPE {
        CAKE {
            @Override
            public boolean isCake() { return true; }
        }, PASTRY {
            @Override
            public boolean isCake() { return false; }
        };
        public abstract boolean isCake();
    }

    public enum PROMOTIONAL {
        YES {
            @Override
            public float getDiscountRatio() { return 0.95f; }
            @Override
            public String toString() { return "5% discount"; }
            @Override
            public boolean isPromotional() { return true; }
        }, NO {
            @Override
            public float getDiscountRatio() { return 1f; }
            @Override
            public String toString() { return "No"; }
            @Override
            public boolean isPromotional() { return false; }
        };
        public abstract float getDiscountRatio();
        public abstract boolean isPromotional();
    }

    //Override hashcode and equals to make each Item objects unique
    @Override
    public int hashCode() { return this.getID(); }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }

        if (!(obj instanceof Item)) { return false; }

        return ((Item)obj).getID() == this.getID();
    }

    @Override
    public String toString() {
        return String.format("%-12d%-36s%-8s%-17.2f%-22.2f%-13s", this.ID, this.name, this.item_type.toString(), this.memberPrice, this.nonMemberPrice, this.promotional.toString());
    }
}