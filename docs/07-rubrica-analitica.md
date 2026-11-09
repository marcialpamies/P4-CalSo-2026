# P4 · Rúbrica analítica de doble entrada (100 puntos)

**Rúbrica de evaluación de la práctica, sobre 100 puntos.** Es la única distribución de puntuaciones aplicable a esta entrega.

## Principio de evaluación y fuente de evidencia

El proceso ATDD/TDD se valorará **principalmente con el vídeo individual de un escenario intermedio completo**, contrastado con la secuencia real de commits, pruebas y resultados del repositorio. El escenario no puede ser el primer ejemplo guiado trivial ni solo el último resultado GREEN: debe exigir al menos una regla significativa, varios microciclos y una revisión de diseño. En parejas, **alumno 1** explica un escenario significativo de HU-02 y **alumno 2** uno de HU-03, partiendo del commit GREEN de relevo que permite auditar la secuencia. El segundo no recibe la obligación de diseñar desde cero lo ya integrado; el primero no tiene que desarrollar el proveedor IA. Cada responsabilidad debe poder comprobarse. El repositorio completo fundamenta comportamiento, OO, integración y calidad; el vídeo no sustituye la ejecución.

Se exige que el `README_P4.md` declare la **técnica de trazabilidad elegida y una política de commits** antes de presentar el recorrido: granularidad, convención de nombres, correspondencia con BDD y tests JUnit, preservación de RED/GREEN/REFACTOR auténticos y modo de localizar commits/hashes y ejecuciones en el vídeo. La técnica puede ser una tabla por escenario, etiquetas en mensajes y enlaces permanentes, o un registro equivalente igualmente auditable. No exigir número fijo de commits ni fabricar RED/REFACTOR si no ocurrieron.

**Los mensajes deberán usar la convención en español `TIPO HU-XX BDD-YY: descripción`**, con `ATDD-TEST`, `ATDD-FEAT`, `TDD-TEST`, `TDD-FEAT` y `TDD-REFACTOR`. La técnica de auditoría puede variar, pero **no** la identificación explícita de HU, escenario y naturaleza del cambio. La valoración tendrá en cuenta los commits reales, el diagnóstico RED genuino y el GREEN antes/después de refactorizar, sin premiar fallos o commits fabricados.

Cada celda describe el logro observable y su puntuación exacta. Solo se asigna una celda por criterio. Los cinco niveles equivalen a 100 %, 75 %, 50 %, 25 % y 0 % del máximo, con cuartos de punto cuando sean necesarios. La evidencia de los criterios B1, F1 y F2 debe permitir revisar personalmente el trabajo del estudiante.

## Matriz de doble entrada

### A. ATDD/BDD/Cucumber

| Criterio (máximo) | Excelente · 100 % | Competente · 75 % | Básico · 50 % | Insuficiente · 25 % | Sin evidencia · 0 % |
|---|---|---|---|---|---|
| **A1. Contrato y reglas de negocio (8 p)** | **8 p:** Reconoce todas las reglas y límites relevantes, acuerda ejemplos y motivos antes del código; trazabilidad completa | **6 p:** Reglas y ejemplos correctos con alguna omisión menor de límites o trazabilidad | **4 p:** Identifica reglas principales; faltan ejemplos significativos o acuerdos explícitos | **2 p:** Escenarios desconectados de reglas o expectativas asumidas sin acuerdo | **0 p:** No se identifica contrato verificable |
| **A2. Cucumber y aceptación verificable (10 p)** | **10 p:** Gherkin claro en inglés; parametriza con criterio; todos los Given/When/Then ejecutan verificaciones reales; regresión GREEN y muestra ciclo exterior en vídeo | **7,5 p:** Cucumber funcional y completo, con defectos menores de legibilidad o parametrización | **5 p:** Solo parte de escenarios automatizada o comprobaciones insuficientes en algunos Then | **2,5 p:** Features incompletas o pegamento que apenas comprueba resultados | **0 p:** No hay automatización de aceptación válida |

### B. TDD y dominio

| Criterio (máximo) | Excelente · 100 % | Competente · 75 % | Básico · 50 % | Insuficiente · 25 % | Sin evidencia · 0 % |
|---|---|---|---|---|---|
| **B1. Ciclo TDD demostrado en vídeo (12 p)** | **12 p:** Escenario intermedio auténtico; reconstruye con hashes test JUnit antes de producción, RED de comportamiento, GREEN mínimo y REFACTOR justificado; explica decisiones y pruebas al ejecutarlas | **9 p:** Secuencia real y comprobable, pero falta justificar algún microciclo o refactorización | **6 p:** Muestra RED/GREEN parcial o evidencia de test-first incompleta; vídeo poco convincente | **3 p:** Solo muestra código final y tests o atribuye a TDD fallos de configuración | **0 p:** No hay ciclo demostrable ni evidencia verificable |
| **B2. Refactorización y reglas en dominio (10 p)** | **10 p:** Tras GREEN reconoce responsabilidades, traslada al dominio las reglas de duración, armas, intensidad y equilibrio del combinado cuando surgen, añade protección de clase y mantiene regresión; distingue refactorización de nueva funcionalidad | **7,5 p:** Reglas esencialmente en dominio con una refactorización poco explicada | **5 p:** Distribución mixta aplicación/dominio o escasa protección de cambios | **2,5 p:** Lógica dominante en steps/adaptadores o refactorizaciones rompen comportamiento | **0 p:** No hay diseño de dominio funcional |

### C. Pruebas orientadas a objetos

| Criterio (máximo) | Excelente · 100 % | Competente · 75 % | Básico · 50 % | Insuficiente · 25 % | Sin evidencia · 0 % |
|---|---|---|---|---|---|
| **C1. Pruebas OO de clases y colaboración (8 p)** | **8 p:** Prueba estado, invariantes, agregados, suma y colaboraciones con oráculos útiles; diferencia unidad de integración | **6 p:** Pruebas adecuadas con omisiones menores de invariantes o colaboración | **4 p:** Cubre clases parcialmente y predomina aserción superficial | **2 p:** Tests acoplados a implementación sin comprobar efectos relevantes | **0 p:** No hay pruebas OO |
| **C2. Especialización y extensibilidad (10 p)** | **10 p:** Prueba contratos comunes, técnico, físico y combinado; sustitución, despacho polimórfico y nueva regla 40/60 ubicada en dominio con límites; demuestra regresión y justifica herencia/composición y UML evolutivo | **7,5 p:** Contratos y especializaciones probados con alguna laguna o justificación limitada | **5 p:** Hay especialización pero pocas pruebas de sustitución o extensión | **2,5 p:** Herencia decorativa o nuevo tipo rompe contratos; pruebas triviales | **0 p:** No hay evidencia de variabilidad o contratos |

### D. Dobles y colaboración

| Criterio (máximo) | Excelente · 100 % | Competente · 75 % | Básico · 50 % | Insuficiente · 25 % | Sin evidencia · 0 % |
|---|---|---|---|---|---|
| **D1. Puertos y elección de dobles (6 p)** | **6 p:** Justifica abstracciones por dependencia; diferencia stub (respuestas), mock (interacciones) y fake (estado), usándolos según objetivo | **4,5 p:** Dobles correctos con justificación parcial o algún sustituto poco adecuado | **3 p:** Uso limitado o confunde parcialmente objetivos de los dobles | **1,5 p:** Dobles sin aislamiento efectivo o verificaciones irrelevantes | **0 p:** No hay aislamiento demostrado |
| **D2. Colaboración, integración y efectos (6 p)** | **6 p:** Comprueba no persistencia al rechazar, errores externos, mapeo y colaboración; integra con validación real y declara límites de pruebas aisladas | **4,5 p:** Colaboración e integración comprobadas salvo algún caso secundario | **3 p:** Solo tests aislados, sin confirmar flujo completo o efectos | **1,5 p:** Mocks ocultan reglas reales o aceptaciones ficticias | **0 p:** No existe evidencia de colaboración |

### E. Calidad y métricas

| Criterio (máximo) | Excelente · 100 % | Competente · 75 % | Básico · 50 % | Insuficiente · 25 % | Sin evidencia · 0 % |
|---|---|---|---|---|---|
| **E1. Automatización y métricas (6 p)** | **6 p:** Actions y Maven reproducibles; analiza JaCoCo y SonarQube CalSo con localización y significado de métricas, cobertura no confundida con efectividad | **4,5 p:** Ejecuciones reproducibles y métricas bien interpretadas salvo omisión puntual | **3 p:** Presenta cifras e informes sin análisis suficiente | **1,5 p:** Ejecuciones poco fiables o interpreta erróneamente las cifras | **0 p:** Sin informes ni análisis verificables |
| **E2. Hallazgos y mejora de calidad (6 p)** | **6 p:** Selecciona riesgos relevantes, prioriza mejoras, aporta antes/después, regresión y situación Quality Gate; interpreta excepciones | **4,5 p:** Mejoras fundadas y reevaluadas con documentación parcial | **3 p:** Detecta incidencias sin priorizar o sin comprobar efectos | **1,5 p:** Corrige indiscriminadamente o ignora defectos significativos | **0 p:** Sin análisis ni acciones de mejora |

### F. GitHub, documentación y vídeo

| Criterio (máximo) | Excelente · 100 % | Competente · 75 % | Básico · 50 % | Insuficiente · 25 % | Sin evidencia · 0 % |
|---|---|---|---|---|---|
| **F1. GitHub y política auditable de commits (10 p)** | **10 p:** README define técnica y política antes del ciclo (convenciones, granularidad, RED/GREEN/REFACTOR, hashes, ramas/PR y revisión); historial real permite reproducir el vídeo sin reescrituras | **7,5 p:** Política clara e historial rastreable con una laguna menor de PR o correspondencia | **5 p:** Hay commits y README pero secuencia parcial o difíciles de auditar | **2,5 p:** Commits masivos/retroactivos, política vaga, vídeo no enlaza cambios | **0 p:** No hay historial/política que permita auditar |
| **F2. Documentación, vídeo individual y coordinación (8 p)** | **8 p:** Cada alumno presenta vídeo de 6–7 min de escenario intermedio completo, continuo por sección, legible y con voz sincronizada; README enlaza matriz, pruebas, UML progresivo, métricas, PR y decisiones; reparto y revisiones claros | **6 p:** Vídeo y documentación sólidos con omisiones menores de enlaces o explicación | **4 p:** Vídeo muestra solo parte del ciclo o documentación incompleta y reparto poco claro | **2 p:** Vídeo de pantalla final o difícil de seguir, documentación no permite reconstrucción | **0 p:** Sin vídeo/evidencia documental individual verificable |

## Reparto de puntuación

| Bloque | Puntos |
|---|---:|
| A. ATDD/BDD/Cucumber | 18 |
| B. TDD y dominio | 22 |
| C. Pruebas orientadas a objetos | 18 |
| D. Dobles y colaboración | 12 |
| E. Calidad y métricas | 12 |
| F. GitHub, documentación y vídeo | 18 |
| **Total** | **100** |

## Reglas de aplicación

1. **Vídeo y GitHub se contrastan:** una exposición convincente sin hashes y pruebas reproducibles no acredita por sí sola test-first (B1). Un historial sin explicación individual del ciclo tampoco acredita la competencia comunicada (F2). No duplicar una misma penalización: B1 evalúa el proceso TDD; F1, su auditabilidad; F2, su explicación y documentación.
2. **Selección de escenario intermedio:** se declara antes de grabar en el README; debe incluir Cucumber inicial, test JUnit RED auténtico, GREEN mínimo, REFACTOR si fue necesario, aceptación final y regresión, junto con la necesidad de diseño emergente. Cuando en un microciclo no haya refactorización, se razona por qué y se muestra otra refactorización real del escenario si existe; jamás fabricar commits.
3. **Estados distintos:** undefined/pending de Cucumber no se denominan RED de lógica; errores de configuración tampoco. Un `Then` vacío no acredita aceptación.
4. **Dominio y OO:** dar el máximo si las reglas terminan en dominio con protección y las pruebas comprueban contratos y colaboración; no exigir herencia ornamental. Composición bien justificada puede alcanzar todo C2.
5. **Dobles y validación real:** un stub/mock no reemplaza la aceptación integrada de HU-03; se exigen pruebas con implementación real de las reglas donde corresponda.
6. **Métricas:** cobertura alta, Quality Gate verde o muchas pruebas no bastan sin análisis crítico; se valoran hipótesis, riesgo, mejoras y nueva medición. El Gate fallido impide el nivel excelente de E2 salvo fallo ajeno documentado y contrastado.
7. **Trabajo individual y reparto secuencial:** el alumno 1 desarrolla HU-01/HU-02 hasta BDD-05d, entrega un relevo GREEN y explica HU-02; el alumno 2 desarrolla los ocho escenarios Cucumber obligatorios de HU-03 y los límites de equilibrio en JUnit, y explica HU-03. Los dos realizan pruebas OO y emplean dobles cuando su objetivo lo requiera. No penalizar al primero por no construir el adaptador IA ni al segundo por no haber creado los primeros ejercicios. B1, F1 y F2 requieren evidencias individuales de los incrementos asignados. La falta de evidencia de una persona no penaliza automáticamente a la otra.
8. **Verificabilidad:** el vídeo deberá enlazarse de forma estable desde el README, alojado fuera del repositorio; sin webcam obligatoria, grabación real de pantalla y voz simultánea, cursor y texto legibles, audio inteligible. Secciones y transiciones permitidas, pero sin voz en off independiente de la interacción mostrada.

## Hoja de puntuación y referencias de auditoría

| ID | Máximo | Nivel elegido | Puntos | Enlace / hash / minuto del vídeo / observación |
|---|---:|---|---:|---|
| A1 | 8 | | | |
| A2 | 10 | | | |
| B1 | 12 | | | |
| B2 | 10 | | | |
| C1 | 8 | | | |
| C2 | 10 | | | |
| D1 | 6 | | | |
| D2 | 6 | | | |
| E1 | 6 | | | |
| E2 | 6 | | | |
| F1 | 10 | | | |
| F2 | 8 | | | |
| **Total** | **100** | | | |

**Documentación de apoyo:** [guía para reconstruir commits, elaborar UML y grabar vídeo](08-guia-auditoria-uml-video.md) y [plantilla de entrega](PLANTILLA_README_P4.md).

**Rango esperado de la máxima puntuación:** excelente dominio de los escenarios de Cucumber, TDD test-first auténtico, traslado progresivo de reglas al dominio, pruebas OO y de contrato, dobles ajustados al objetivo, métricas analizadas y una explicación en vídeo auditada mediante GitHub. No se necesita aumentar el tamaño de la aplicación ni acumular clases innecesarias.
