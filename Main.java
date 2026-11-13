package uniaccounts;

import uniaccounts.audit.AuditLogger;
import uniaccounts.model.StudentAccount;
import uniaccounts.prototype.AccountTemplateRegistry;
import uniaccounts.repository.AccountRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AccountRepository repo = AccountRepository.getInstance();
        AuditLogger audit = AuditLogger.getInstance();

        // ---------- 1) Singleton ----------
        System.out.println("=== 1. Singleton ===");
        System.out.println("Same repository instance? " + (repo == AccountRepository.getInstance()));
        System.out.println("Same audit logger instance? " + (audit == AuditLogger.getInstance()));

        // ---------- 2) Builder ----------
        System.out.println("\n=== 2. Builder ===");
        StudentAccount full = new StudentAccount.Builder("20240001", "Yacine Bensalem", "y.bensalem@univ.dz")
                .phone("0550000000")
                .level("L3")
                .major("Informatique")
                .scholarship(true)
                .addCourse("Java")
                .addCourse("Databases")
                .build();
        repo.add(full);
        System.out.println(full);

        StudentAccount minimal = new StudentAccount.Builder("20240002", "Nadia Ouali", "n.ouali@univ.dz").build();
        repo.add(minimal);
        System.out.println(minimal + "   <- defaults used");

        expectError("Email without @", () ->
                new StudentAccount.Builder("20240003", "Test", "no-at-sign").build());
        expectError("Invalid level", () ->
                new StudentAccount.Builder("20240003", "Test", "t@univ.dz").level("L9").build());
        expectError("Blank id", () ->
                new StudentAccount.Builder("  ", "Test", "t@univ.dz").build());
        expectError("Duplicate id", () ->
                repo.add(new StudentAccount.Builder("20240001", "Other", "o@univ.dz").build()));

        // ---------- 3) Prototype ----------
        System.out.println("\n=== 3. Prototype ===");
        StudentAccount template = new StudentAccount.Builder("TEMPLATE", "Template", "template@univ.dz")
                .level("L2")
                .major("Informatique")
                .courses(List.of("Algorithms", "Java", "Databases"))
                .build();

        AccountTemplateRegistry registry = new AccountTemplateRegistry();
        registry.register("L2-INFO-2024", template);

        repo.add(registry.create("L2-INFO-2024", "20240101", "Amine Kaci", "a.kaci@univ.dz"));
        repo.add(registry.create("L2-INFO-2024", "20240102", "Sara Haddad", "s.haddad@univ.dz"));
        repo.add(registry.create("L2-INFO-2024", "20240103", "Lina Merabet", "l.merabet@univ.dz"));

        repo.findById("20240101").ifPresent(System.out::println);
        repo.findById("20240102").ifPresent(System.out::println);
        System.out.println("Template stored in repository? " + repo.findById("TEMPLATE").isPresent());

        // ---------- Result ----------
        System.out.println("\n=== Repository ===");
        System.out.println("Total accounts: " + repo.count());

        System.out.println("\n=== Audit log ===");
        audit.print();
    }

    private static void expectError(String label, Runnable action) {
        try {
            action.run();
            System.out.println(label + " -> NO ERROR (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println(label + " -> rejected: " + e.getMessage());
        }
    }
}
