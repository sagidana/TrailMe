package depton.trailme.fragments.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final List<DummyTrack> TRACK_ITEMS = new ArrayList<DummyTrack>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    public static final Map<String, DummyTrack> TRACK_MAP = new HashMap<String, DummyTrack>();

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    static {
        // Add some sample items.
        DummyTrack Gilabun = new DummyTrack(String.valueOf(1), "Gilabon", "this is the description", 3);
        TRACK_ITEMS.add(Gilabun);
        TRACK_MAP.put(Gilabun.id, Gilabun);
    }

    private static void addTrack(DummyTrack item) {
        TRACK_ITEMS.add(item);
        TRACK_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class DummyHiker {
        public final String id;
        public final String content;
        public final String details;

        public DummyHiker(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class DummyGroup {
        public final String id;
        public final String content;
        public final String details;

        public DummyGroup(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class DummyTrack {
        public final String id;
        public final String name;
        public final String description;
        public final int rating;

        public DummyTrack(String id, String name, String description,int rating) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
