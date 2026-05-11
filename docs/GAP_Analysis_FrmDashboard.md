# GAP Analysis: FrmDashboard Actual vs. Diseño Objetivo (home.png)

---

## 1. RESUMEN EJECUTIVO

El formulario `FrmDashboard` actual implementa un menú de navegación tipo `JMenuBar` (archivo tradicional de escritorio), mientras que el diseño objetivo `home.png` muestra una interfaz moderna tipo **dashboard con header oscuro, tarjetas de navegación visual y acceso directo a funcionalidades**. La raíz del problema es que `JMenuBar` es un componente de menú desplegable (diseñado para apps MDI de los 90s), no un componente de navegación visual. El gap no es cosmético — es arquitectónico: se requiere reemplazar el sistema de navegación completamente.

---

## 2. GAP ANALYSIS — TABLA COMPARATIVA

| Aspecto | Estado Actual (FrmDashboard) | Diseño Objetivo (home.png) | Severidad |
|---|---|---|---|
| **Navegación principal** | `JMenuBar` con 2 menús desplegables ("Gestión" → 8 items, "Sistema" → 1 item) | Header oscuro horizontal (~30-40px) con elementos visibles permanentemente | 🔴 CRÍTICO |
| **Header / Navbar** | No existe como tal. El `JMenuBar` actúa como header funcional pero con aspecto de menú OS nativo | Barra oscura sólida (avg RGB ~108,91,96) de ~30-40px en la parte superior | 🔴 CRÍTICO |
| **Visibilidad de opciones** | Las opciones están ocultas en submenús (requieren clic en "Gestión" para ver la lista desplegable) | Todas las opciones visibles como botones/tarjetas en la superficie del dashboard | 🔴 CRÍTICO |
| **Área de contenido** | 3 `JLabel` centrados: bienvenida, icono grande y subtítulo | Panel central con tarjetas (icono + texto), posible sidebar izquierdo con accesos directos | 🟡 ALTO |
| **Estilo visual** | Tema Nimbus estándar, fondo blanco, tipografía Segoe UI | Diseño plano moderno con header oscuro, bordes laterales oscuros, tarjetas visuales | 🟡 ALTO |
| **Uso del espacio** | Vertical: MenuBar + 3 labels centrados (mucho espacio vacío) | Header + sidebar + content area + footer, aprovechando todo el viewport | 🟡 ALTO |
| **Iconografía** | 1 icono grande centrado en el área de contenido | Múltiples iconos en tarjetas/botones de acceso directo | 🟢 MEDIO |
| **Retrocompatibilidad .form** | JMenuBar definido en `<NonVisualComponents>` del .form | Requiere componentes visuales (`JPanel`, `JButton`) como SubComponents | 🟢 MEDIO |
| **Eventos** | 8 ActionListeners en `configurarComponentes()` | Similar cantidad de ActionListeners, pero en botones en lugar de menu items | 🟢 BAJO |
| **Look and Feel** | Nimbus (configurado en `App.java`) | Nimbus compatible con colores personalizados (setBackground) | 🟢 BAJO |

---

## 3. ROOT CAUSE ANALYSIS

### 3.1 ¿Por qué el JMenuBar actual NO puede lograr el look del header oscuro de home.png?

**Razón técnica fundamental**: `javax.swing.JMenuBar` es un componente cuya apariencia es **controlada completamente por el Look and Feel (L&F)**. Las propiedades que NO se pueden personalizar adecuadamente son:

1. **Background**: En Nimbus, `JMenuBar.setBackground(Color)` es mayormente ignorado. Nimbus usa `MenuBar.backgroundPainter` que no respeta el color arbitrario.
2. **Altura fija**: La altura del `JMenuBar` está determinada por el L&F (normalmente 20-25px). No se puede expandir a 40px manteniendo la usabilidad.
3. **Sin contenido interno**: Un `JMenuBar` solo acepta `JMenu` (y por extensión `JMenuItem`). No se puede añadir un `JLabel` con logo, ni `JButton` de acción directa, ni espaciadores personalizados.
4. **Interacción tipo dropdown**: Para llegar a "Empleados", el usuario debe: clic en "Gestión" → esperar despliegue → mover ratón → clic en "Empleados". En el target, es un solo clic directo.
5. **Estética "windows 95"**: Incluso con Nimbus, un `JMenuBar` evoca aplicaciones de escritorio legacy, no dashboards modernos.

### 3.2 ¿Qué componentes Swing pueden reemplazar el JMenuBar?

| Componente | ¿Sirve? | Razón |
|---|---|---|
| `JPanel` pintado + `JButton` / `JLabel` | ✅ RECOMENDADO | Control total de color, altura, contenido. Cada botón es un acceso directo visible. |
| `JToolBar` | ⚠️ POSIBLE | Soporta botones, pero está diseñado para floating/dragging. Difícil de estilizar como header fijo. |
| `JDesktopPane` + `JInternalFrame` | ❌ NO | Arquitectura MDI. No aplica. |
| `JTabbedPane` | ❌ NO | Orientado a pestañas de contenido, no a navegación principal. |

**Veredicto**: `JPanel` + `JButton` es la opción correcta. Un `JPanel` con `setBackground(new Color(108, 91, 96))` + `setOpaque(true)` + `setPreferredSize(new Dimension(w, 40))` + un `FlowLayout` o `BorderLayout` para los botones.

### 3.3 ¿Conviene usar JPanel pintado personalizado + JButton/JLabel en lugar de JMenuBar?

**Sí, es la única opción viable.** Justificación:

- `JPanel` soporta `paintComponent()` si se requiere gradiente
- `JButton` en Nimbus puede tener fondo personalizado con `setBackground()` + `setContentAreaFilled(false)` + override del painter
- Se pueden agrupar los botones en un `JPanel` con `FlowLayout(LEFT, 5, 0)` para la navegación principal
- Se puede agregar un `JLabel` con el logo/nombre de la app a la izquierda
- Se puede agregar un botón "Cerrar Sesión" a la derecha usando `BorderLayout`

---

## 4. TECHNICAL APPROACH RECOMMENDATION

### Opción A: Reemplazo completo (RECOMENDADA ✅)

**Descripción**: Eliminar el `JMenuBar` del .form y reemplazarlo con una estructura de `JPanel` header + `JPanel` contenido con tarjetas de navegación.

**Arquitectura propuesta**:
```
FrmDashboard (JFrame)
├── pnlHeader (JPanel - bg oscuro, 40px altura)
│   ├── lblAppTitle (JLabel - "Sistema de Planillas")
│   ├── pnlNavButtons (JPanel - FlowLayout)
│   │   ├── btnEmpleados (JButton)
│   │   ├── btnDepartamentos (JButton)
│   │   ├── btnMarcacion (JButton)
│   │   ├── btnPlanillas (JButton)
│   │   ├── btnReportes (JButton)
│   │   ├── btnContratos (JButton)
│   │   ├── btnHorarios (JButton)
│   │   └── btnAsigJornada (JButton)
│   └── btnCerrarSesion (JButton - alineado derecha)
├── pnlMainContent (JPanel - BorderLayout o GridBagLayout)
│   ├── pnlSidebar (JPanel - opcional, accesos rápidos con iconos)
│   │   └── [tarjetas pequeñas con icono + texto]
│   └── pnlCenter (JPanel - área principal)
│       ├── lblBienvenida (JLabel - título)
│       ├── pnlCardsGrid (JPanel - GridLayout 2x4 o 4x2)
│       │   ├── cardEmpleados (Acceso rapido card)
│       │   ├── cardDepartamentos (Acceso rapido card)
│       │   ├── ... (8 cards en total)
│       └── pnlFooter (JPanel - barra inferior con iconos)
└── [Sin JMenuBar]
```

**Ventajas**:
- Control total del diseño y la estética
- Coincide con el diseño objetivo
- Tiempo de respuesta más rápido (un clic directo)
- Escalable a futuro (pueden agregarse más tarjetas)

**Riesgos**:
- El .form debe ser modificado fuera del Form Editor (edición XML directa)
- Se pierde la edición visual del header (pero se gana flexibilidad)
- Los botones requieren configuración adicional en Nimbus para estilizarlos

**Mitigación de riesgos**:
- Mantener la estructura GEN-BEGIN/GEN-END intacta editando el .form XML directamente
- Los botones del header se estilizan con `setForeground(Color.WHITE)` + `setContentAreaFilled(false)` + `setBorderPainted(false)` + `setCursor(Cursor.HAND_CURSOR)`

### Opción B: Híbrida (No recomendada ❌)

**Descripción**: Mantener el `JMenuBar` oculto y construir la UI encima.

**Problemas**:
- `setJMenuBar(null)` en `configurarComponentes()` es peligroso (puede dejar referencias huérfanas)
- El .form aún genera el código del menu bar, consumiendo recursos
- Complejidad innecesaria
- No resuelve el problema de fondo

### Opción C: JToolBar + JPanel (Descartada ⚠️)

**Descripción**: Usar `JToolBar` como header con botones.

**Problemas**:
- `JToolBar` es draggable (el usuario puede moverlo)
- Difícil de fijar en la parte superior con color personalizado
- No soporta gradiente fácilmente

---

## 5. LOOK AND FEEL: RECOMENDACIÓN

**Estado actual**: Nimbus (configurado en `App.java:12`).

**Recomendación**: **Mantener Nimbus** + personalización de colores vía `UIManager` para componentes específicos.

**Justificación**:
- Nimbus es el L&F más moderno incluido en JDK 11+ (Metal es vintage, Motif es legacy)
- Nimbus soporta `setBackground()` en `JPanel` (funciona correctamente con `setOpaque(true)`)
- Nimbus permite personalización por componente usando `UIManager.put("Button[Enabled].backgroundPainter", ...)`
- Migrar a FlatLaf está explícitamente descartado por las reglas del proyecto
- Nimbus es 100% compatible con NetBeans Form Editor

**Configuración adicional recomendada en App.java**:
```java
// Personalizar colores Nimbus para el dashboard
UIManager.put("control", new Color(240, 240, 245));        // Fondo general
UIManager.put("nimbusBase", new Color(108, 91, 96));        // Color base para el header
UIManager.put("nimbusBlueGrey", new Color(180, 170, 175));
UIManager.put("text", new Color(50, 50, 50));
```

---

## 6. LISTA DETALLADA DE COMPONENTES SWING NECESARIOS

### 6.1 Header (NavBar) — Reemplazo del JMenuBar

| Variable | Clase | Propiedades clave | Propósito |
|---|---|---|---|
| `pnlHeader` | `javax.swing.JPanel` | `bg=Color(108,91,96)`, `prefH=40`, `opaque=true` | Contenedor del header oscuro |
| `lblAppTitle` | `javax.swing.JLabel` | `text="Sistema de Planillas"`, `fg=Color.WHITE`, `font=Segoe UI Bold 14` | Logo/nombre de la app a la izquierda |
| `pnlNavButtons` | `javax.swing.JPanel` | `bg=Color(108,91,96)`, `layout=FlowLayout(LEFT,2,0)` | Agrupación de botones de navegación |
| `btnEmpleados` | `javax.swing.JButton` | `text="Empleados"`, `fg=WHITE`, `borderPainted=false`, `contentAreaFilled=false`, `cursor=HAND` | Acceso directo |
| `btnDepartamentos` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnMarcacion` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnPlanillas` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnReportes` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnContratos` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnHorarios` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnAsigJornada` | `javax.swing.JButton` | igual que btnEmpleados | Acceso directo |
| `btnCerrarSesion` | `javax.swing.JButton` | `text="Cerrar Sesión"`, `fg=WHITE`, `contentAreaFilled=false` | alineado a la derecha |

### 6.2 Área de Contenido Principal

| Variable | Clase | Propiedades clave | Propósito |
|---|---|---|---|
| `pnlMainContent` | `javax.swing.JPanel` | `layout=BorderLayout`, `bg=WHITE` | Contenedor principal |
| `lblBienvenida` | `javax.swing.JLabel` | `text="Bienvenid@ al Sistema de Planilla"`, `font=Segoe UI Bold 24`, `horizontalAlignment=CENTER` | Título de bienvenida |
| `pnlCardsGrid` | `javax.swing.JPanel` | `layout=GridLayout(2,4,10,10)` o `GridLayout(4,2,10,10)` | Grilla de tarjetas de acceso rápido |
| `cardEmpleados` | `javax.swing.JPanel` | `border=LineBorder(lightGray)`, `cursor=HAND`, `layout=BorderLayout` | Tarjeta clicable con icono + label |
| `cardDepartamentos` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| `cardMarcacion` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| `cardPlanillas` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| `cardReportes` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| `cardContratos` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| `cardHorarios` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| `cardAsigJornada` | `javax.swing.JPanel` | igual que cardEmpleados | Tarjeta clicable |
| (dentro de cada card) `lblIcono` | `javax.swing.JLabel` | `icon=...` (icono pequeño 24x24 o 32x32) | Icono de la tarjeta |
| (dentro de cada card) `lblTexto` | `javax.swing.JLabel` | `text="Empleados"`, `font=Segoe UI 12` | Texto de la tarjeta |
| `pnlFooter` | `javax.swing.JPanel` | `bg=Color(240,240,245)`, `prefH=30` | Barra de estado inferior |

### 6.3 Layouts Principales

| Contenedor | Layout | Parámetros |
|---|---|---|
| `getContentPane()` | `BorderLayout` | N/A (reemplaza al GroupLayout actual) |
| `pnlHeader` | `BorderLayout` | N/A (title WEST, nav CENTER, logout EAST) |
| `pnlNavButtons` | `FlowLayout` | `FlowLayout.LEFT, 2, 0` |
| `pnlMainContent` | `BorderLayout` | N/A |
| `pnlCardsGrid` | `GridLayout` | `4 filas, 2 columnas, 10px gap, 10px gap` |
| `cardEmpleados` (y similares) | `BoxLayout` o `BorderLayout` | `PAGE_AXIS` para centrar icono + texto |

---

## 7. PLAN DE IMPLEMENTACIÓN — ORDEN Y PRIORIDAD

### Fase 1: Preparación (🧩 Prioridad: INMEDIATA)

| # | Tarea | Archivo | Descripción |
|---|---|---|---|
| 1.1 | Crear respaldo del .form y .java actuales | `FrmDashboard.form.bak`, `FrmDashboard.java.bak` | `cp FrmDashboard.form FrmDashboard.form.bak` |
| 1.2 | Identificar y preparar iconos pequeños para tarjetas | `src/img/*` | Verificar existencia de iconos 24x24 o 32x32 para cada módulo. Los iconos actuales del proyecto pueden reutilizarse. |
| 1.3 | Revisar imports necesarios | `FrmDashboard.java` | `javax.swing.BorderFactory`, `javax.swing.border.LineBorder`, `java.awt.Color`, `java.awt.Cursor`, `java.awt.GridLayout`, `java.awt.BorderLayout` |

### Fase 2: Modificación del .form (🧩 Prioridad: ALTA)

| # | Tarea | Acción |
|---|---|---|
| 2.1 | Eliminar `<NonVisualComponents>` del .form | Remover el bloque `jMenuBar1`, `jMenu1`, `jMenu2` y todos los `JMenuItem`. |
| 2.2 | Agregar `pnlHeader` como SubComponent | `<Container class="javax.swing.JPanel" name="pnlHeader">` |
| 2.3 | Agregar `pnlNavButtons` dentro de `pnlHeader` | `<Container class="javax.swing.JPanel" name="pnlNavButtons">` |
| 2.4 | Agregar `btnEmpleados`..`btnAsigJornada` dentro de `pnlNavButtons` | 8 `<Component class="javax.swing.JButton">` |
| 2.5 | Agregar `btnCerrarSesion` dentro de `pnlHeader` | `<Component class="javax.swing.JButton">` |
| 2.6 | Agregar `lblAppTitle` dentro de `pnlHeader` | `<Component class="javax.swing.JLabel">` |
| 2.7 | Agregar `pnlMainContent` como SubComponent | `<Container class="javax.swing.JPanel" name="pnlMainContent">` |
| 2.8 | Agregar `pnlCardsGrid` dentro de `pnlMainContent` | `<Container class="javax.swing.JPanel" name="pnlCardsGrid">` |
| 2.9 | Agregar 8 tarjetas dentro de `pnlCardsGrid` | Cada una: `<Container class="javax.swing.JPanel" name="cardEmpleados">` con `JLabel` icono + `JLabel` texto |
| 2.10 | Cambiar layout del content pane | De GroupLayout a BorderLayout |

### Fase 3: Modificación del .java — initComponents() (🧩 Prioridad: ALTA)

| # | Tarea | Detalle |
|---|---|---|
| 3.1 | Reemplazar `initComponents()` | Mantener GEN-BEGIN/GEN-END, regenerar con los nuevos componentes desde el .form |
| 3.2 | Eliminar referencias a `jMenuBar1`, `jMenu1`, `jMenu2`, `itm*` | Remover de variables declaration |
| 3.3 | Agregar declaraciones de las nuevas variables | `pnlHeader`, `btnEmpleados`, etc. en la sección GEN-BEGIN:variables |

### Fase 4: Modificación del .java — configurarComponentes() (🧩 Prioridad: ALTA)

| # | Tarea | Código ejemplo |
|---|---|---|
| 4.1 | Configurar colores del header | `pnlHeader.setBackground(new Color(108, 91, 96));` |
| 4.2 | Configurar botones del header (texto blanco, sin borde) | `btnEmpleados.setForeground(Color.WHITE); btnEmpleados.setContentAreaFilled(false); btnEmpleados.setBorderPainted(false); btnEmpleados.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));` |
| 4.3 | Asignar ActionListeners a botones | `btnEmpleados.addActionListener(this::itmEmpleadoActionPerformed);` (reutilizar métodos existentes) |
| 4.4 | Configurar tarjetas (borde, cursor, hover effect) | `cardEmpleados.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); cardEmpleados.setCursor(HAND_CURSOR);` |
| 4.5 | Asignar MouseListeners a tarjetas para navegación | `cardEmpleados.addMouseListener(new MouseAdapter() { ... click -> controller.abrirGestionEmpleados() });` |
| 4.6 | Remover `setBackground(Color.WHITE)` del content pane (ya no necesario si BorderLayout) | Opcional, depende de diseño final |

### Fase 5: Limpieza (🧩 Prioridad: MEDIA)

| # | Tarea | Detalle |
|---|---|---|
| 5.1 | Remover métodos `itm*ActionPerformed` antiguos si se reemplazan por lambdas inline | Mantener si se reutilizan, eliminar si se refactorizan |
| 5.2 | Verificar que no queden referencias a `itm*` en `DashboardController` | No deberían cambiar (los métodos del controller son los mismos) |
| 5.3 | Probar compilación | `ant clean build` o Build en NetBeans |
| 5.4 | Probar en runtime | Verificar que todas las navegaciones funcionen |

### Fase 6: Pulido visual (🧩 Prioridad: BAJA — POST-MVP)

| # | Tarea | Detalle |
|---|---|---|
| 6.1 | Agregar hover effect en botones del header | `btnEmpleados.addMouseListener(new MouseAdapter() { mouseEntered -> btnEmpleados.setForeground(Color.YELLOW); mouseExited -> btnEmpleados.setForeground(Color.WHITE); })` |
| 6.2 | Agregar hover effect en tarjetas | Cambiar color de fondo o borde en hover |
| 6.3 | Personalizar Nimbus painters para JButton | `UIManager.put("Button[Enabled].backgroundPainter", ...)` para botones de tarjeta |
| 6.4 | Agregar transiciones suaves (opcional) | Con `javax.swing.Timer` |

---

## 8. RIESGOS Y MITIGACIONES

| Riesgo | Probabilidad | Impacto | Mitigación |
|---|---|---|---|
| 🔴 **El .form se corrompe al editarlo manualmente** | ALTA | ALTO | Hacer backup (.bak) antes de editar. Validar XML con `xmllint` después de editar. Mantener estructura `<Form>`, `<Layout>`, `<SubComponents>` correcta. |
| 🟡 **Los botones del header no se ven blancos en Nimbus** | MEDIA | ALTO | Usar `setForeground(Color.WHITE)` + `setContentAreaFilled(false)` + `setBorderPainted(false)`. Si no funciona, usar `UIManager.put("Button[Enabled].textForeground", Color.WHITE)` antes de crear el botón. |
| 🟡 **Las tarjetas no se ven clicables** | MEDIA | MEDIO | Agregar `setCursor(Cursor.HAND_CURSOR)` y un borde visible `LineBorder` con color suave. En hover, cambiar el borde a un color más oscuro. |
| 🟢 **Los ActionListeners duplicados** | BAJA | MEDIO | Todos los eventos van en `configurarComponentes()`. No poner eventos en el .form. |
| 🟢 **Regeneración del .form desde NetBeans sobreescribe cambios** | BAJA | ALTO | Documentar que el .form debe mantenerse manualmente. Si se abre en el Form Editor, NO hacer clic en "Source" sin respaldo. |
| 🟢 **Compatibilidad con versiones futuras de JDK** | BAJA | BAJO | Solo se usan APIs estándar de `java.swing` y `java.awt` presentes desde JDK 6. |

---

## 9. DIAGRAMA DE ARQUITECTURA (TEXTUAL)

```
┌──────────────────────────────────────────────────────────────┐
│  FrmDashboard (JFrame)                                        │
│  Layout: BorderLayout                                         │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ pnlHeader (BorderLayout, bg=Color(108,91,96), h=40)     │ │ <- PAGE_START
│  │ ┌──────────┐ ┌─────────────────────────────┐ ┌───────┐ │ │
│  │ │lblAppTit │ │ pnlNavButtons (FlowLayout)  │ │btnCerr│ │ │
│  │ │(WEST)    │ │ [Emp] [Dpto] [Marc] [Plan] │ │(EAST) │ │ │
│  │ │          │ │ [Repo] [Cont] [Hor] [Jorn] │ │       │ │ │
│  │ └──────────┘ └─────────────────────────────┘ └───────┘ │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ pnlMainContent (BorderLayout)                           │ │ <- CENTER
│  │                                                         │ │
│  │  ┌──────────────────────────────────────────────────┐  │ │
│  │  │ lblBienvenida (Segoe UI Bold 24, CENTER)         │  │ │ <- PAGE_START
│  │  └──────────────────────────────────────────────────┘  │ │
│  │                                                         │ │
│  │  ┌──────────────────────────────────────────────────┐  │ │
│  │  │ pnlCardsGrid (GridLayout 4x2, gap 10px)         │  │ │ <- CENTER
│  │  │ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────┐│  │ │
│  │  │ │cardEmpl  │ │cardDpto  │ │cardMarc  │ │cardPl││  │ │
│  │  │ │[icono]   │ │[icono]   │ │[icono]   │ │[icono]││  │ │
│  │  │ │Empleados │ │Dptos     │ │Marcación │ │Planil ││  │ │
│  │  │ └──────────┘ └──────────┘ └──────────┘ └──────┘│  │ │
│  │  │ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────┐│  │ │
│  │  │ │cardRepo  │ │cardCont  │ │cardHor   │ │cardJor││  │ │
│  │  │ │[icono]   │ │[icono]   │ │[icono]   │ │[icono]││  │ │
│  │  │ │Reportes  │ │Contratos │ │Horarios  │ │Jornada││  │ │
│  │  │ └──────────┘ └──────────┘ └──────────┘ └──────┘│  │ │
│  │  └──────────────────────────────────────────────────┘  │ │
│  │                                                         │ │
│  │  ┌──────────────────────────────────────────────────┐  │ │
│  │  │ pnlFooter (bg=Color(240,240,245), h=30)         │  │ │ <- PAGE_END
│  │  │ [status text]              [iconos de ayuda]     │  │ │
│  │  └──────────────────────────────────────────────────┘  │ │
│  └─────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

---

## 10. CONCLUSIÓN

El GAP es **estructural**: no se puede alcanzar el diseño objetivo `home.png` simplemente cambiando colores o fuente del `JMenuBar` actual. Se requiere una **reescritura controlada** del formulario, reemplazando el `JMenuBar` por un `JPanel` header con botones de navegación directa, y el área central de labels por una grilla de tarjetas de acceso rápido.

La Opción A (Reemplazo completo) es la única viable. La implementación debe seguir el orden de fases descrito, comenzando por el respaldo, luego la modificación del .form, luego initComponents, luego configurarComponentes. El Look and Feel Nimbus debe mantenerse, con personalización de colores vía `UIManager` o `setBackground()`.

**Estimación de esfuerzo**: ~4-6 horas para un desarrollador familiarizado con Swing y NetBeans.
