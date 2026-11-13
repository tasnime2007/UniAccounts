package uniaccounts.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * StudentAccount هو المنتج في نمط Builder، وهو أيضا النموذج الأولي (Prototype).
 * الحقول final: الحساب لا يتغير بعد إنشائه.
 */
public class StudentAccount {

    private static final Set<String> LEVELS = Set.of("L1", "L2", "L3", "M1", "M2");

    private final String id;           // إجباري
    private final String fullName;     // إجباري
    private final String email;        // إجباري
    private final String phone;        // اختياري، الافتراضي null
    private final String level;        // اختياري، الافتراضي L1
    private final String major;        // اختياري
    private final boolean scholarship; // اختياري، الافتراضي false
    private final List<String> courses;// اختياري، فارغة افتراضيا

    // Builder: المُنشئ خاص، الطريقة الوحيدة للإنشاء هي Builder.build()
    private StudentAccount(Builder b) {
        this.id = b.id;
        this.fullName = b.fullName;
        this.email = b.email;
        this.phone = b.phone;
        this.level = b.level;
        this.major = b.major;
        this.scholarship = b.scholarship;
        this.courses = new ArrayList<>(b.courses);
    }

    // Prototype: مُنشئ النسخ. ننسخ كل شيء ونغيّر الهوية فقط.
    private StudentAccount(StudentAccount src, String id, String fullName, String email) {
        validateIdentity(id, fullName, email);
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = null;                       // الهاتف شخصي، لا يُنسخ من القالب
        this.level = src.level;
        this.major = src.major;
        this.scholarship = src.scholarship;
        this.courses = new ArrayList<>(src.courses); // نسخة عميقة: قائمة مستقلة
    }

    // Prototype: النسخ مع تغيير الهوية (Prototype.clone)
    public StudentAccount copyFor(String newId, String newFullName, String newEmail) {
        return new StudentAccount(this, newId, newFullName, newEmail);
    }

    private static void validateIdentity(String id, String fullName, String email) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id is required");
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("fullName is required");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("email must contain '@'");
    }

    // ---------------- Getters ----------------
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLevel() { return level; }
    public String getMajor() { return major; }
    public boolean hasScholarship() { return scholarship; }
    public List<String> getCourses() { return Collections.unmodifiableList(courses); }

    @Override
    public String toString() {
        return "StudentAccount{id=" + id + ", name=" + fullName + ", email=" + email
                + ", phone=" + phone + ", level=" + level + ", major=" + major
                + ", scholarship=" + scholarship + ", courses=" + courses + "}";
    }

    /**
     * Builder: يبني الحساب خطوة بخطوة.
     * الحقول الإجبارية في المُنشئ (لا يمكن نسيانها)، والاختيارية بدوال متسلسلة.
     */
    public static class Builder {
        private final String id;
        private final String fullName;
        private final String email;
        private String phone = null;
        private String level = "L1";
        private String major = null;
        private boolean scholarship = false;
        private final List<String> courses = new ArrayList<>();

        public Builder(String id, String fullName, String email) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
        }

        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder level(String level) { this.level = level; return this; }
        public Builder major(String major) { this.major = major; return this; }
        public Builder scholarship(boolean s) { this.scholarship = s; return this; }
        public Builder addCourse(String course) { this.courses.add(course); return this; }
        public Builder courses(List<String> list) {
            this.courses.clear();
            this.courses.addAll(list);
            return this;
        }

        // Builder: كل التحقق من القواعد يتم هنا، فلا يوجد حساب غير صالح أبدا
        public StudentAccount build() {
            validateIdentity(id, fullName, email);
            if (!LEVELS.contains(level))
                throw new IllegalArgumentException("invalid level: " + level);
            return new StudentAccount(this);
        }
    }
}
