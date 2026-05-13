---
name: netbeans-form-safe-edit
description: "Trigger: edit netbeans form, swing ui, jframe form, .form file, netbeans designer. Safely edit Java Swing JFrameForms in NetBeans without corrupting .form files. Enforces strict separation between hand-editable .java code and NetBeans-generated .form XML."
license: MIT
metadata:
  author: OpenCode Agent
  version: 1.0.0
  project: Proyecto
---

# NetBeans Form Safe Edit

## Activation Contract

Load this skill when:
- User requests changes to **JFrameForms** in NetBeans (e.g., "Add a button to FrmLogin").
- Agent detects `.form` files in the project.
- Task involves **UI modifications** in Java Swing.

Do NOT load for:
- Non-Swing Java projects.
- Projects without `.form` files.
- Changes to business logic only (use `sdd-apply` instead).

---

## Hard Rules

1. **NEVER edit `.form` files directly** — they are auto-generated XML by NetBeans.
2. **Only modify `.java` files** associated with the `.form` (e.g., `FrmLogin.java`).
3. **Preserve guarded blocks** — do not alter lines marked:
   ```java
   // Variables declaration - do not modify
   // End of variables declaration
   ```
4. **Use NetBeans Design View** for UI changes — never manually edit component initialization.
5. **Validate after edits** — ensure the `.form` opens in NetBeans without errors.
6. **Backup first** — create a backup of the `.form` and `.java` files before editing.

---

## Decision Gates

| Scenario                          | Action                                                                                     |
|-----------------------------------|--------------------------------------------------------------------------------------------|
| Add/remove UI components          | Use NetBeans Design View (manual process).                                                 |
| Modify component properties       | Edit `.java` file **outside** guarded blocks.                                              |
| Change event handlers             | Edit `.java` file (safe to modify methods).                                                |
| Rename components                 | ❌ **Forbidden** (breaks `.form` references).                                               |
| Edit layout managers              | Use NetBeans Design View or edit `.java` **outside** guarded blocks (risky).              |

---

## Execution Steps

1. **Backup**
   - Copy `.form` and `.java` files to `assets/backups/` with timestamp.

2. **Identify Editable Regions**
   - Open `.java` file and locate:
     - Method `initComponents()` (editable outside guarded blocks).
     - Event handlers (e.g., `btnLoginActionPerformed`).

3. **Edit Safely**
   - For **component properties**:
     ```java
     btnLogin.setText("Iniciar Sesión");  // Safe
     ```
   - For **event handlers**:
     ```java
     private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
         // Custom logic here
     }
     ```

4. **Validate**
   - Open `.form` in NetBeans Design View.
   - Check for errors in the **Navigator** panel.
   - Run the application to verify UI behavior.

5. **Recover if Corrupted**
   - Restore from backup if NetBeans shows:
     - `Form load error`
     - `NullPointerException` in Design View.
   - Reapply changes manually in NetBeans.

---

## Output Contract

Return:
- **Modified `.java` file** with changes applied safely.
- **Backup files** in `assets/backups/` (if created).
- **Validation report** (success/failure and errors).

Example output:
```json
{
  "status": "success",
  "modified_files": ["src/views/FrmLogin.java"],
  "backups_created": ["assets/backups/FrmLogin.java_20260513_120000"],
  "validation": {
    "design_view": "no_errors",
    "runtime": "functional"
  }
}
```

---

## References

- `assets/backups/` — Backup copies of `.form` and `.java` files.
- `references/netbeans-form-guide.md` — Detailed guide on NetBeans `.form` structure (create this file if needed).
- Project `README.md` — Architecture rules for Swing UI.
- NetBeans Design View Documentation: [Official Guide](https://netbeans.apache.org/kb/docs/java/quickstart-gui.html)