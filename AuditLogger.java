package uniaccounts.audit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton: سجل التدقيق الوحيد في البرنامج.
 * كل الأجزاء تكتب في نفس السجل عبر getInstance().
 */
public final class AuditLogger {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<String> entries = new ArrayList<>();

    // Singleton: المُنشئ خاص، لا أحد يستطيع كتابة new AuditLogger()
    private AuditLogger() { }

    // Singleton: النسخة تُنشأ عند أول استعمال فقط (Holder idiom) وهي آمنة مع الخيوط
    private static class Holder {
        private static final AuditLogger INSTANCE = new AuditLogger();
    }

    // Singleton: نقطة الوصول الوحيدة إلى النسخة
    public static AuditLogger getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void log(String action, String details) {
        entries.add("[" + LocalDateTime.now().format(FMT) + "] " + action + " | " + details);
    }

    public synchronized List<String> getEntries() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }

    public synchronized void print() {
        entries.forEach(System.out::println);
    }
}
