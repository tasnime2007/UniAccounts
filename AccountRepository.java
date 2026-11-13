package uniaccounts.repository;

import uniaccounts.audit.AuditLogger;
import uniaccounts.model.StudentAccount;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton: المستودع الوحيد الذي يحفظ كل الحسابات.
 */
public final class AccountRepository {

    private final Map<String, StudentAccount> accounts = new LinkedHashMap<>();

    // Singleton: مُنشئ خاص
    private AccountRepository() { }

    // Singleton: Holder idiom
    private static class Holder {
        private static final AccountRepository INSTANCE = new AccountRepository();
    }

    // Singleton: نقطة الوصول الوحيدة
    public static AccountRepository getInstance() {
        return Holder.INSTANCE;
    }

    /** يضيف حسابا. الرقم يجب أن يكون فريدا في المستودع. */
    public synchronized void add(StudentAccount account) {
        AuditLogger logger = AuditLogger.getInstance();
        if (accounts.containsKey(account.getId())) {
            logger.log("REJECTED", "duplicate id " + account.getId());
            throw new IllegalArgumentException("duplicate id: " + account.getId());
        }
        accounts.put(account.getId(), account);
        logger.log("CREATED", account.getId() + " " + account.getFullName());
    }

    public synchronized Optional<StudentAccount> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized List<StudentAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized int count() {
        return accounts.size();
    }
}
