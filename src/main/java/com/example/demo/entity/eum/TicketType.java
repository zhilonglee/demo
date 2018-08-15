package com.example.demo.entity.eum;

public enum TicketType {

    PRIMARYCLASS(0),
    FIRSTCLASS(1),
    SECONDCLASS(2);

    private int type;

    TicketType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
