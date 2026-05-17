package util;

import model.UserRole;
import java.awt.Color;

public class UITheme {
    
    // Backgrounds & Surfaces
    public static final Color BACKGROUND_MAIN = new Color(13, 14, 18);     // #0D0E12 Deep Obsidian
    public static final Color BACKGROUND_CARD = new Color(22, 23, 29);      // #16171D Dark Grey Surface
    public static final Color BORDER_COLOR = new Color(38, 41, 48);         // #262930 Ultra-thin Border
    
    // Typography
    public static final Color TEXT_PRIMARY = new Color(243, 244, 246);      // #F3F4F6 Near-white
    public static final Color TEXT_MUTED = new Color(156, 163, 175);        // #9CA3AF Medium grey
    
    // Accents
    public static final Color ACCENT_INDIGO = new Color(79, 70, 229);       // #4F46E5 Primary Brand Accent
    public static final Color ACCENT_EMERALD = new Color(16, 185, 129);     // #10B981 Success / Active Badge
    public static final Color ACCENT_CRIMSON = new Color(239, 68, 68);      // #EF4444 Danger / Refined Crimson
    
    // Dynamic Role-based Sidebar and Highlight Colors
    public static final Color ADMIN_COLOR = new Color(79, 70, 229);         // Admin = Indigo Accent
    public static final Color MENTOR_COLOR = new Color(16, 185, 129);       // Mentor = Emerald Accent
    public static final Color MENTEE_COLOR = new Color(59, 130, 246);       // Mentee = Royal Blue Accent
    public static final Color DEFAULT_COLOR = new Color(156, 163, 175);

    // Sidebar Backgrounds
    public static final Color SIDEBAR_ADMIN = BACKGROUND_MAIN;               // #0D0E12 Obsidian Black
    public static final Color SIDEBAR_MENTEE = new Color(15, 23, 42);        // #0F172A Slate Dark Blue
    public static final Color SIDEBAR_MENTOR = new Color(2, 44, 34);         // #022C22 Deep Dark Green

    public static Color getColorForRole(UserRole role) {
        if (role == null) return DEFAULT_COLOR;
        switch (role) {
            case ADMIN: return ADMIN_COLOR;
            case MENTOR: return MENTOR_COLOR;
            case MENTEE: return MENTEE_COLOR;
            default: return DEFAULT_COLOR;
        }
    }

    public static Color getSidebarBackgroundForRole(UserRole role) {
        if (role == null) return BACKGROUND_MAIN;
        switch (role) {
            case ADMIN: return SIDEBAR_ADMIN;
            case MENTOR: return SIDEBAR_MENTOR;
            case MENTEE: return SIDEBAR_MENTEE;
            default: return BACKGROUND_MAIN;
        }
    }
}