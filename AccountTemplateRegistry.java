package uniaccounts.prototype;

import uniaccounts.model.StudentAccount;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype: سجل القوالب. لكل دفعة قالب جاهز (التخصص، المستوى، المقررات)،
 * ونُنتج منه حسابات جديدة بالنسخ وتغيير الهوية فقط.
 */
public class AccountTemplateRegistry {

    private final Map<String, StudentAccount> templates = new HashMap<>();

    public void register(String key, StudentAccount template) {
        templates.put(key, template);
    }

    // Prototype: ننسخ القالب بدل البناء من الصفر
    public StudentAccount create(String key, String id, String fullName, String email) {
        StudentAccount template = templates.get(key);
        if (template == null)
            throw new IllegalArgumentException("unknown template: " + key);
        return template.copyFor(id, fullName, email);
    }
}
