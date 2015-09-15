package moy.tollenaar.stephen.NPC;

public enum NPCAnimation {
    /**
     * Makes NPC swing his arm.
     */
    SWING_ARM(0),
    /**
     * Highlights the NPC in red to mark that it has been damaged.
     */
    DAMAGE(1),
    /**
     * Moves NPC's arm towards his mouth to eat food.
     */
    EAT_FOOD(3),
    /**
     * Displays criticial hit
     */
    CRITICAL_HIT(4),
    /**
     * Display magic critical hit
     */
    MAGIC_CRITICAL_HIT(5),
    /**
     * Makes NPC crouch.
     */
    CROUCH(104),
    /**
     * Stops NPC from crouching.
     */
    UNCROUCH(105);

    private final int id;

    private NPCAnimation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
