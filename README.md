# UniAccounts - Work 1 (Creational Patterns)

## Run
From the project root (needs JDK 11+):

    mkdir out
    javac -encoding UTF-8 -d out $(find src -name "*.java")     # Linux / macOS
    java -cp out uniaccounts.Main

On Windows (PowerShell):

    mkdir out
    javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
    java -cp out uniaccounts.Main

Or open `src` in IntelliJ / Eclipse and run `uniaccounts.Main`.

## Structure
| Package | Class | Pattern |
|---|---|---|
| uniaccounts.model | StudentAccount (+ nested Builder) | Builder (and Prototype participant) |
| uniaccounts.audit | AuditLogger | Singleton |
| uniaccounts.repository | AccountRepository | Singleton |
| uniaccounts.prototype | AccountTemplateRegistry | Prototype (registry of templates) |
| uniaccounts | Main | Demo |
